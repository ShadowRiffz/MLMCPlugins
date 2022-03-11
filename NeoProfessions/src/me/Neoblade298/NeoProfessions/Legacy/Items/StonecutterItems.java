package me.Neoblade298.NeoProfessions.Legacy.Items;


import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StonecutterItems {
	
	// Constants
	final int WEAPON_GEM_STR_BASE = 4;
	final int WEAPON_GEM_STR_PER_LVL = 2;
	final int ARMOR_GEM_STR_PER_LVL = 1;
	final int ARMOR_GEM_STR_BASE = 2;
	final int ARMOR_GEM_VIT_PER_LVL = 7;
	final int ARMOR_GEM_VIT_BASE = 1;
	final int ARMOR_GEM_END_PER_LVL = 4;
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
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getOreSolidify(String type, int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getOre(int random, int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getWeaponGem(String attr, int level, boolean isOverloaded) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getWeaponGem(String attr, int level, boolean isOverloaded, int potency) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getArmorGem(String attr, int level, boolean isOverloaded, int potency) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
