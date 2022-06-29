package me.neoblade298.neocore.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.Util;

public class BungeeAPI {
	public static void broadcast(String msg) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF("ALL");
		out.writeUTF(Util.translateColors(msg));

		Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		p.sendPluginMessage(NeoCore.inst(), "BungeeCord", out.toByteArray());
	}
	
	public static void sendPlayer(Player p, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(NeoCore.inst(), "BungeeCord", out.toByteArray());
	}
}
