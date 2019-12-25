package me.neoblade298.neogear;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GearConfig {
	private Main main;
	public String name;
	public Material material;
	public ArrayList<String> prefixes;
	public ArrayList<String> displayNames;
	public int duraBase;
	public ArrayList<Enchant> requiredEnchants;
	public ArrayList<Enchant> optionalEnchants;
	public int enchantmentMin;
	public int enchantmentMax;
	public Attributes attributes;
	public HashMap<String, RarityBonuses> rarities;
	
	public GearConfig(Main main, String name, Material material, ArrayList<String> prefixes, ArrayList<String> displayNames, int duraBase,
			ArrayList<Enchant> requiredEnchants, ArrayList<Enchant> optionalEnchants, int enchantmentMin, int enchantmentMax, Attributes attributes,
			HashMap<String, RarityBonuses> rarities) {
		
		// Add color codes to all strings necessary
		for (String prefix : prefixes) {
			prefix = prefix.replaceAll("&", "§");
		}
		for (String displayName : displayNames) {
			displayName = displayName.replaceAll("&", "§");
		}
		
		this.main = main;
		this.name = name;
		this.material = material;
		this.prefixes = prefixes;
		this.displayNames = displayNames;
		this.duraBase = duraBase;
		this.requiredEnchants = requiredEnchants;
		this.optionalEnchants = optionalEnchants;
		this.enchantmentMax = enchantmentMax;
		this.enchantmentMin = enchantmentMin;
		this.attributes = attributes;
		this.rarities = rarities;
	}
	
	public ItemStack generateItem(String rarity, int level) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		
		// Display
		String prefix = prefixes.get(main.gen.nextInt(prefixes.size()));
		String display = displayNames.get(main.gen.nextInt(displayNames.size()));
		meta.setDisplayName(main.rarities.get(rarity).colorCode + prefix + " " + display);
		
		// Add required enchantments
		for (Enchant enchant : requiredEnchants) {
			int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
			meta.addEnchant(enchant.enchantment, lv, true);
		}
		
		// Add optional enchantments
		int optEnchantNum = enchantmentMin + main.gen.nextInt(enchantmentMax - enchantmentMin + 1);
		ArrayList<Enchant> optEnchants = new ArrayList<Enchant>();
		optEnchants.addAll(optionalEnchants);
		for (int i = 0; i < optEnchantNum; i++) {
			Enchant enchant = optEnchants.get(main.gen.nextInt(optEnchants.size()));
			int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
			meta.addEnchant(enchant.enchantment, lv, true);
			optEnchants.remove(enchant);
		}
		
		// Lore
		Attributes rarityAttrs = rarities.get(rarity).attributes;
		int strength = attributes.strBase + (attributes.strPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.strRange);
		strength += rarityAttrs.strBase + (rarityAttrs.strPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.strRange);
		int dexterity = attributes.dexBase + (attributes.dexPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.dexRange);
		dexterity += rarityAttrs.dexBase + (rarityAttrs.dexPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.dexRange);
		int intelligence = attributes.intBase + (attributes.intPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.intRange);
		intelligence += rarityAttrs.intBase + (rarityAttrs.intPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.intRange);
		int spirit = attributes.sprBase + (attributes.sprPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.sprRange);
		spirit += rarityAttrs.sprBase + (rarityAttrs.sprPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.sprRange);
		int perception = attributes.prcBase + (attributes.prcPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.prcRange);
		perception += rarityAttrs.prcBase + (rarityAttrs.prcPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.prcRange);
		int endurance = attributes.endBase + (attributes.endPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.endRange);
		endurance += rarityAttrs.endBase + (rarityAttrs.endPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.endRange);
		int vitality = attributes.vitBase + (attributes.vitPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.vitRange);
		vitality += rarityAttrs.vitBase + (rarityAttrs.vitPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.vitRange);
		lore.add("§7Tier: " + main.rarities.get(rarity).displayName);
		lore.add("Level Req: " + level);
		lore.add("§9[Base Attributes]");
		if (strength > 0) { lore.add("§9Strength +" + strength); }
		if (dexterity > 0) { lore.add("§9Dexterity +" + strength); }
		if (intelligence > 0) { lore.add("§9Intelligence +" + strength); }
		if (spirit > 0) { lore.add("§9Spirit +" + strength); }
		if (perception > 0) { lore.add("§9Perception +" + strength); }
		if (endurance > 0) { lore.add("§9Endurance +" + strength); }
		if (vitality > 0) { lore.add("§9Vitality +" + strength); }
		
		int durability = duraBase + rarities.get(rarity).duraBonus;
		lore.add("§7Durability " + durability + " / " + durability);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
}
