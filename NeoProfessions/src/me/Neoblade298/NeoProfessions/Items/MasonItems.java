package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MasonItems {
	public static ItemStack getExpCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 2;
		if(isAdvanced) {
			meta.setDisplayName("&4[Lv 3] §cAdvanced EXP Charm");
			lore.add("§7Level 3 Advanced EXP Charm");
			lore.add("§7Effect: Raises EXP gained by 2x");
			level = 3;
		}
		else {
			meta.setDisplayName("§4[Lv 2] §cEXP Charm");
			lore.add("§7Level 2 EXP Charm");
			lore.add("§7Effect: Raises EXP gained by 1.5x");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getLootingCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 2;
		if(isAdvanced) {
			meta.setDisplayName("&4[Lv 3] §cAdvanced Looting Charm");
			lore.add("§7Level 3 Advanced Looting Charm");
			lore.add("§7Effect: Quest mobs drop some gold");
			level = 3;
		}
		else {
			meta.setDisplayName("§4[Lv 2] §cLooting Charm");
			lore.add("§7Level 2 Looting Charm");
			lore.add("§7Effect: Quest mobs drop more gold");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getDropCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 3;
		if(isAdvanced) {
			meta.setDisplayName("&4[Lv 4] §cAdvanced Drop Charm");
			lore.add("§7Level 3 Advanced Drop Charm");
			lore.add("§7Effect: Raises droprate by 1.5x");
			level = 4;
		}
		else {
			meta.setDisplayName("§4[Lv 3] §cDrop Charm");
			lore.add("§7Level 2 Drop Charm");
			lore.add("§7Effect: Raises droprate by 1.2x");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getTravelerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 1;
		meta.setDisplayName("§4[Lv 1] §cTraveler's Charm");
		lore.add("§7Level 1 Traveler Charm");
		lore.add("§7Effect: Move faster out of combat");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getRecoveryCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 2;
		meta.setDisplayName("§4[Lv 2] §cRecovery Charm");
		lore.add("§7Level 2 Recovery Charm");
		lore.add("§7Effect: Recover out of combat");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getHungerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 4;
		meta.setDisplayName("§4[Lv 4] §cHunger Charm");
		lore.add("§7Level 4 Hunger Charm");
		lore.add("§7Effect: Permanently stay at 9 hunger");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public static ItemStack getSecondChanceCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 4;
		meta.setDisplayName("§4[Lv 4] §cSecond Chance Charm");
		lore.add("§7Level 4 Secondary Chance Charm");
		lore.add("§7Effect: Revives on death");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
