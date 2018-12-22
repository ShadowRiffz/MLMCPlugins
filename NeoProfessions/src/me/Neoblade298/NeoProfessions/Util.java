package me.Neoblade298.NeoProfessions;

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
}
