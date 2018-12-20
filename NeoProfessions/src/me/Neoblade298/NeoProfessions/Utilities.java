package me.Neoblade298.NeoProfessions;

import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;

public class Utilities {
	public static void sendMessage(Player p, String m) {
		p.sendMessage(m.replaceAll("&", "§"));
	}
	
	public static void createItem(Player p, String profession, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + itemtype + "." + level)) {
				p.getInventory().addItem(BlacksmithItems.getDurabilityItem(level, itemtype));
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
	
	public static void createItem(Player p, String profession, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + level)) {
				// Give max durability shard from mythicmobs
				// mythicmobs items give -s item + level
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
	
	public static void createItem(Player p, String profession, String item, String type) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + type)) {
				// Give charm
				// mythicmobs items give -s item + type
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
}
