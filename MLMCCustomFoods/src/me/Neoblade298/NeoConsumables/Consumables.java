package me.Neoblade298.NeoConsumables;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;

import me.Neoblade298.NeoConsumables.bosschests.AugmentReward;
import me.Neoblade298.NeoConsumables.bosschests.Chest;
import me.Neoblade298.NeoConsumables.bosschests.ChestReward;
import me.Neoblade298.NeoConsumables.bosschests.ChestStage;
import me.Neoblade298.NeoConsumables.bosschests.EssenceReward;
import me.Neoblade298.NeoConsumables.bosschests.GearReward;
import me.Neoblade298.NeoConsumables.bosschests.RecipeReward;
import me.Neoblade298.NeoConsumables.bosschests.RelicReward;
import me.Neoblade298.NeoConsumables.bosschests.ResearchBookReward;
import me.Neoblade298.NeoConsumables.objects.Attributes;
import me.Neoblade298.NeoConsumables.objects.ChestConsumable;
import me.Neoblade298.NeoConsumables.objects.Consumable;
import me.Neoblade298.NeoConsumables.objects.FoodConsumable;
import me.Neoblade298.NeoConsumables.objects.RecipeConsumable;
import me.Neoblade298.NeoConsumables.objects.SettingsChanger;
import me.Neoblade298.NeoConsumables.objects.TokenConsumable;
import me.Neoblade298.NeoConsumables.runnables.AttributeTask;
import me.neoblade298.neosettings.NeoSettings;
import me.neoblade298.neosettings.objects.Settings;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
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
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Consumables extends JavaPlugin implements Listener {
	public static HashMap<String, Consumable> consumables = new HashMap<String, Consumable>();
	public static HashMap<String, Chest> bosschests = new HashMap<String, Chest>();
	// These runnables take away attributes from players when they're done being
	// used
	public HashMap<UUID, HashMap<Consumable, AttributeTask>> attributes = new HashMap<UUID, HashMap<Consumable, AttributeTask>>();
	public HashMap<UUID, Long> globalCooldowns = new HashMap<UUID, Long>();
	public HashMap<UUID, HashMap<Consumable, Long>> foodCooldowns = new HashMap<UUID, HashMap<Consumable, Long>>();
	public boolean isInstance = false;
	public Settings settings;
	public Settings hiddenSettings;

	public void onEnable() {
		isInstance = new File(getDataFolder(), "instance.yml").exists();

		// Settings
		NeoSettings nsettings = (NeoSettings) Bukkit.getPluginManager().getPlugin("NeoSettings");
		settings = nsettings.createSettings("Consumables", this, false);
		settings.addSetting("InventoryUse", false);
		hiddenSettings = nsettings.createSettings("Tokens", this, true);
		hiddenSettings.addSetting("Boss", false);

		// Setup databases
		for (Player p : Bukkit.getOnlinePlayers()) {
			UUID uuid = p.getUniqueId();
			attributes.put(uuid, new HashMap<Consumable, AttributeTask>());
			foodCooldowns.put(uuid, new HashMap<Consumable, Long>());
		}

		// Load consumables and boss chests
		reload();

		getCommand("cons").setExecutor(new Commands(this));
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public void reload() {
		consumables.clear();
		loadConsumableDirectory(new File(getDataFolder(), "consumables"));

		bosschests.clear();
		loadChestDirectory(new File(getDataFolder(), "chests"));
	}

	private void loadChestDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadChestDirectory(file);
			}
			else {
				loadChest(file);
			}
		}
	}

	private void loadChest(File file) {
		FileConfiguration chestConfig = YamlConfiguration.loadConfiguration(file);
		
		// Chests
		for (String chest : chestConfig.getKeys(false)) {
			ConfigurationSection chestSec = chestConfig.getConfigurationSection(chest);
			String internal = chestSec.getString("internal");
			int level = chestSec.getInt("level");
			String display = chestSec.getString("display", internal);

			// Chest stages
			LinkedList<ChestStage> stages = new LinkedList<ChestStage>();
			ConfigurationSection stagesSec = chestSec.getConfigurationSection("stages");
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
					}
					if (cr == null) {
						Bukkit.getLogger().log(Level.WARNING, "[NeoConsumables] Could not load reward: " + reward);
					}
					
					totalWeight += cr.getWeight();
					rewards.add(cr);
				}
				
				stages.add(new ChestStage(chance, sound, pitch, effect, rewards, totalWeight));
			}
			bosschests.put(internal, new Chest(this, internal, level, stages, display));
		}
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
			ConfigurationSection sec = itemConfig.getConfigurationSection(key);
			String name = sec.getString("name");

			ArrayList<Sound> sounds = new ArrayList<Sound>();
			for (String sname : sec.getStringList("sound-effects")) {
				Sound sound = Sound.valueOf(sname.toUpperCase());
				if (sound != null) {
					sounds.add(sound);
				}
			}
			ArrayList<String> lore = new ArrayList<String>();
			for (String loreLine : sec.getStringList("lore")) {
				lore.add(loreLine.replaceAll("&", "§"));
			}

			HashMap<String, String> nbtMap = new HashMap<String, String>();
			ConfigurationSection nbts = sec.getConfigurationSection("nbt");
			if (nbts != null) {
				for (String nbt : nbts.getKeys(false)) {
					nbtMap.put(nbt, nbts.getString(nbt));
				}
			}

			String type = sec.getString("type", "FOOD");
			if (type.equals("FOOD")) {
				FoodConsumable food = loadFoodConsumable(sec, name, sounds, lore, nbtMap);
				consumables.put(food.getName(), food);
			}
			else if (type.equals("CHEST")) {
				ChestConsumable chest = loadChestConsumable(sec, name, sounds, lore, nbtMap);
				consumables.put(chest.getName(), chest);
			}
			else if (type.equals("TOKEN")) {
				TokenConsumable token = loadTokenConsumable(sec, name, sounds, lore, nbtMap);
				consumables.put(token.getName(), token);
			}
			else if (type.equals("RECIPE")) {
				RecipeConsumable recipe = loadRecipeConsumable(sec, name, sounds, lore, nbtMap);
				consumables.put(recipe.getName(), recipe);
			}
		}
	}

	private FoodConsumable loadFoodConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds,
			ArrayList<String> lore, HashMap<String, String> nbts) {
		FoodConsumable cons = new FoodConsumable(this, name, sounds, lore, nbts);

		// Potion effects
		ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
		for (String potion : config.getStringList("effects")) {
			String[] split = potion.split(",");
			PotionEffectType type = PotionEffectType.getByName(split[0]);
			int amp = Integer.parseInt(split[1]);
			int duration = Integer.parseInt(split[2]);
			PotionEffect effect = new PotionEffect(type, duration, amp);
			potions.add(effect);
		}
		cons.setPotions(potions);

		// Attributes
		Attributes attribs = new Attributes();
		for (String attribute : config.getStringList("attributes")) {
			String[] split = attribute.split(",");
			String attr = split[0];
			int amp = Integer.parseInt(split[1]);
			attribs.addAttribute(attr, amp);
		}
		if (!attribs.isEmpty()) {
			cons.setAttributes(attribs);
			cons.setAttributeTime(config.getInt("attributetime"));
		}

		cons.setSaturation(config.getDouble("saturation"));
		cons.setHunger(config.getInt("hunger"));
		cons.setHealth(config.getInt("health.amount"));
		cons.setHealthDelay(config.getInt("health.delay"));
		cons.setHealthTime(config.getInt("health.repetitions"));
		cons.setMana(config.getInt("mana.amount"));
		cons.setManaDelay(config.getInt("mana.delay"));
		cons.setManaTime(config.getInt("mana.repetitions"));
		cons.setCooldown(config.getLong("cooldown"));
		cons.setCommands((ArrayList<String>) config.getStringList("commands"));
		cons.setWorlds(config.getStringList("worlds"));
		cons.setIgnoreGcd(config.getBoolean("ignore-gcd"));
		return cons;
	}

	private ChestConsumable loadChestConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds,
			ArrayList<String> lore, HashMap<String, String> nbts) {
		ChestConsumable cons = new ChestConsumable(this, name, sounds, lore, nbts);
		return cons;
	}

	private TokenConsumable loadTokenConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds,
			ArrayList<String> lore, HashMap<String, String> nbts) {
		TokenConsumable cons = new TokenConsumable(this, name, sounds, lore, nbts);
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

	private RecipeConsumable loadRecipeConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds,
			ArrayList<String> lore, HashMap<String, String> nbts) {
		RecipeConsumable cons = new RecipeConsumable(this, name, sounds, lore, nbts);
		cons.setPermission(config.getString("permission"));
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
		if ((!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName())) {
			return;
		}

		ItemMeta meta = item.getItemMeta();
		String name = ChatColor.stripColor(meta.getDisplayName());

		// Find the food in the inv
		Consumable consumable = null;
		if (consumables.containsKey(name)) {
			consumable = consumables.get(name);
			if (!consumable.isSimilar(item)) {
				return;
			}
			if (!consumable.canUse(p, item)) {
				return;
			}
		}

		if (consumable == null) {
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
		ItemMeta meta = item.getItemMeta();
		String name = ChatColor.stripColor(meta.getDisplayName());

		if (!consumables.containsKey(name)) {
			return;
		}
		Consumable cons = consumables.get(name);

		// If food is a consumable, continue only if setting is set
		if (cons instanceof FoodConsumable) {
			if (!((boolean) settings.getValue(p.getUniqueId(), "InventoryUse"))) {
				return;
			}
		}
		// Recipes always work on right click in inventory
		else if (!(cons instanceof RecipeConsumable)) {
			return;
		}

		if (!cons.isSimilar(item)) {
			return;
		}
		if (!cons.canUse(p, item)) {
			return;
		}

		e.setCancelled(true);
		cons.use(p, item);
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		if (!foodCooldowns.containsKey(uuid)) {
			foodCooldowns.put(uuid, new HashMap<Consumable, Long>());
		}
		if (!attributes.containsKey(uuid)) {
			attributes.put(uuid, new HashMap<Consumable, AttributeTask>());
		}

		if (isInstance) {
			// Remove cooldowns for food if joining a boss instance
			foodCooldowns.get(uuid).clear();
			attributes.get(uuid).clear();
			globalCooldowns.remove(uuid);
		}
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		if (item.getType().equals(Material.CHEST) && item.hasItemMeta() && item.getItemMeta().hasLore()
				&& item.getItemMeta().getLore().get(0).contains("Potential Rewards")) {
			e.setCancelled(true);
		}
	}

	private void resetAttributes(Player p) {
		HashMap<Consumable, AttributeTask> tasks = attributes.get(p.getUniqueId());
		for (Consumable c : tasks.keySet()) {
			tasks.get(c).getTask().cancel();
		}
		tasks.clear();
	}

	@EventHandler
	public void onAttributeUnload(PlayerAttributeUnloadEvent e) {
		resetAttributes(e.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		resetAttributes(e.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		resetAttributes(e.getPlayer());
	}
}
