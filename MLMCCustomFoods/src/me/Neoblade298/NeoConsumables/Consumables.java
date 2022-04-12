package me.Neoblade298.NeoConsumables;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.util.BuffType;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.bosschests.AugmentReward;
import me.Neoblade298.NeoConsumables.bosschests.ChestReward;
import me.Neoblade298.NeoConsumables.bosschests.ChestStage;
import me.Neoblade298.NeoConsumables.bosschests.EssenceReward;
import me.Neoblade298.NeoConsumables.bosschests.GearReward;
import me.Neoblade298.NeoConsumables.bosschests.RecipeReward;
import me.Neoblade298.NeoConsumables.bosschests.RelicReward;
import me.Neoblade298.NeoConsumables.bosschests.ResearchBookReward;
import me.Neoblade298.NeoConsumables.bosschests.StoredItemReward;
import me.Neoblade298.NeoConsumables.objects.BuffAction;
import me.Neoblade298.NeoConsumables.objects.ChestConsumable;
import me.Neoblade298.NeoConsumables.objects.Consumable;
import me.Neoblade298.NeoConsumables.objects.FlagAction;
import me.Neoblade298.NeoConsumables.objects.FoodConsumable;
import me.Neoblade298.NeoConsumables.objects.SettingsChanger;
import me.Neoblade298.NeoConsumables.objects.StoredAttributes;
import me.Neoblade298.NeoConsumables.objects.TokenConsumable;
import me.neoblade298.neosettings.NeoSettings;
import me.neoblade298.neosettings.objects.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Consumables extends JavaPlugin implements Listener {
	private static HashMap<String, FoodConsumable> food = new HashMap<String, FoodConsumable>();
	private static HashMap<String, Consumable> consumables = new HashMap<String, Consumable>();
	private static ArrayList<String> defaultWorlds = new ArrayList<String>();
	
	public boolean isInstance = false;
	public Settings settings;
	public Settings hiddenSettings;

	public static String sqlUser;
	public static String sqlPass;
	public static String connection;
	public static Properties properties = new Properties();
	
	static {
		defaultWorlds.add("Argyll");
		defaultWorlds.add("ClassPVP");
		defaultWorlds.add("Dev");
	}

	public void onEnable() {
		isInstance = new File(getDataFolder(), "instance.yml").exists();

		// Settings
		NeoSettings nsettings = (NeoSettings) Bukkit.getPluginManager().getPlugin("NeoSettings");
		settings = nsettings.createSettings("Consumables", this, false);
		settings.addSetting("InventoryUse", false);
		hiddenSettings = nsettings.createSettings("Tokens", this, true);
		hiddenSettings.addSetting("Boss", false);

		// SQL
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		sqlUser = sql.getString("username");
		sqlPass = sql.getString("password");
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		properties.setProperty("useSSL", "false");

		// Load consumables and boss chests
		reload();

		getCommand("cons").setExecutor(new Commands(this, food.keySet()));
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new ConsumableManager(this), this);
	}
	
	public void onDisable() {
		ConsumableManager.saveAll();
		ConsumableManager.handleDisable();
		org.bukkit.Bukkit.getServer().getLogger().info("NeoConsumables Disabled");
		super.onDisable();
	}
	
	public void reload() {
		consumables.clear();
		loadConsumableDirectory(new File(getDataFolder(), "consumables"));
	}

	private void loadConsumableDirectory(File file) {
		for (File subfile : file.listFiles()) {
			if (subfile.isDirectory()) {
				loadConsumableDirectory(subfile);
			}
			else {
				loadConsumable(subfile);
			}
		}
	}

	private void loadConsumable(File file) {
		FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(file);
		for (String key : itemConfig.getKeys(false)) {
			try {
				ConfigurationSection sec = itemConfig.getConfigurationSection(key);
				
				String type = sec.getString("type", "INSTANT_FOOD");
				Consumable cons = null;
				if (type.equals("INSTANT_FOOD")) {
					cons = loadFoodConsumable(sec, key, false);
					consumables.put(key, cons);
				}
				else if (type.equals("DURATION_FOOD")) {
					cons = loadFoodConsumable(sec, key, true);
					consumables.put(key, cons);
				}
				else if (type.equals("CHEST")) {
					cons = loadChestConsumable(sec, key);
					consumables.put(key, cons);
				}
				else if (type.equals("TOKEN")) {
					cons = loadTokenConsumable(sec, key);
					consumables.put(key, cons);
				}
	
				ArrayList<Sound> sounds = cons.getSounds();
				for (String sname : sec.getStringList("sound-effects")) {
					Sound sound = Sound.valueOf(sname.toUpperCase());
					if (sound != null) {
						sounds.add(sound);
					}
				}
			}
			catch (Exception e) {
				Bukkit.getLogger().log(Level.WARNING, "Couldn't load recipe " + key);
				e.printStackTrace();
			}
		}
	}

	private FoodConsumable loadFoodConsumable(ConfigurationSection config, String key, boolean isDuration) {
		FoodConsumable cons = new FoodConsumable(this, key);
		cons.setIsDuration(isDuration);
		
		// Itemstack stuff
		String mat = config.getString("material");
		if (mat.length() > 30) {
			cons.setBase64(mat);
		}
		else {
			cons.setMaterial(mat);
		}
		cons.setDisplay(config.getString("display"));
		cons.setDescription(config.getStringList("desc"));

		// Potion effects
		ArrayList<PotionEffect> potions = cons.getPotions();
		for (String potion : config.getStringList("potion-effects")) {
			String[] split = potion.split(",");
			PotionEffectType type = PotionEffectType.getByName(split[0]);
			int amp = Integer.parseInt(split[1]);
			int duration = Integer.parseInt(split[2]) * 20;
			PotionEffect effect = new PotionEffect(type, duration, amp);
			potions.add(effect);
		}

		// Attributes
		StoredAttributes attribs = cons.getAttributes();
		for (String attribute : config.getStringList("attributes")) {
			String[] split = attribute.split(",");
			String attr = split[0];
			int amp = Integer.parseInt(split[1]);
			attribs.setAttribute(attr, amp);
		}
		if (!attribs.isEmpty()) {
			cons.setAttributeTime(config.getInt("attribute-time"));
		}
		
		for (String flagLine : config.getStringList("flags")) {
			String[] flagArgs = flagLine.split(",");
			String flag = flagArgs[0];
			boolean add = true;
			if (flag.startsWith("-")) {
				add = false;
				flag = flag.substring(1);
			}
			cons.addFlag(new FlagAction(flag, add ? Integer.parseInt(flagArgs[1]) : -1, add));
		}
		
		for (String buffLine : config.getStringList("buffs")) {
			String[] buffArgs = buffLine.split(",");
			double value = Double.parseDouble(buffArgs[1]);
			int duration = Integer.parseInt(buffArgs[2]);
			boolean isPercent = Boolean.parseBoolean(buffArgs[3]);
			cons.addBuff(new BuffAction(BuffType.valueOf(buffArgs[0]), value, duration, isPercent));
		}

		cons.setSpeed(config.getDouble("speed"));
		cons.setSpeedTime(config.getInt("speed-time"));
		cons.setSaturation(config.getDouble("saturation"));
		cons.setHunger(config.getInt("hunger"));
		cons.setHealth(config.getInt("health.amount"));
		cons.setHealthPeriod(config.getInt("health.period", 1));
		cons.setHealthReps(config.getInt("health.repetitions"));
		cons.setMana(config.getInt("mana.amount"));
		cons.setManaPeriod(config.getInt("mana.period", 1));
		cons.setManaReps(config.getInt("mana.repetitions"));
		cons.setCommands((ArrayList<String>) config.getStringList("commands"));
		if (!config.getStringList("worlds").isEmpty()) {
			cons.setWorlds(config.getStringList("worlds"));
		}
		else {
			cons.setWorlds(defaultWorlds);
		}
		cons.setCooldown(config.getInt("cooldown", isDuration ? 30 : 15));
		cons.generateLore();
		food.put(key, cons);
		return cons;
	}

	private ChestConsumable loadChestConsumable(ConfigurationSection config, String key) {
		String internal = config.getString("internal");
		int level = config.getInt("level");
		String display = config.getString("display", internal);
		Sound initSound = Sound.valueOf(config.getString("sound-effects"));

		// Chest stages
		LinkedList<ChestStage> stages = new LinkedList<ChestStage>();
		ConfigurationSection stagesSec = config.getConfigurationSection("stages");
		for (String stage : stagesSec.getKeys(false)) {
			ConfigurationSection stageSec = stagesSec.getConfigurationSection(stage);
			double chance = stageSec.getDouble("chance");
			Sound sound = Sound.ENTITY_ARROW_HIT_PLAYER;
			float pitch = 1.0F;
			String effect = stageSec.getString("effect");
			
			// Sound
			String soundLine = stageSec.getString("sound");
			int index = soundLine.indexOf(":");
			if (index != -1) {
				sound = Sound.valueOf(soundLine.substring(0, index));
				pitch = Float.parseFloat(soundLine.substring(index + 1));
			}
			else {
				sound = Sound.valueOf(soundLine);
			}

			// Rewards
			ArrayList<ChestReward> rewards = new ArrayList<ChestReward>();
			int totalWeight = 0;
			for (String reward : stageSec.getStringList("rewards")) {
				String args[] = reward.replaceAll(" ", "/").replaceAll("_", " ").split("/");
				ChestReward cr = null;
				switch (args[0]) {
				case "essence":
					cr = EssenceReward.parse(args, level);
					break;
				case "gear":
					cr = GearReward.parse(args, level);
					break;
				case "relic":
					cr = RelicReward.parse(args, internal, display);
					break;
				case "recipe":
					cr = RecipeReward.parse(args);
					break;
				case "rbook":
					cr = ResearchBookReward.parse(args, internal, display);
					break;
				case "augment":
					cr = AugmentReward.parse(args, level);
					break;
				case "storeditem":
					cr = StoredItemReward.parse(args, level);
					break;
				}
				if (cr == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoConsumables] Could not load reward: " + reward);
				}
				
				totalWeight += cr.getWeight();
				rewards.add(cr);
			}
			
			stages.add(new ChestStage(chance, sound, pitch, effect, rewards, totalWeight));
		}
		
		return new ChestConsumable(this, key, stages, initSound);
	}

	private TokenConsumable loadTokenConsumable(ConfigurationSection config, String key) {
		TokenConsumable cons = new TokenConsumable(this, key);
		ArrayList<SettingsChanger> settingsChangers = new ArrayList<SettingsChanger>();
		ConfigurationSection scConfig = config.getConfigurationSection("settings");
		if (scConfig != null) {
			for (String settingsChangerKey : scConfig.getKeys(false)) {
				ConfigurationSection sckConfig = scConfig.getConfigurationSection(settingsChangerKey);
				String subsetting = sckConfig.getString("subkey");
				String type = sckConfig.getString("type");
				Object value = null;
				long expiration = sckConfig.getLong("expiration");
				boolean overwrite = sckConfig.getBoolean("overwrite");
				if (type.equalsIgnoreCase("string")) {
					value = sckConfig.getString("value");
				}
				else if (type.equalsIgnoreCase("boolean")) {
					value = sckConfig.getBoolean("value");
				}
				else if (type.equalsIgnoreCase("integer")) {
					value = sckConfig.getInt("value");
				}
				settingsChangers
						.add(new SettingsChanger(this.hiddenSettings, subsetting, value, expiration, overwrite));
			}
			cons.setSettingsChangers(settingsChangers);
		}
		return cons;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((!e.getAction().equals(Action.RIGHT_CLICK_AIR)) && (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		if (!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!SkillAPI.isLoaded(p)) {
			return;
		}
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item == null || item.getType().equals(Material.AIR)) {
			return;
		}
		String key = new NBTItem(item).getString("consumable");

		// Find the food in the inv
		Consumable consumable = null;
		if (consumables.containsKey(key)) {
			consumable = consumables.get(key);
			if (!consumable.canUse(p, item)) {
				return;
			}
		}
		else {
			return;
		}

		// Use consumable
		consumable.use(p, item);
		e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.isRightClick()) {
			return;
		}
		if (e.getClickedInventory() == null) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (e.getClickedInventory() != p.getInventory()) {
			return;
		}
		ItemStack item = e.getClickedInventory().getItem(e.getSlot());
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		String key = new NBTItem(item).getString("consumable");

		if (!consumables.containsKey(key)) {
			return;
		}
		Consumable cons = consumables.get(key);

		// If consumable is a food, continue only if setting is set
		if (cons instanceof FoodConsumable) {
			if (!((boolean) settings.getValue(p.getUniqueId(), "InventoryUse"))) {
				return;
			}
		}

		if (!cons.canUse(p, item)) {
			return;
		}

		e.setCancelled(true);
		cons.use(p, item);
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		if (item.getType().equals(Material.CHEST) && new NBTItem(item).hasKey("consumable")) {
			e.setCancelled(true);
		}
	}
	
	public static FoodConsumable getFood(String key) {
		return food.get(key);
	}
	
	public static Consumable getConsumable(String key) {
		return consumables.get(key);
	}
}
