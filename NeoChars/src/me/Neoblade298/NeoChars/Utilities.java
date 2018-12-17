package me.Neoblade298.NeoChars;

import org.bukkit.entity.Player;

public class Utilities {
	public static void sendMessage(Player p, String m) {
		p.sendMessage(m.replaceAll("&", "§"));
	}
}
