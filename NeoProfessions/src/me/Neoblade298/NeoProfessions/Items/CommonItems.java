package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonItems {
	
	public ItemStack getEssence(int level, boolean roundUp) {
		ItemStack item = new ItemStack(Material.QUARTZ);
		ItemMeta meta = item.getItemMeta();
		level = roundUp ? level + (10 - level % 10) : level - (level % 10);
		switch (level) {
		case 10:	meta.setDisplayName("§4[Lv " + level + "] §cFractured Essence");
					break;
		case 20:	meta.setDisplayName("§4[Lv " + level + "] §cDull Essence");
						break;
		case 30:	meta.setDisplayName("§4[Lv " + level + "] §cMinor Essence");
						break;
		case 40:	meta.setDisplayName("§4[Lv " + level + "] §cPotent Essence");
						break;
		case 50:	meta.setDisplayName("§4[Lv " + level + "] §cSublime Essence");
						break;
		case 60:	meta.setDisplayName("§4[Lv " + level + "] §cPure Essence");
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
}
