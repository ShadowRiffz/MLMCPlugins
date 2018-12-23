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
		String tier = null;
		
		// Find the tier string
		for(String line : lore) {
			if(line.contains("Tier:")) {
				tier = line;
				break;
			}
		}
		
		if (tier != null) {
			if(tier.contains("Common") ||
					tier.contains("Uncommon") ||
					tier.contains("Rare")) {
				return 1;
			}
			else if(tier.contains("Unique") ||
					tier.contains("Epic")) {
				return 2;
			}
			else if(tier.contains("Angelic")) {
				return 3;
			}
			else if(tier.contains("Mythic")) {
				return 4;
			}
			else if(tier.contains("Legendary")) {
				return 5;
			}
		}
		return -1;
	}
}
