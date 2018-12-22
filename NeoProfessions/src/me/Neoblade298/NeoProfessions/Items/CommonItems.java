package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonItems {
	
	public static ItemStack getEssence(int level, int amount) {
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
		lore.add("§7Level " + level);
		lore.add("§7Item used for profession crafting");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		return item;
	}
}
