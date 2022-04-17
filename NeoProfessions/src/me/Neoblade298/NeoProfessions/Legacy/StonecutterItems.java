package me.Neoblade298.NeoProfessions.Legacy;


import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StonecutterItems {
	
	// Constants
	
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
