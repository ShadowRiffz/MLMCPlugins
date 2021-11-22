package me.neoblade298.neogear.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.neoblade298.neogear.Gear;
import net.md_5.bungee.api.ChatColor;

public class GearConfig {
	private Gear main;
	public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	public String name, display, title;
	public Material material;
	public ArrayList<String> prefixes, displayNames;
	public int duraBase;
	public ArrayList<String> requiredAugments;
	public ArrayList<Enchant> requiredEnchants, optionalEnchants;
	public int enchantmentMin, enchantmentMax;
	public int startingSlotsBase, startingSlotsRange;
	public int slotsMax;
	public Attributes attributes;
	public HashMap<String, RarityBonuses> rarities;
	public double price;
	
	public GearConfig(Gear main, String name, String display, String title, Material material, ArrayList<String> prefixes, ArrayList<String> displayNames,
			int duraBase, ArrayList<Enchant> requiredEnchants, ArrayList<Enchant> optionalEnchants, ArrayList<String> requiredAugments,
			int enchantmentMin, int enchantmentMax, Attributes attributes, HashMap<String, RarityBonuses> rarities, int slotsMax,
			int startingSlotsBase, int startingSlotsRange, double price) {
		
		// Add color codes to all strings necessary
		for (String prefix : prefixes) {
			prefix = prefix.replaceAll("&", "�");
		}
		for (String displayName : displayNames) {
			displayName = displayName.replaceAll("&", "�");
		}
		
		this.main = main;
		this.name = name;
		if (title == null) {
			this.title = "�7Standard " + display;
		}
		else {
			this.title = title;
		}
		this.display = display;
		this.material = material;
		this.prefixes = prefixes;
		this.displayNames = displayNames;
		this.duraBase = duraBase;
		this.requiredEnchants = requiredEnchants;
		this.optionalEnchants = optionalEnchants;
		this.requiredAugments = requiredAugments;
		this.enchantmentMax = enchantmentMax;
		this.enchantmentMin = enchantmentMin;
		this.attributes = attributes;
		this.rarities = rarities;
		this.price = price;
		this.slotsMax = slotsMax;
		this.startingSlotsBase = startingSlotsBase;
		this.startingSlotsRange = startingSlotsRange;
	}
	
	public ItemStack generateItem(String rarity, int level) {
		ItemStack item = new ItemStack(material);
		if (rarities.get(rarity).material != null) {
			item = new ItemStack(rarities.get(rarity).material);
		}
		
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
		if (main.rarities.get(rarity).isEnchanted) {
			meta.addEnchant(Enchantment.LUCK, 1, true);
		}
		for (Enchant enchant : requiredEnchants) {
			int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
			meta.addEnchant(enchant.enchantment, lv, true);
		}
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		// Add optional enchantments
		if (enchantmentMax > 0) {
			int optEnchantNum = enchantmentMin + main.gen.nextInt(enchantmentMax - enchantmentMin);
			ArrayList<Enchant> optEnchants = new ArrayList<Enchant>();
			optEnchants.addAll(optionalEnchants);
			if (optEnchants.size() > 0) {
				for (int i = 0; i < optEnchantNum; i++) {
					Enchant enchant = optEnchants.get(main.gen.nextInt(optEnchants.size()));
					int lv = enchant.min + main.gen.nextInt(enchant.max - enchant.min + 1);
					meta.addEnchant(enchant.enchantment, lv, true);
					optEnchants.remove(enchant);
				}
			}
		}
		
		// Attributes
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
		
		// Slots and augments
		int maxSlots = slotsMax;
		if (rarities.get(rarity).slotsMax != -1) {
			maxSlots = rarities.get(rarity).slotsMax;
		}
		int currentSlot = 0;
		int numSlots = startingSlotsBase;
		if (rarities.get(rarity).startingSlotsBase != -1) {
			numSlots = rarities.get(rarity).startingSlotsBase;
		}
		int slotsRange = startingSlotsRange;
		if (rarities.get(rarity).startingSlotsRange != -1) {
			slotsRange = rarities.get(rarity).startingSlotsRange;
		}
		if (slotsRange > 0) {
			numSlots += main.gen.nextInt(slotsRange);
		}
		
		// Lore part 1
		lore.add(translateHexCodes("&7Title: " + this.title));
		lore.add("�7Type: " + this.display);
		lore.add("�7Rarity: " + main.rarities.get(rarity).displayName);
		lore.add("�7Level: " + level);
		lore.add("�8�m-----");
		// Lore part 2
		if (strength > 0) { lore.add("�9Strength +" + (strength - (strength % attributes.strRounded))); }
		if (dexterity > 0) { lore.add("�9Dexterity +" + (dexterity - (dexterity % attributes.dexRounded))); }
		if (intelligence > 0) { lore.add("�9Intelligence +" + (intelligence - (intelligence % attributes.intRounded))); }
		if (spirit > 0) { lore.add("�9Spirit +" + (spirit - (spirit % attributes.strRounded))); }
		if (perception > 0) { lore.add("�9Perception +" + (perception - (perception % attributes.prcRounded))); }
		if (endurance > 0) { lore.add("�9Endurance +" + (endurance - (endurance % attributes.endRounded))); }
		if (vitality > 0) { lore.add("�9Vitality +" + (vitality - (vitality % attributes.vitRounded))); }
		if (regeneration > 0) { lore.add("�9Regen +" + (regeneration - (regeneration % attributes.rgnRounded))); }
		// Lore part 3, only add separator if there was at least 1 attribute
		HashMap<String, Integer> nbtIntegers = new HashMap<String, Integer>();
		HashMap<String, String> nbtStrings = new HashMap<String, String>();
		if (lore.size() >= 6) { lore.add("�8�m-----"); }
		for (String augment : requiredAugments) {
			currentSlot++;
			String args[] = augment.split(":");
			lore.add(translateHexCodes(args[0]));
			nbtIntegers.put("slot" + currentSlot + "Line", lore.size());
			nbtIntegers.put("slot" + currentSlot + "Level", Integer.parseInt(args[2]));
			nbtStrings.put("slot" + currentSlot + "Augment", args[1]);
		}
		for (int i = 0; i < numSlots; i++) {
			currentSlot++;
			lore.add("�8[Empty Slot]");
			nbtIntegers.put("slot" + currentSlot + "Line", lore.size());
		}
		
		int durability = duraBase + rarities.get(rarity).duraBonus;
		lore.add("�7Durability " + durability + " / " + durability);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		double price = level * main.rarities.get(rarity).priceModifier;
		nbti.setDouble("value", price);
		nbti.setString("gear", name);
		nbti.setInteger("version", 1);
		nbti.setInteger("slotsMax", maxSlots);
		nbti.setInteger("level", level);
		nbti.setString("rarity", rarity.toLowerCase());
		nbti.setInteger("slotsCreated", numSlots);
		for (String key : nbtIntegers.keySet()) {
			nbti.setInteger(key, nbtIntegers.get(key));
		}
		for (String key : nbtStrings.keySet()) {
			nbti.setString(key, nbtStrings.get(key));
		}
		return nbti.getItem();
	}
	
	private String translateHexCodes(String textToTranslate) {
		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}
}
