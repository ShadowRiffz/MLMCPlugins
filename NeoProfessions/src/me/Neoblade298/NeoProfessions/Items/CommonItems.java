package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonItems {
	
	public static ItemStack getEssence(int level) {
		ItemStack item = new ItemStack(Material.QUARTZ);
		ItemMeta meta = item.getItemMeta();
		switch (level) {
		case 1:	meta.setDisplayName("§4[Lv " + level + "] §cDull Essence");
						break;
		case 2:	meta.setDisplayName("§4[Lv " + level + "] §cMinor Essence");
						break;
		case 3:	meta.setDisplayName("§4[Lv " + level + "] §cPotent Essence");
						break;
		case 4:	meta.setDisplayName("§4[Lv " + level + "] §cSublime Essence");
						break;
		case 5:	meta.setDisplayName("§4[Lv " + level + "] §cPure Essence");
						break;
		}
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " Essence");
		lore.add("§7Item used for profession crafting");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}
	
	public static ItemStack getEssenceFragment(int level) {
		ItemStack item = new ItemStack(Material.IRON_NUGGET);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		switch (level) {
		case 1:	meta.setDisplayName("§4[Lv " + level + "] §cDull Essence Fragment");
						lore.add("§7Level " + level + " Essence Fragment");
						lore.add("§7Combine 9 in a workbench for §cDull Essence");
						break;
		case 2:	meta.setDisplayName("§4[Lv " + level + "] §cMinor Essence Fragment");
						lore.add("§7Level " + level + " Essence Fragment");
						lore.add("§7Combine 9 in a workbench for §cMinor Essence");
						break;
		case 3:	meta.setDisplayName("§4[Lv " + level + "] §cPotent Essence Fragment");
						lore.add("§7Level " + level + " Essence Fragment");
						lore.add("§7Combine 9 in a workbench for §cPotent Essence");
						break;
		case 4:	meta.setDisplayName("§4[Lv " + level + "] §cSublime Essence Fragment");
						lore.add("§7Level " + level + " Essence Fragment");
						lore.add("§7Combine 9 in a workbench for §cSublime Essence");
						break;
		case 5:	meta.setDisplayName("§4[Lv " + level + "] §cPure Essence Fragment");
						lore.add("§7Level " + level + " Essence Fragment");
						lore.add("§7Combine 9 in a workbench for §cPure Essence");
						break;
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
