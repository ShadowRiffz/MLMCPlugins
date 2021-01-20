package me.neoblade298.neogear;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neogear.objects.Attributes;
import me.neoblade298.neogear.objects.Enchant;
import me.neoblade298.neogear.objects.GearConfig;
import me.neoblade298.neogear.objects.ItemSet;
import me.neoblade298.neogear.objects.Rarity;
import me.neoblade298.neogear.objects.RarityBonuses;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	public HashMap<String, HashMap<Integer, GearConfig>> settings;
	private YamlConfiguration cfg;
	public int lvlMax;
	public int lvlInterval;
	public HashMap<String, Rarity> rarities; // Color codes within
	public HashMap<String, ArrayList<String>> raritySets;
	public HashMap<String, ItemSet> itemSets;
	public HashMap<String, String> typeConverter;
	public Random gen;
    private static Economy econ = null;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoGear Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("gear").setExecutor(new Commands(this));
		gen = new Random();
		
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        typeConverter = new HashMap<String, String>();
        typeConverter.put("reinforced helmet", "rhelmet");
        typeConverter.put("reinforced chestplate", "rchestplate");
        typeConverter.put("reinforced leggings", "rleggings");
        typeConverter.put("reinforced boots", "rboots");
        typeConverter.put("infused helmet", "ihelmet");
        typeConverter.put("infused chestplate", "ichestplate");
        typeConverter.put("infused leggings", "ileggings");
        typeConverter.put("infused boots", "iboots");

		loadConfigs();
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoGear Disabled");
	    super.onDisable();
	}
	
	public void loadConfigs() {File cfg = new File(getDataFolder(), "config.yml");
		File gearFolder = new File(getDataFolder().getPath() + "/gear");
		
		// Save config if doesn't exist
		if (!cfg.exists()) {
			saveResource("config.yml", false);
		}
		this.cfg = YamlConfiguration.loadConfiguration(cfg);
		
		// Load config
		this.lvlInterval = this.cfg.getInt("lvl-interval");
		this.lvlMax = this.cfg.getInt("lvl-max");

		// Rarities and color codes
		this.rarities = new HashMap<String, Rarity>();
		ConfigurationSection raritySec = this.cfg.getConfigurationSection("rarities");
		for (String rarity : raritySec.getKeys(false)) {
			ConfigurationSection specificRarity = raritySec.getConfigurationSection(rarity);
			Rarity rarityObj = new Rarity(specificRarity.getString("color-code"), specificRarity.getString("display-name"), specificRarity.getDouble("price-modifier"));
			this.rarities.put(rarity, rarityObj);
		}
		
		// Rarity sets
		this.raritySets = new HashMap<String, ArrayList<String>>();
		ConfigurationSection rareSets = this.cfg.getConfigurationSection("rarity-sets");
		for (String set : rareSets.getKeys(false)) {
			this.raritySets.put(set, (ArrayList<String>) rareSets.getStringList(set));
		}
		
		// Item sets
		this.itemSets = new HashMap<String, ItemSet>();
		ConfigurationSection itemSets = this.cfg.getConfigurationSection("item-sets");
		for (String set : itemSets.getKeys(false)) {
			ArrayList<String> setContents = (ArrayList<String>) itemSets.getStringList(set);
			ItemSet itemset = new ItemSet(this, setContents);
			this.itemSets.put(set, itemset);
		}
		
		// Set up gear folder
		if (!gearFolder.exists()) {
			gearFolder.mkdir();
		}
		
		// Load in all gear files
		this.settings = new HashMap<String, HashMap<Integer, GearConfig>>();
		for (File file : gearFolder.listFiles()) {
			YamlConfiguration gearCfg = YamlConfiguration.loadConfiguration(file);
			String name = gearCfg.getString("name");
			String display = gearCfg.getString("display");
			Material material = Material.getMaterial(gearCfg.getString("material").toUpperCase());
			double price = gearCfg.getDouble("price");
			
			ConfigurationSection nameSec = gearCfg.getConfigurationSection("display-name");
			ArrayList<String> prefixes = (ArrayList<String>) nameSec.getStringList("prefix");
			ArrayList<String> displayNames = (ArrayList<String>) nameSec.getStringList("name");
			
			ConfigurationSection duraSec = gearCfg.getConfigurationSection("durability");
			int duraMinBase = duraSec.getInt("base");
			
			// Parse enchantments
			ConfigurationSection enchSec = gearCfg.getConfigurationSection("enchantments");
			ArrayList<Enchant> reqEnchList = parseEnchantments((ArrayList<String>) enchSec.getStringList("required"));
			ArrayList<Enchant> optEnchList = parseEnchantments((ArrayList<String>) enchSec.getStringList("optional"));
			int enchMin = enchSec.getInt("optional-min");
			int enchMax = enchSec.getInt("optional-max");
			
			Attributes attributes = parseAttributes(gearCfg.getConfigurationSection("attributes"));
			
			ConfigurationSection rareSec = gearCfg.getConfigurationSection("rarity");
			HashMap<String, RarityBonuses> rarities = new HashMap<String, RarityBonuses>();
			// Load in rarities
			for (String rarity : this.rarities.keySet()) {
				ConfigurationSection specificRareSec = rareSec.getConfigurationSection(rarity);
				if (specificRareSec != null) {
					rarities.put(rarity, new RarityBonuses(parseAttributes(specificRareSec), specificRareSec.getInt("added-durability"),
							(ArrayList<String>) specificRareSec.getStringList("prefix")));
				}
				else {
					rarities.put(rarity,  new RarityBonuses());
				}
			}
			
			ConfigurationSection overrideSec = gearCfg.getConfigurationSection("lvl-overrides");
			if (overrideSec != null) {
				HashMap<Integer, GearConfig> gearLvli = new HashMap<Integer, GearConfig>();
				for (int i = 0; i <= this.lvlMax; i += this.lvlInterval) {
					GearConfig gearConf = new GearConfig(this, name, display, material, prefixes, displayNames, duraMinBase, reqEnchList, optEnchList,
							enchMin, enchMax, attributes, rarities, price);
	
					// Level override
					ConfigurationSection lvlOverride = overrideSec.getConfigurationSection(i + "");
					if (lvlOverride != null) {
						overrideLevel(i, gearConf, lvlOverride);
					}
					gearLvli.put(i, gearConf);
				}
				settings.put(name, gearLvli);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private ArrayList<Enchant> parseEnchantments(ArrayList<String> enchList) {
		ArrayList<Enchant> enchantments = new ArrayList<Enchant>();
		for (String ench : enchList) {
			String[] enchParams = ench.split(":");
			enchantments.add(new Enchant(Enchantment.getByName(enchParams[0]), Integer.parseInt(enchParams[1]), Integer.parseInt(enchParams[2])));
		}
		return enchantments;
	}
	
	private Attributes parseAttributes(ConfigurationSection sec) {
		int strBase = sec.getInt("str-base");
		int strLvl = sec.getInt("str-per-lvl");
		int strRange = sec.getInt("str-range");
		int strRounded = sec.getInt("str-rounded", 1);
		int dexBase = sec.getInt("dex-base");
		int dexLvl = sec.getInt("dex-per-lvl");
		int dexRange = sec.getInt("dex-range");
		int dexRounded = sec.getInt("dex-rounded", 1);
		int intBase = sec.getInt("int-base");
		int intLvl = sec.getInt("int-per-lvl");
		int intRange = sec.getInt("int-range");
		int intRounded = sec.getInt("int-rounded", 1);
		int sprBase = sec.getInt("spr-base");
		int sprLvl = sec.getInt("spr-per-lvl");
		int sprRange = sec.getInt("spr-range");
		int sprRounded = sec.getInt("spr-rounded", 1);
		int prcBase = sec.getInt("prc-base");
		int prcLvl = sec.getInt("prc-per-lvl");
		int prcRange = sec.getInt("prc-range");
		int prcRounded = sec.getInt("prc-rounded", 1);
		int endBase = sec.getInt("end-base");
		int endLvl = sec.getInt("end-per-lvl");
		int endRange = sec.getInt("end-range");
		int endRounded = sec.getInt("end-rounded", 1);
		int vitBase = sec.getInt("vit-base");
		int vitLvl = sec.getInt("vit-per-lvl");
		int vitRange = sec.getInt("vit-range");
		int vitRounded = sec.getInt("vit-rounded", 1);
		int rgnBase = sec.getInt("rgn-base");
		int rgnLvl = sec.getInt("rgn-per-lvl");
		int rgnRange = sec.getInt("rgn-range");
		int rgnRounded = sec.getInt("rgn-rounded", 1);
		
		return new Attributes(strBase, strLvl, strRange, strRounded, dexBase, dexLvl, dexRange, dexRounded, intBase, intLvl,
				intRange, intRounded, sprBase, sprLvl, sprRange, sprRounded, prcBase, prcLvl, prcRange, prcRounded, endBase,
				endLvl, endRange, endRounded, vitBase, vitLvl, vitRange, vitRounded, rgnBase, rgnLvl, rgnRange, rgnRounded);
	}
	
	private Attributes overrideAttributes(Attributes current, ConfigurationSection sec) {
		int strBase = sec.getInt("str-base", -1) != -1 ? sec.getInt("str-base", -1) : current.strBase;
		int strLvl = sec.getInt("str-per-lvl", -1) != -1 ? sec.getInt("str-per-lvl", -1) : current.strPerLvl;
		int strRange = sec.getInt("str-range", -1) != -1 ? sec.getInt("str-range", -1) : current.strRange;
		int strRounded = sec.getInt("str-rounded", -1) != -1 ? sec.getInt("str-rounded", -1) : current.strRounded;
		int dexBase = sec.getInt("dex-base", -1) != -1 ? sec.getInt("dex-base", -1) : current.dexBase;
		int dexLvl = sec.getInt("dex-per-lvl", -1) != -1 ? sec.getInt("dex-per-lvl", -1) : current.dexPerLvl;
		int dexRange = sec.getInt("dex-range", -1) != -1 ? sec.getInt("dex-range", -1) : current.dexRange;
		int dexRounded = sec.getInt("dex-rounded", -1) != -1 ? sec.getInt("dex-rounded", -1) : current.dexRounded;
		int intBase = sec.getInt("int-base", -1) != -1 ? sec.getInt("int-base", -1) : current.intBase;
		int intLvl = sec.getInt("int-per-lvl", -1) != -1 ? sec.getInt("int-per-lvl", -1) : current.intPerLvl;
		int intRange = sec.getInt("int-range", -1) != -1 ? sec.getInt("int-range", -1) : current.intRange;
		int intRounded = sec.getInt("int-rounded", -1) != -1 ? sec.getInt("int-rounded", -1) : current.intRounded;
		int sprBase = sec.getInt("spr-base", -1) != -1 ? sec.getInt("spr-base", -1) : current.sprBase;
		int sprLvl = sec.getInt("spr-per-lvl", -1) != -1 ? sec.getInt("spr-per-lvl", -1) : current.sprPerLvl;
		int sprRange = sec.getInt("spr-range", -1) != -1 ? sec.getInt("spr-range", -1) : current.sprRange;
		int sprRounded = sec.getInt("spr-rounded", -1) != -1 ? sec.getInt("spr-rounded", -1) : current.sprRounded;
		int prcBase = sec.getInt("prc-base", -1) != -1 ? sec.getInt("prc-base", -1) : current.prcBase;
		int prcLvl = sec.getInt("prc-per-lvl", -1) != -1 ? sec.getInt("prc-per-lvl", -1) : current.prcPerLvl;
		int prcRange = sec.getInt("prc-range", -1) != -1 ? sec.getInt("prc-range", -1) : current.prcRange;
		int prcRounded = sec.getInt("prc-rounded", -1) != -1 ? sec.getInt("prc-rounded", -1) : current.prcRounded;
		int endBase = sec.getInt("end-base", -1) != -1 ? sec.getInt("end-base", -1) : current.endBase;
		int endLvl = sec.getInt("end-per-lvl", -1) != -1 ? sec.getInt("end-per-lvl", -1) : current.endPerLvl;
		int endRange = sec.getInt("end-range", -1) != -1 ? sec.getInt("end-range", -1) : current.endRange;
		int endRounded = sec.getInt("end-rounded", -1) != -1 ? sec.getInt("end-rounded", -1) : current.endRounded;
		int vitBase = sec.getInt("vit-base", -1) != -1 ? sec.getInt("vit-base", -1) : current.vitBase;
		int vitLvl = sec.getInt("vit-per-lvl", -1) != -1 ? sec.getInt("vit-per-lvl", -1) : current.vitPerLvl;
		int vitRange = sec.getInt("vit-range", -1) != -1 ? sec.getInt("vit-range", -1) : current.vitRange;
		int vitRounded = sec.getInt("vit-rounded", -1) != -1 ? sec.getInt("vit-rounded", -1) : current.vitRounded;
		int rgnBase = sec.getInt("rgn-base", -1) != -1 ? sec.getInt("rgn-base", -1) : current.rgnBase;
		int rgnLvl = sec.getInt("rgn-per-lvl", -1) != -1 ? sec.getInt("rgn-per-lvl", -1) : current.rgnPerLvl;
		int rgnRange = sec.getInt("rgn-range", -1) != -1 ? sec.getInt("rgn-range", -1) : current.rgnRange;
		int rgnRounded = sec.getInt("rgn-rounded", -1) != -1 ? sec.getInt("rgn-rounded", -1) : current.rgnRounded;

		return new Attributes(strBase, strLvl, strRange, strRounded, dexBase, dexLvl, dexRange, dexRounded, intBase, intLvl,
				intRange, intRounded, sprBase, sprLvl, sprRange, sprRounded, prcBase, prcLvl, prcRange, prcRounded, endBase,
				endLvl, endRange, endRounded, vitBase, vitLvl, vitRange, vitRounded, rgnBase, rgnLvl, rgnRange, rgnRounded);
	}
	
	private RarityBonuses overrideRarities(RarityBonuses current, ConfigurationSection sec) {
		Attributes currAttr = current.attributes;
		Attributes newAttr = overrideAttributes(currAttr, sec);
		int addedDura = sec.getInt("added-durability", -1) != -1 ? sec.getInt("added-durability", -1) : current.duraBonus;
		ArrayList<String> currPrefixes = current.prefixes;
		ArrayList<String> newPrefixes = (ArrayList<String>) sec.getStringList("prefix");
		ArrayList<String> changedPrefixes = currPrefixes;
		if (newPrefixes != null) {
			changedPrefixes = currPrefixes.equals(newPrefixes) ? currPrefixes : newPrefixes;
		}
		
		return new RarityBonuses(newAttr, addedDura, changedPrefixes);
	}
	
	private void overrideLevel(int level, GearConfig conf, ConfigurationSection sec) {
		Material material = Material.getMaterial(sec.getString("material", "STONE").toUpperCase());
		if (material != null && !material.equals(Material.STONE)) {
			conf.material = material;
		}
		
		double price = sec.getDouble("price", -1);
		if (price != -1) {
			conf.price = price;
		}
		
		ConfigurationSection nameSec = sec.getConfigurationSection("display-name");
		if (nameSec != null) {
			ArrayList<String> prefixes = (ArrayList<String>) nameSec.getStringList("prefix");
			ArrayList<String> displayNames = (ArrayList<String>) nameSec.getStringList("name");
			if (!prefixes.isEmpty()) {
				conf.prefixes = prefixes;
			}
			if (!displayNames.isEmpty()) {
				conf.displayNames = displayNames;
			}
		}
		
		ConfigurationSection duraSec = sec.getConfigurationSection("durability");
		if (duraSec != null) {
			int duraBase = duraSec.getInt("base", -1);
			if (duraBase != -1) {
				conf.duraBase = duraBase;
			}
		}
		
		// Parse enchantments
		ConfigurationSection enchSec = sec.getConfigurationSection("enchantments");
		if (enchSec != null) {
			ArrayList<Enchant> reqEnchList = parseEnchantments((ArrayList<String>) enchSec.getStringList("required"));
			ArrayList<Enchant> optEnchList = parseEnchantments((ArrayList<String>) enchSec.getStringList("optional"));
			int enchMin = enchSec.getInt("optional-min", -1);
			int enchMax = enchSec.getInt("optional-max", -1);
			if (!reqEnchList.isEmpty()) {
				conf.requiredEnchants = reqEnchList;
			}
			if (!optEnchList.isEmpty()) {
				conf.optionalEnchants = optEnchList;
			}
			if (enchMin != -1) {
				conf.enchantmentMin = enchMin;
			}
			if (enchMax != -1) {
				conf.enchantmentMax = enchMax;
			}
		}
		
		ConfigurationSection attrSec = sec.getConfigurationSection("attributes");
		if (attrSec != null) {
			conf.attributes = overrideAttributes(conf.attributes, attrSec);
		}
		
		ConfigurationSection raresSec = sec.getConfigurationSection("rarity");
		// Load in rarities
		if (raresSec != null) {
			for (String rarity : this.rarities.keySet()) {
				ConfigurationSection raritySec = raresSec.getConfigurationSection(rarity);
				if (raritySec != null) {
					conf.rarities.put(rarity, overrideRarities(conf.rarities.get(rarity), raritySec));
				}
			}
		}
	}
	
	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent e) {
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = e.getInventory().getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			if (item != null) {
				if (isQuestGear(item)) {
					e.setResult(null);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onChangeWorlds(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		String from = e.getFrom().getWorld().getName();
		String to = e.getTo().getWorld().getName();
		
		// Only consider changing worlds
		if (!from.equals(to)) {
			if (!to.equals("Argyll") && !to.equals("ClassPVP")) {
				PlayerInventory inv = p.getInventory();
				ItemStack[] armor = inv.getArmorContents();
				for (int i = 0; i <= 3; i++) {
					if (armor[i] != null && isQuestGear(armor[i])) {
						if (inv.firstEmpty() != -1) {
							inv.addItem(armor[i]);
							armor[i] = null;
						}
						else {
							e.setCancelled(true);
							p.sendMessage("§c[§4§lMLMC§4] §cYou must take off your quest armor before changing worlds!");
							break;
						}
					}
				}
				inv.setArmorContents(armor);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		String world = p.getWorld().getName();
		if (!world.equals("Argyll") && !world.equals("ClassPVP")) {
			if (isQuestGear(item)) {
				e.setCancelled(true);
				p.sendMessage("§c[§4§lMLMC§4] §cYou cannot use quest gear in this world!");
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		String world = e.getEntity().getWorld().getName();
		if (!world.equals("Argyll") && !world.equals("ClassPVP")) {
			if (e.getDamager() instanceof Player) {
				Player p = (Player) e.getDamager();
				ItemStack[] weapons = { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand() };
				for (ItemStack item : weapons) {
					if (isQuestGear(item)) {
						e.setCancelled(true);
						p.sendMessage("§c[§4§lMLMC§4] §cYou cannot use quest gear in this world!");
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		String world = e.getEntity().getWorld().getName();
		if (!world.equals("Argyll") && !world.equals("ClassPVP")) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				ItemStack item = e.getBow();
				if (isQuestGear(item)) {
					e.setCancelled(true);
					p.sendMessage("§c[§4§lMLMC§4] §cYou cannot use quest gear in this world!");
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		String world = e.getView().getPlayer().getWorld().getName();
		if (!world.equals("Argyll") && !world.equals("ClassPVP")) {
			PlayerInventory inv = (PlayerInventory) e.getView().getBottomInventory();
			InventoryAction action = e.getAction();
			
			// Disable shift clicking armor
			if (action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && e.isShiftClick()) {
				ItemStack item = e.getCurrentItem();
				if (item.getType().toString().endsWith("HELMET")) {
					if (inv.getContents()[39] == null && isQuestGear(item)) {
						e.setCancelled(true);
						return;
					}
				}
				else if (item.getType().toString().endsWith("CHESTPLATE")) {
					if (inv.getContents()[38] == null && isQuestGear(item)) {
						e.setCancelled(true);
						return;
					}
				}
				else if (item.getType().toString().endsWith("LEGGINGS")) {
					if (inv.getContents()[37] == null && isQuestGear(item)) {
						e.setCancelled(true);
						return;
					}
				}
				else if (item.getType().toString().endsWith("BOOTS")) {
					if (inv.getContents()[36] == null && isQuestGear(item)) {
						e.setCancelled(true);
						return;
					}
				}
			}
			
			// Disable hotbar keying armor
			else if (action.equals(InventoryAction.HOTBAR_SWAP) && e.getSlot() >= 36 && e.getSlot() <= 39) {
				ItemStack item = inv.getContents()[e.getHotbarButton()];
				if (isQuestGear(item)) {
					e.setCancelled(true);
					return;
				}
			}
			
			// Disable dropping armor
			else if (action.equals(InventoryAction.PLACE_ALL) || action.equals(InventoryAction.PLACE_ONE) ||
					action.equals(InventoryAction.SWAP_WITH_CURSOR)) {
				if (e.getSlotType().equals(SlotType.ARMOR) && isQuestGear(e.getCursor())) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	public boolean isQuestGear(ItemStack item) {
		return item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Tier");
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		String world = e.getView().getPlayer().getWorld().getName();
		if (!world.equals("Argyll") && !world.equals("ClassPVP")) {
			if (e.getInventorySlots().size() == 1) {
				for (Integer i : e.getInventorySlots()) {
					if (i >= 36 && i <= 39) {
						if (isQuestGear(e.getOldCursor())) {
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSmith(PrepareSmithingEvent e) {
		ItemStack result = e.getResult();
		if (isQuestGear(result)) {
			result = null;
		}
	}
	
	public Economy getEcon() {
		return econ;
	}
}
