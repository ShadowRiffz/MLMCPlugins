package me.Neoblade298.NeoProfessions.Utilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class BlacksmithUtils {

	
	public static boolean isRepairItem(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		for(String line : meta.getLore()) {
			if(line.contains("Restores durability of an item")) {
				return true;
			}
		}
		return false;
	}
	
	public static int getItemLevel(ItemStack item) {
		return item.getEnchantments().get(Enchantment.DURABILITY);
	}
	
	public static int getItemPotency(ItemStack item) {
		for(String line : item.getItemMeta().getLore()) {
			if(line.contains("Potency")) {
				line = ChatColor.stripColor(line);
				return Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.indexOf("%")));
			}
		}
		return -1;
	}
	
	public static boolean canRepair(ItemStack item) {
		// Check if the item is a quest item
		System.out.println(item.getType().getMaxDurability());
		if(Util.getItemLevel(item) != -1) {
			return true;
		}
		if(item.getType().getMaxDurability() > 0) {
			return true;
		}
		return false;
	}
}
