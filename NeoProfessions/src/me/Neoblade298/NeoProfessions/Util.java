package me.Neoblade298.NeoProfessions;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {
	public static void sendMessage(Player p, String m) {
		String message = "&4[&c&lMLMC&4] " + m;
		p.sendMessage(message.replaceAll("&", "§"));
	}

	public static ItemStack setAmount(ItemStack item, int amount) {
		item.setAmount(amount);
		return item;
	}
	
	public static int getItemLevel(ItemStack item) {
		ArrayList<String> lore  = (ArrayList<String>) item.getItemMeta().getLore();
		if (lore.contains("Tier:")) {
			if(lore.contains("Common") ||
					lore.contains("Uncommon") ||
					lore.contains("Rare")) {
				return 1;
			}
			else if(lore.contains("Unique") ||
					lore.contains("Epic")) {
				return 2;
			}
			else if(lore.contains("Angelic")) {
				return 3;
			}
			else if(lore.contains("Mythic")) {
				return 4;
			}
			else if(lore.contains("Legendary")) {
				return 5;
			}
		}
		return -1;
	}
}
