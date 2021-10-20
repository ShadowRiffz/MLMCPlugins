package me.Neoblade298.NeoConsumables;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;

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
import java.util.UUID;
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

public class NeoConsumables extends JavaPlugin implements Listener {
	HashMap<String, Consumable> consumables = new HashMap<String, Consumable>();
	// These runnables take away attributes from players when they're done being used
	public HashMap<UUID, HashMap<Consumable, AttributeTask>> attributes = new HashMap<UUID, HashMap<Consumable, AttributeTask>>();
	public HashMap<UUID, Long> globalCooldowns = new HashMap<UUID, Long>();
	public HashMap<UUID, HashMap<Consumable, Long>> foodCooldowns = new HashMap<UUID, HashMap<Consumable, Long>>();
	public boolean isInstance = false;
	public Settings settings;
	public Settings hiddenSettings;

	public void onEnable() {
		isInstance = new File(getDataFolder(), "instance.yml").exists();
		loadConfigs();
		
		// Setup databases
		for (Player p : Bukkit.getOnlinePlayers()) {
			UUID uuid = p.getUniqueId();
			attributes.put(uuid, new HashMap<Consumable, AttributeTask>());
			foodCooldowns.put(uuid, new HashMap<Consumable, Long>());
		}

	    getCommand("cons").setExecutor(new Commands(this));
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public void loadConfigs() {
		consumables.clear();
		NeoSettings nsettings = (NeoSettings) Bukkit.getPluginManager().getPlugin("NeoSettings");
		settings = nsettings.createSettings("Consumables", this, false);
		settings.addSetting("InventoryUse", false);
		hiddenSettings = nsettings.createSettings("Tokens", this, true);
		hiddenSettings.addSetting("Boss", false);
		
		for (File file : new File(getDataFolder(), "consumables").listFiles()) {
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
				
				String type = sec.getString("type", "FOOD");
				if (type.equals("FOOD")) {
					FoodConsumable food = loadFoodConsumable(sec, name, sounds, lore);
					consumables.put(food.getName(), food);
				}
				else if (type.equals("CHEST")) {
					ChestConsumable chest = loadChestConsumable(sec, name, sounds, lore);
					consumables.put(chest.getName(), chest);
				}
				else if (type.equals("TOKEN")) {
					TokenConsumable token = loadTokenConsumable(sec, name, sounds, lore);
					consumables.put(token.getName(), token);
				}
				else if (type.equals("RECIPE")) {
					RecipeConsumable recipe = loadRecipeConsumable(sec, name, sounds, lore);
					consumables.put(recipe.getName(), recipe);
				}
			}
		}
	}
	
	private FoodConsumable loadFoodConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		FoodConsumable cons = new FoodConsumable(this, name, sounds, lore);
		
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

	private ChestConsumable loadChestConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		ChestConsumable cons = new ChestConsumable(this, name, sounds, lore);
		cons.setCommands((ArrayList<String>) config.getStringList("commands"));
		return cons;
	}

	private TokenConsumable loadTokenConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		TokenConsumable cons = new TokenConsumable(this, name, sounds, lore);
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
				settingsChangers.add(new SettingsChanger(this.hiddenSettings, subsetting, value, expiration, overwrite));
			}
			cons.setSettingsChangers(settingsChangers);
		}
		return cons;
	}

	private RecipeConsumable loadRecipeConsumable(ConfigurationSection config, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		RecipeConsumable cons = new RecipeConsumable(this, name, sounds, lore);
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
		boolean quickEat = false;
		if (meta.hasLore()) {
			for (String line : meta.getLore()) {
				if (line.contains("Quick Eat") && !item.getType().equals(Material.PRISMARINE_CRYSTALS)) {
					quickEat = true;
				}
			}
		}

		// Find the food in the inv
		Consumable consumable = null;
		if (quickEat) {
			ItemStack[] contents = p.getInventory().getContents();
			for (int i = 0; i < 36; i++) {
				ItemStack invItem = contents[i];
				if (invItem != null && invItem.hasItemMeta() && invItem.getItemMeta().hasLore()) {
					ItemMeta invMeta = invItem.getItemMeta();
					String invName = ChatColor.stripColor(invMeta.getDisplayName());
					if (!consumables.containsKey(invName)) {
						continue;
					}
					consumable = consumables.get(invName);
					if (!(consumable instanceof FoodConsumable)) {
						continue;
					}
					if (!consumable.isSimilar(invMeta)) {
						continue;
					}
					if (!consumable.canUse(p, item)) {
						continue;
					}
					break;
				}
			}
		}
		else {
			if (consumables.containsKey(name)) {
				consumable = consumables.get(name);
				if (!consumable.isSimilar(meta)) {
					return;
				}
				if (!consumable.canUse(p, item)) {
					return;
				}
			}
		}
		
		if (consumable == null) {
			return;
		}
		
		// Use consumable
		consumable.use(p, item);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.isRightClick()) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
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
		
		if (!cons.isSimilar(meta)) {
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
