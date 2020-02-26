package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MasonItems {
	public ItemStack getExpCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 30;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 40] §cAdvanced Exp Charm");
			lore.add("§7Level 40 Advanced Exp Charm");
			lore.add("§7Effect: Raises Exp gained by 2x");
			level = 40;
		}
		else {
			meta.setDisplayName("§4[Lv 30] §cExp Charm");
			lore.add("§7Level 30 Exp Charm");
			lore.add("§7Effect: Raises Exp gained by 1.5x");
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getLootingCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 30;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 40] §cAdvanced Looting Charm");
			lore.add("§7Level 40 Advanced Looting Charm");
			lore.add("§7Effect: Quest mobs drop more gold");
			level = 40;
		}
		else {
			meta.setDisplayName("§4[Lv 30] §cLooting Charm");
			lore.add("§7Level 30 Looting Charm");
			lore.add("§7Effect: Quest mobs drop some gold");
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getDropCharm(boolean isAdvanced) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 40;
		if(isAdvanced) {
			meta.setDisplayName("§4[Lv 50] §cAdvanced Drop Charm");
			lore.add("§7Level 50 Advanced Drop Charm");
			lore.add("§7Effect: Raises droprate by 1.5x");
			level = 50;
		}
		else {
			meta.setDisplayName("§4[Lv 40] §cDrop Charm");
			lore.add("§7Level 40 Drop Charm");
			lore.add("§7Effect: Raises droprate by 1.2x");
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getTravelerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 20;
		meta.setDisplayName("§4[Lv 20] §cTraveler's Charm");
		lore.add("§7Level 20 Traveler Charm");
		lore.add("§7Effect: Move faster out of combat");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getRecoveryCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 30;
		meta.setDisplayName("§4[Lv 30] §cRecovery Charm");
		lore.add("§7Level 30 Recovery Charm");
		lore.add("§7Effect: Recover out of combat");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getHungerCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 50;
		meta.setDisplayName("§4[Lv 50] §cHunger Charm");
		lore.add("§7Level 50 Hunger Charm");
		lore.add("§7Effect: Permanently stay at 9 hunger");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getSecondChanceCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 40;
		meta.setDisplayName("§4[Lv 40] §cSecond Chance Charm");
		lore.add("§7Level 40 Second Chance Charm");
		lore.add("§7Effect: Revives on death");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
	
	public ItemStack getQuickEatCharm() {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		int level = 40;
		meta.setDisplayName("§4[Lv 40] §cQuick Eat Charm");
		lore.add("§7Level 40 Quick Eat Charm");
		lore.add("§7Effect: Eat recipes without holding them");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
