package me.Neoblade298.NeoProfessions.Items;


import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class StonecutterItems {
	
	// Constants
	final int WEAPON_GEM_STR_BASE = 4;
	final int WEAPON_GEM_STR_PER_LVL = 2;
	final int ARMOR_GEM_STR_PER_LVL = 1;
	final int ARMOR_GEM_STR_BASE = 2;
	final int ARMOR_GEM_VIT_PER_LVL = 7;
	final int ARMOR_GEM_VIT_BASE = 0;
	final int ARMOR_GEM_END_PER_LVL = 1;
	final int ARMOR_GEM_END_BASE = 2;
	final int OL_WEAPON_GEM_STR_RANGE = 3;
	final int OL_ARMOR_GEM_STR_RANGE = 2;
	final int OL_ARMOR_GEM_STR_BASE = 1;
	final int OL_ARMOR_GEM_STR_PER_LVL = 2;
	final int OL_ARMOR_GEM_VIT_RANGE = 15;
	final int OL_ARMOR_GEM_VIT_BASE = 20; // set to 200 max or something
	final int OL_ARMOR_GEM_VIT_PER_LVL = 15;
	final int OL_ARMOR_GEM_END_RANGE = 3;
	final int OL_ARMOR_GEM_END_BASE = 1;
	final int OL_ARMOR_GEM_END_PER_LVL = 3;
	final int OL_WEAPON_DURABILITY_BASE = 400;
	final int OL_ARMOR_DURABILITY_BASE = 200;
	
	Random gen = new Random();
	
	public ItemStack getOre(String type, int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		Builder fe = FireworkEffect.builder();
		String oreName = null;
		type = type.toLowerCase();
		switch (type) {
			case "strength":
				oreName = "Ruby";
				fe.withColor(Color.BLACK, Color.RED);
				break;
			case "dexterity":
				oreName = "Amethyst";
				fe.withColor(Color.GRAY, Color.PURPLE);
				break;
			case "intelligence":	
				oreName = "Sapphire";
				fe.withColor(Color.BLUE);
				break;
			case "spirit":
				oreName = "Emerald";
				fe.withColor(Color.LIME, Color.LIME, Color.GREEN);
				break;
			case "perception":
				oreName = "Topaz";
				fe.withColor(Color.YELLOW, Color.ORANGE);
				break;
			case "vitality":
				oreName = "Garnet";
				fe.withColor(Color.GRAY, Color.BLACK);
				break;
			case "endurance":
				oreName = "Adamantium";
				fe.withColor(Color.BLACK, Color.RED);
				break;
		}
		ArrayList<String> lore = new ArrayList<String>();
		meta.setDisplayName("§4[Lv " + level + "] §c" + oreName + " Ore");
		lore.add("§7Level " + level + " " + StringUtils.capitalize(type) + " Ore");
		lore.add("§7Item used for profession crafting");
		meta.setEffect(fe.build());
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getOre(int random, int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		Builder fe = FireworkEffect.builder();
		String oreName = null;
		String type = null;
		switch (random) {
			case 1:
				oreName = "Ruby";
				type = "Strength";
				fe.withColor(Color.BLACK, Color.RED);
				break;
			case 2:
				oreName = "Amethyst";
				type = "Dexterity";
				fe.withColor(Color.GRAY, Color.PURPLE);
				break;
			case 3:	
				oreName = "Sapphire";
				type = "Intelligence";
				fe.withColor(Color.BLUE);
				break;
			case 4:
				oreName = "Emerald";
				type = "Spirit";
				fe.withColor(Color.LIME, Color.LIME, Color.GREEN);
				break;
			case 5:
				oreName = "Topaz";
				type = "Perception";
				fe.withColor(Color.YELLOW, Color.ORANGE);
				break;
			case 6:
				oreName = "Garnet";
				type = "Vitality";
				fe.withColor(Color.GRAY, Color.BLACK);
				break;
			case 7:
				oreName = "Adamantium";
				type = "Endurance";
				fe.withColor(Color.BLACK, Color.RED);
				break;
			default:
				oreName = "Adamantium";
				type = "Endurance";
				fe.withColor(Color.BLACK, Color.RED);
				break;
		}
		ArrayList<String> lore = new ArrayList<String>();
		meta.setDisplayName("§4[Lv " + level + "] §c" + oreName + " Ore");
		lore.add("§7Level " + level + " " + type + " Ore");
		lore.add("§7Item used for profession crafting");
		meta.setEffect(fe.build());
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getWeaponGem(String attr, int level, boolean isOverloaded) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		if(isOverloaded) {
			item = new ItemStack(Material.ENDER_EYE);
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int potency = 0;
		int duraLost = 0;
		int lvlDivided = level / 5;
		if(isOverloaded) {
			potency = gen.nextInt(OL_WEAPON_GEM_STR_RANGE) + 1 + (OL_WEAPON_GEM_STR_RANGE * (lvlDivided - 1));
			duraLost = OL_WEAPON_DURABILITY_BASE;
		}
		else {
			potency = WEAPON_GEM_STR_BASE + (WEAPON_GEM_STR_PER_LVL * (lvlDivided - 1));
		}
		switch (attr) {
		case "strength":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
			break;
		case "dexterity":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
			break;
		case "intelligence":	
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
			break;
		case "spirit":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
			break;
		case "perception":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
			break;
		}
		if(isOverloaded) {
			lore.add("§7Level " + level + " Overloaded Gem Augment");
		}
		else {
			lore.add("§7Level " + level + " Gem Augment");
		}
		String effect = "§7Effect: Increases weapon " + attr;
		lore.add(effect);
		lore.add("§7Potency: §e" + potency);
		if(isOverloaded) {
			lore.add("§7Durability Lost: §e" + duraLost);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getWeaponGem(String attr, int level, boolean isOverloaded, int potency, int durabilityLoss) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		if(isOverloaded) {
			item = new ItemStack(Material.ENDER_EYE);
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		switch (attr) {
		case "strength":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
			break;
		case "dexterity":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
			break;
		case "intelligence":	
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
			break;
		case "spirit":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
			break;
		case "perception":
			meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
			break;
		}
		if(isOverloaded) {
			lore.add("§7Level " + level + " Overloaded Gem Augment");
		}
		else {
			lore.add("§7Level " + level + " Gem Augment");
		}
		String effect = "§7Effect: Increases weapon " + attr;
		if(isOverloaded) {
			effect += ", lowers durability";
		}
		lore.add(effect);
		lore.add("§7Potency: §e" + potency);
		if(isOverloaded) {
			lore.add("§7Durability Lost: §e" + durabilityLoss);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getArmorGem(String attr, int level, boolean isOverloaded) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		if(isOverloaded) {
			item = new ItemStack(Material.ENDER_EYE);
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int potency = 0;
		int duraLost = 0;
		int lvlDivided = level / 10;
		if(isOverloaded) {
			duraLost = OL_ARMOR_DURABILITY_BASE;
			switch (attr) {
			case "strength":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
				potency = gen.nextInt(OL_ARMOR_GEM_STR_RANGE) + OL_ARMOR_GEM_STR_BASE + (OL_ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1));
				break;
			case "dexterity":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
				potency = gen.nextInt(OL_ARMOR_GEM_STR_RANGE) + OL_ARMOR_GEM_STR_BASE + (OL_ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1));
				break;
			case "intelligence":	
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
				potency = gen.nextInt(OL_ARMOR_GEM_STR_RANGE) + OL_ARMOR_GEM_STR_BASE + (OL_ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1));
				break;
			case "spirit":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
				potency = gen.nextInt(OL_ARMOR_GEM_STR_RANGE) + OL_ARMOR_GEM_STR_BASE + (OL_ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1));
				break;
			case "perception":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
				potency = gen.nextInt(OL_ARMOR_GEM_STR_RANGE) + OL_ARMOR_GEM_STR_BASE + (OL_ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1));
				break;
			case "vitality":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Garnet");
				potency = gen.nextInt(OL_ARMOR_GEM_VIT_RANGE) + OL_ARMOR_GEM_VIT_BASE + (OL_ARMOR_GEM_VIT_PER_LVL * (lvlDivided - 1));
				break;
			case "endurance":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Adamantium");
				potency = gen.nextInt(OL_ARMOR_GEM_END_RANGE) + OL_ARMOR_GEM_END_BASE + (OL_ARMOR_GEM_END_PER_LVL * (lvlDivided - 1));
				break;
			}
		}
		else {
			switch (attr) {
			case "strength":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
				potency = (ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_STR_BASE;
				break;
			case "dexterity":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
				potency = (ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_STR_BASE;
				break;
			case "intelligence":	
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
				potency = (ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_STR_BASE;
				break;
			case "spirit":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
				potency = (ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_STR_BASE;
				break;
			case "perception":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
				potency = (ARMOR_GEM_STR_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_STR_BASE;
				break;
			case "vitality":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Garnet");
				potency = gen.nextInt(ARMOR_GEM_VIT_BASE) + 1 + (ARMOR_GEM_VIT_PER_LVL * (lvlDivided - 1));
				break;
			case "endurance":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Adamantium");
				potency = (ARMOR_GEM_END_PER_LVL * (lvlDivided - 1)) + ARMOR_GEM_END_BASE;
				break;
			}
		}
		if(isOverloaded) {
			lore.add("§7Level " + level + " Overloaded Gem Augment");
		}
		else {
			lore.add("§7Level " + level + " Gem Augment");
		}
		String effect = "§7Effect: Increases armor " + attr;
		lore.add(effect);
		lore.add("§7Potency: §e" + potency);
		if(isOverloaded) {
			lore.add("§7Durability Lost: §e" + duraLost);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getArmorGem(String attr, int level, boolean isOverloaded, int potency, int durabilityLoss) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		if(isOverloaded) {
			item = new ItemStack(Material.ENDER_EYE);
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		if(isOverloaded) {
			switch (attr) {
			case "strength":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
				break;
			case "dexterity":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
				break;
			case "intelligence":	
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
				break;
			case "spirit":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
				break;
			case "perception":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
				break;
			case "vitality":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Garnet");
				break;
			case "endurance":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Adamantium");
				break;
			}
		}
		else {
			switch (attr) {
			case "strength":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Ruby");
				break;
			case "dexterity":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Amethyst");
				break;
			case "intelligence":	
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Sapphire");
				break;
			case "spirit":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Emerald");
				break;
			case "perception":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Topaz");
				break;
			case "vitality":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Garnet");
				break;
			case "endurance":
				meta.setDisplayName("§4[Lv " + level + "] §cRefined Adamantium");
				break;
			}
		}
		if(isOverloaded) {
			lore.add("§7Level " + level + " Overloaded Gem Augment");
		}
		else {
			lore.add("§7Level " + level + " Gem Augment");
		}
		String effect = "§7Effect: Increases armor " + attr;
		lore.add(effect);
		lore.add("§7Potency: §e" + potency);
		if(isOverloaded) {
			lore.add("§7Durability Lost: §e" + durabilityLoss);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
