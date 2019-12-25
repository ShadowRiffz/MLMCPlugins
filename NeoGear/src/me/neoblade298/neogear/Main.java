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
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	private HashMap<String, HashMap<Integer, GearConfig>> settings;
	private YamlConfiguration cfg;
	public int lvlMax;
	public int lvlInterval;
	public HashMap<String, Rarity> rarities; // Color codes within
	public Random gen;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoGear Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		gen = new Random();
		
		loadConfigs();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoGear Disabled");
	    super.onDisable();
	}
	
	public void loadConfigs() {
		File cfg = new File(getDataFolder(), "config.yml");
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
		ConfigurationSection raritySec = this.cfg.getConfigurationSection("rarities");
		for (String rarity : raritySec.getKeys(false)) {
			Rarity rarityObj = new Rarity(raritySec.getString("color-code"), raritySec.getString("display-name"));
			this.rarities.put(rarity, rarityObj);
		}
		
		// Load in all gear files
		for (File file : gearFolder.listFiles()) {
			YamlConfiguration gearCfg = YamlConfiguration.loadConfiguration(file);
			String name = gearCfg.getString("name");
			Material material = Material.getMaterial(gearCfg.getString("material"));
			
			ConfigurationSection nameSec = gearCfg.getConfigurationSection("display-name");
			ArrayList<String> prefixes = (ArrayList<String>) nameSec.getStringList("prefixes");
			ArrayList<String> displayNames = (ArrayList<String>) nameSec.getStringList("name");
			
			ConfigurationSection duraSec = gearCfg.getConfigurationSection("durability");
			int duraMinBase = duraSec.getInt("min-base");
			
			// Parse enchantments
			ConfigurationSection enchSec = gearCfg.getConfigurationSection("enchantments");
			ArrayList<Enchant> reqEnchList = parseEnchantments((ArrayList<String>) gearCfg.getStringList("required"));
			ArrayList<Enchant> optEnchList = parseEnchantments((ArrayList<String>) gearCfg.getStringList("optional"));
			int enchMin = enchSec.getInt("optional-min");
			int enchMax = enchSec.getInt("optional-max");
			
			Attributes attributes = parseAttributes(gearCfg.getConfigurationSection("attributes"));
			
			ConfigurationSection rareSec = gearCfg.getConfigurationSection("rarity");
			HashMap<String, RarityBonuses> rarities = new HashMap<String, RarityBonuses>();
			// Load in rarities
			for (String rarity : this.rarities.keySet()) {
				ConfigurationSection specificRareSec = rareSec.getConfigurationSection(rarity);
				if (specificRareSec != null) {
					rarities.put(rarity, new RarityBonuses(parseAttributes(specificRareSec), specificRareSec.getInt("added-durability")));
				}
			}
			
			ConfigurationSection overrideSec = gearCfg.getConfigurationSection("lvl-overrides");
			for (int i = 0; i <= this.lvlMax; i += this.lvlInterval) {
				HashMap<Integer, GearConfig> gearLvli = new HashMap<Integer, GearConfig>();
				GearConfig gearConf = new GearConfig(this, name, material, prefixes, displayNames, duraMinBase, reqEnchList, optEnchList,
						enchMin, enchMax, attributes, rarities);

				// Level override
				ConfigurationSection lvlOverride = overrideSec.getConfigurationSection(i + "");
				if (lvlOverride != null) {
					overrideLevel(i, gearConf, overrideSec);
				}
				gearLvli.put(i, gearConf);
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
		int dexBase = sec.getInt("dex-base");
		int dexLvl = sec.getInt("dex-per-lvl");
		int dexRange = sec.getInt("dex-range");
		int intBase = sec.getInt("int-base");
		int intLvl = sec.getInt("int-per-lvl");
		int intRange = sec.getInt("int-range");
		int sprBase = sec.getInt("spr-base");
		int sprLvl = sec.getInt("spr-per-lvl");
		int sprRange = sec.getInt("spr-range");
		int prcBase = sec.getInt("prc-base");
		int prcLvl = sec.getInt("prc-per-lvl");
		int prcRange = sec.getInt("prc-range");
		int endBase = sec.getInt("end-base");
		int endLvl = sec.getInt("end-per-lvl");
		int endRange = sec.getInt("end-range");
		int vitBase = sec.getInt("vit-base");
		int vitLvl = sec.getInt("vit-per-lvl");
		int vitRange = sec.getInt("vit-range");
		
		return new Attributes(strBase, strLvl, strRange, dexBase, dexLvl, dexRange, intBase, intLvl, intRange,
				sprBase, sprLvl, sprRange, prcBase, prcLvl, prcRange, endBase, endLvl, endRange, vitBase, vitLvl, vitRange);
	}
	
	private Attributes overrideAttributes(Attributes current, ConfigurationSection sec) {
		int strBase = sec.getInt("str-base", -1) != -1 ? sec.getInt("str-base", -1) : current.strBase;
		int strLvl = sec.getInt("str-per-lvl", -1) != -1 ? sec.getInt("str-per-lvl", -1) : current.strPerLvl;
		int strRange = sec.getInt("str-range", -1) != -1 ? sec.getInt("str-range", -1) : current.strRange;
		int dexBase = sec.getInt("dex-base", -1) != -1 ? sec.getInt("dex-base", -1) : current.dexBase;
		int dexLvl = sec.getInt("dex-per-lvl", -1) != -1 ? sec.getInt("dex-per-lvl", -1) : current.dexPerLvl;
		int dexRange = sec.getInt("dex-range", -1) != -1 ? sec.getInt("dex-range", -1) : current.dexRange;
		int intBase = sec.getInt("int-base", -1) != -1 ? sec.getInt("int-base", -1) : current.intBase;
		int intLvl = sec.getInt("int-per-lvl", -1) != -1 ? sec.getInt("int-per-lvl", -1) : current.intPerLvl;
		int intRange = sec.getInt("int-range", -1) != -1 ? sec.getInt("int-range", -1) : current.intRange;
		int sprBase = sec.getInt("spr-base", -1) != -1 ? sec.getInt("spr-base", -1) : current.sprBase;
		int sprLvl = sec.getInt("spr-per-lvl", -1) != -1 ? sec.getInt("spr-per-lvl", -1) : current.sprPerLvl;
		int sprRange = sec.getInt("spr-range", -1) != -1 ? sec.getInt("spr-range", -1) : current.sprRange;
		int prcBase = sec.getInt("prc-base", -1) != -1 ? sec.getInt("prc-base", -1) : current.prcBase;
		int prcLvl = sec.getInt("prc-per-lvl", -1) != -1 ? sec.getInt("prc-per-lvl", -1) : current.prcPerLvl;
		int prcRange = sec.getInt("prc-range", -1) != -1 ? sec.getInt("prc-range", -1) : current.prcRange;
		int endBase = sec.getInt("end-base", -1) != -1 ? sec.getInt("end-base", -1) : current.endBase;
		int endLvl = sec.getInt("end-per-lvl", -1) != -1 ? sec.getInt("end-per-lvl", -1) : current.endPerLvl;
		int endRange = sec.getInt("end-range", -1) != -1 ? sec.getInt("end-range", -1) : current.endRange;
		int vitBase = sec.getInt("vit-base", -1) != -1 ? sec.getInt("vit-base", -1) : current.vitBase;
		int vitLvl = sec.getInt("vit-per-lvl", -1) != -1 ? sec.getInt("vit-per-lvl", -1) : current.vitPerLvl;
		int vitRange = sec.getInt("vit-range", -1) != -1 ? sec.getInt("vit-range", -1) : current.vitRange;
		
		return new Attributes(strBase, strLvl, strRange, dexBase, dexLvl, dexRange, intBase, intLvl, intRange, sprBase, sprLvl,
				sprRange, prcBase, prcLvl, prcRange, endBase, endLvl, endRange, vitBase, vitLvl, vitRange);
	}
	
	private void overrideLevel(int level, GearConfig conf, ConfigurationSection sec) {
		Material material = Material.getMaterial(sec.getString("material", "Stone"));
		if (!material.equals(Material.STONE)) {
			conf.material = material;
		}
		
		ConfigurationSection nameSec = sec.getConfigurationSection("display-name");
		if (nameSec != null) {
			ArrayList<String> prefixes = (ArrayList<String>) nameSec.getStringList("prefixes");
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
			ArrayList<Enchant> reqEnchList = parseEnchantments((ArrayList<String>) sec.getStringList("required"));
			ArrayList<Enchant> optEnchList = parseEnchantments((ArrayList<String>) sec.getStringList("optional"));
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
		conf.attributes = overrideAttributes(conf.attributes, attrSec);
		
		ConfigurationSection raresSec = sec.getConfigurationSection("rarity");
		// Load in rarities
		if (raresSec != null) {
			for (String rarity : this.rarities.keySet()) {
				ConfigurationSection raritySec = raresSec.getConfigurationSection(rarity);
				if (raritySec != null) {
					int duraBonus = raritySec.getInt("added-durability");
					
					conf.rarities.put(rarity, new RarityBonuses(parseAttributes(raritySec.getConfigurationSection(rarity)), duraBonus));
				}
			}
		}
	}
}
