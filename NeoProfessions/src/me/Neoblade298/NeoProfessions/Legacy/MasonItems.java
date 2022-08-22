package me.Neoblade298.NeoProfessions.Legacy;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MasonItems {
	public ItemStack getExpCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 30;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 40] §cAdvanced Exp Charm");
			level = 40;
		}
		else {
			meta.setDisplayName("§4[Lv 30] §cExp Charm");
		}
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getLootingCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 30;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 40] §cAdvanced Looting Charm");
			level = 40;
		}
		else {
			meta.setDisplayName("§4[Lv 30] §cLooting Charm");
		}
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getDropCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 40;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 50] §cAdvanced Drop Charm");
			level = 50;
		}
		else {
			meta.setDisplayName("§4[Lv 40] §cDrop Charm");
		}
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getTravelerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 20;
		meta.setDisplayName("§4[Lv 20] §cTraveler's Charm");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getRecoveryCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 30;
		meta.setDisplayName("§4[Lv 30] §cRecovery Charm");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getHungerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 50;
		meta.setDisplayName("§4[Lv 50] §cHunger Charm");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getSecondChanceCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 40;
		meta.setDisplayName("§4[Lv 40] §cSecond Chance Charm");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getQuickEatCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		int level = 40;
		meta.setDisplayName("§4[Lv 40] §cQuick Eat Charm");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
