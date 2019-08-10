package io.github.bananapuncher714;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MLMCTierManager {
	MLMCTieredItemsMain plugin;

	public MLMCTierManager(MLMCTieredItemsMain p) {
		this.plugin = p;
	}

	public void loadTiers() {
		File tierDir = new File(this.plugin.getDataFolder() + File.separator + "tiers");
		tierDir.mkdir();
		File[] arrayOfFile;
		int j = (arrayOfFile = tierDir.listFiles()).length;
		for (int i = 0; i < j; i++) {
			File f = arrayOfFile[i];
			FileConfiguration tier = YamlConfiguration.loadConfiguration(f);
			String name = tier.getString("tier-name").replaceAll("&", "§");
			ArrayList<String> materials = new ArrayList();
			String s;
			for (Iterator localIterator1 = tier.getStringList("materials").iterator(); localIterator1
					.hasNext(); materials.add(s.replaceAll("&", "§"))) {
				s = (String) localIterator1.next();
			}
			ArrayList<String> reqlore = new ArrayList();
			for (Iterator localIterator2 = tier.getStringList("lore.required").iterator(); localIterator2
					.hasNext(); reqlore.add(s.replaceAll("&", "§"))) {
				s = (String) localIterator2.next();
			}
			Object oplore = new ArrayList();
			for (Iterator localIterator3 = tier.getStringList("lore.optional").iterator(); localIterator3
					.hasNext(); ((ArrayList) oplore).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator3.next();
			}
			Object reqEnch = new ArrayList();
			for (Iterator localIterator4 = tier.getStringList("enchantments.required").iterator(); localIterator4
					.hasNext(); ((ArrayList) reqEnch).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator4.next();
			}
			Object opEnch = new ArrayList();
			for (Iterator localIterator5 = tier.getStringList("enchantments.optional").iterator(); localIterator5
					.hasNext(); ((ArrayList) opEnch).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator5.next();
			}
			Object prefix = new ArrayList();
			for (Iterator localIterator6 = tier.getStringList("names.prefix.names").iterator(); localIterator6
					.hasNext(); ((ArrayList) prefix).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator6.next();
			}
			Object names = new ArrayList();
			for (Iterator localIterator7 = tier.getStringList("names.name.names").iterator(); localIterator7
					.hasNext(); ((ArrayList) names).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator7.next();
			}
			Object suffix = new ArrayList();
			for (Iterator localIterator8 = tier.getStringList("names.suffix.names").iterator(); localIterator8
					.hasNext(); ((ArrayList) suffix).add(s.replaceAll("&", "§"))) {
				s = (String) localIterator8.next();
			}
			int loreMin = tier.getInt("lore.optional-min");
			int loreMax = tier.getInt("lore.optional-max");
			int enchMin = tier.getInt("enchantments.optional-min");
			int enchMax = tier.getInt("enchantments.optional-max");
			int preMin = tier.getInt("names.prefix.min-amount");
			int preMax = tier.getInt("names.prefix.max-amount");
			int sufMin = tier.getInt("names.suffix.min-amount");
			int sufMax = tier.getInt("names.suffix.max-amount");
			int minDur = tier.getInt("durability.min-durability");
			int maxDur = tier.getInt("durability.max-durability");
			int durScale = tier.getInt("durability.durability-scale");
			int minPer = tier.getInt("durability.durability-percent-min");
			int maxPer = tier.getInt("durability.durability-percent-max");
			boolean customDur = tier.getBoolean("durability.custom");
			boolean hideEnchants = tier.getBoolean("enchantments.hide");
			boolean unbreakable = tier.getBoolean("durability.unbreakable");
			Tier t = new Tier(name, materials, reqlore, (ArrayList) oplore, (ArrayList) reqEnch, (ArrayList) opEnch,
					(ArrayList) prefix, (ArrayList) names, (ArrayList) suffix, unbreakable);
			t.setValues(hideEnchants, customDur, loreMin, loreMax, enchMin, enchMax, preMin, preMax, sufMin, sufMax,
					minDur, maxDur, durScale, minPer, maxPer);
			this.plugin.registerTier(t);
		}
	}

	public void loadSets() {
		FileConfiguration c = this.plugin.getConfig();
		HashMap<String, ArrayList<String>> sets = new HashMap<String, ArrayList<String>>();
		for (String s : c.getConfigurationSection("sets").getKeys(false)) {
			ArrayList<String> set = new ArrayList<String>(c.getStringList("sets." + s));
			sets.put(s, set);
			this.plugin.getLogger().info("Loaded set '" + s + "'");
		}
		this.plugin.sets = sets;
	}

	public void addDamageables() {
		ArrayList<Material> items = MLMCTieredItemsMain.DAMAGEABLES;
		items.add(Material.GOLDEN_AXE);
		items.add(Material.GOLDEN_HOE);
		items.add(Material.GOLDEN_SHOVEL);
		items.add(Material.GOLDEN_SWORD);
		items.add(Material.GOLDEN_PICKAXE);
		items.add(Material.GOLDEN_HELMET);
		items.add(Material.GOLDEN_CHESTPLATE);
		items.add(Material.GOLDEN_LEGGINGS);
		items.add(Material.DIAMOND_BOOTS);
		items.add(Material.DIAMOND_AXE);
		items.add(Material.DIAMOND_HOE);
		items.add(Material.DIAMOND_SHOVEL);
		items.add(Material.DIAMOND_SWORD);
		items.add(Material.DIAMOND_PICKAXE);
		items.add(Material.DIAMOND_HELMET);
		items.add(Material.DIAMOND_CHESTPLATE);
		items.add(Material.DIAMOND_LEGGINGS);
		items.add(Material.DIAMOND_BOOTS);
		items.add(Material.IRON_AXE);
		items.add(Material.IRON_HOE);
		items.add(Material.IRON_SHOVEL);
		items.add(Material.IRON_SWORD);
		items.add(Material.IRON_PICKAXE);
		items.add(Material.IRON_HELMET);
		items.add(Material.IRON_CHESTPLATE);
		items.add(Material.IRON_LEGGINGS);
		items.add(Material.IRON_BOOTS);
		items.add(Material.STONE_AXE);
		items.add(Material.STONE_HOE);
		items.add(Material.STONE_SHOVEL);
		items.add(Material.STONE_SWORD);
		items.add(Material.STONE_PICKAXE);
		items.add(Material.WOODEN_AXE);
		items.add(Material.WOODEN_HOE);
		items.add(Material.WOODEN_SHOVEL);
		items.add(Material.WOODEN_SWORD);
		items.add(Material.WOODEN_PICKAXE);
		items.add(Material.CHAINMAIL_HELMET);
		items.add(Material.CHAINMAIL_CHESTPLATE);
		items.add(Material.CHAINMAIL_LEGGINGS);
		items.add(Material.CHAINMAIL_BOOTS);
		items.add(Material.LEATHER_HELMET);
		items.add(Material.LEATHER_CHESTPLATE);
		items.add(Material.LEATHER_LEGGINGS);
		items.add(Material.LEATHER_BOOTS);
		items.add(Material.BOW);
		items.add(Material.ELYTRA);
		items.add(Material.SHIELD);
		items.add(Material.CARROT_ON_A_STICK);
		items.add(Material.FISHING_ROD);
		items.add(Material.FLINT_AND_STEEL);
		items.add(Material.SHEARS);
	}
}
