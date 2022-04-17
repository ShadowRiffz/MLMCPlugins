package me.Neoblade298.NeoProfessions.Legacy;

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
		level = roundUp ? level + ((level % 5 == 0 ? 0 : 5) - level % 5) : level - (level % 5);
		meta.setDisplayName("§4[Lv " + level + "] §cEssence");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " Essence§0");
		lore.add("§7Item used for profession crafting");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}
}
