package me.neoblade298.neogear.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neoblade298.neogear.Main;

public class GearConfig {
	private Main main;
	public String name;
	public String display;
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
	
	public GearConfig(Main main, String name, String display, Material material, ArrayList<String> prefixes, ArrayList<String> displayNames, int duraBase,
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
		this.display = display;
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
		
		// If item is a shield, give it damage
		if (material.equals(Material.SHIELD)) {
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(1L, 2L), "Shield", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
		}
		
		// Decide Prefix
		String prefix;
		ArrayList<String> rarityPrefixes = rarities.get(rarity).prefixes;
		if (rarityPrefixes.size() != 0) {
			prefix = rarityPrefixes.get(main.gen.nextInt(rarityPrefixes.size()));
		}
		else {
			prefix = prefixes.get(main.gen.nextInt(prefixes.size()));
		}
		
		// Rest of display
		String display = displayNames.get(main.gen.nextInt(displayNames.size()));
		meta.setDisplayName(main.rarities.get(rarity).colorCode + prefix + " " + display);
		
		// Add required enchantments
		for (Enchant enchant : requiredEnchants) {
			int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
			meta.addEnchant(enchant.enchantment, lv, true);
		}
		
		// Add optional enchantments
		if (enchantmentMax > 0) {
			int optEnchantNum = enchantmentMin + main.gen.nextInt(enchantmentMax - enchantmentMin);
			ArrayList<Enchant> optEnchants = new ArrayList<Enchant>();
			optEnchants.addAll(optionalEnchants);
			for (int i = 0; i < optEnchantNum; i++) {
				Enchant enchant = optEnchants.get(main.gen.nextInt(optEnchants.size()));
				int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
				meta.addEnchant(enchant.enchantment, lv, true);
				optEnchants.remove(enchant);
			}
		}
		
		// Lore
		Attributes rarityAttrs = rarities.get(rarity).attributes;
		int strength = attributes.strBase + (attributes.strPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.strRange + 1);
		strength += rarityAttrs.strBase + (rarityAttrs.strPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.strRange + 1);
		int dexterity = attributes.dexBase + (attributes.dexPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.dexRange + 1);
		dexterity += rarityAttrs.dexBase + (rarityAttrs.dexPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.dexRange + 1);
		int intelligence = attributes.intBase + (attributes.intPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.intRange + 1);
		intelligence += rarityAttrs.intBase + (rarityAttrs.intPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.intRange + 1);
		int spirit = attributes.sprBase + (attributes.sprPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.sprRange + 1);
		spirit += rarityAttrs.sprBase + (rarityAttrs.sprPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.sprRange + 1);
		int perception = attributes.prcBase + (attributes.prcPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.prcRange + 1);
		perception += rarityAttrs.prcBase + (rarityAttrs.prcPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.prcRange + 1);
		int endurance = attributes.endBase + (attributes.endPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.endRange + 1);
		endurance += rarityAttrs.endBase + (rarityAttrs.endPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.endRange + 1);
		int vitality = attributes.vitBase + (attributes.vitPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.vitRange + 1);
		vitality += rarityAttrs.vitBase + (rarityAttrs.vitPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.vitRange + 1);
		int regeneration = attributes.rgnBase + (attributes.rgnPerLvl * (level / main.lvlInterval)) + main.gen.nextInt(attributes.rgnRange + 1);
		regeneration += rarityAttrs.rgnBase + (rarityAttrs.rgnPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.rgnRange + 1);
		vitality += rarityAttrs.vitBase + (rarityAttrs.vitPerLvl * (level / main.lvlInterval))
				+ main.gen.nextInt(rarityAttrs.vitRange + 1);
		lore.add("§7Tier: " + main.rarities.get(rarity).displayName + " " + this.display);
		lore.add("§7Level Req: " + level);
		lore.add("§9[Base Attributes]");
		if (strength > 0) { lore.add("§9Strength +" + (strength - (strength % attributes.strRounded))); }
		if (dexterity > 0) { lore.add("§9Dexterity +" + (dexterity - (dexterity % attributes.dexRounded))); }
		if (intelligence > 0) { lore.add("§9Intelligence +" + (intelligence - (intelligence % attributes.intRounded))); }
		if (spirit > 0) { lore.add("§9Spirit +" + (spirit - (spirit % attributes.strRounded))); }
		if (perception > 0) { lore.add("§9Perception +" + (perception - (perception % attributes.prcRounded))); }
		if (endurance > 0) { lore.add("§9Endurance +" + (endurance - (endurance % attributes.endRounded))); }
		if (vitality > 0) { lore.add("§9Vitality +" + (vitality - (vitality % attributes.vitRounded))); }
		if (regeneration > 0) { lore.add("§9Regen +" + (regeneration - (regeneration % attributes.rgnRounded))); }
		
		int durability = duraBase + rarities.get(rarity).duraBonus;
		lore.add("§7Durability " + durability + " / " + durability);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
}
