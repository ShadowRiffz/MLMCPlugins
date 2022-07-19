package me.neoblade298.neocore.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
		if (p == null) {
			Bukkit.getLogger().warning("[NeoCore] Could not send message due to no online players: " + msg);
			return;
		}
		p.sendPluginMessage(NeoCore.inst(), "BungeeCord", out.toByteArray());
	}
	
	public static void sendPlayer(Player p, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(NeoCore.inst(), "BungeeCord", out.toByteArray());
	}
	
	public static void sendPluginMessage(String channel, Object... msgs) {
		sendPluginMessage("ALL", Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channel, msgs);
	}
	
	public static void sendPluginMessage(String server, String channel, Object... msgs) {
		sendPluginMessage(server, Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channel, msgs);
	}
	
	public static void sendPluginMessage(Player p, String channel, Object... msgs) {
		sendPluginMessage("ALL", p, channel, msgs);
	}
	
	public static void sendPluginMessage(String server, Player p, String channel, Object... msgs) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF(server);
		out.writeUTF(channel);

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			for (Object msg : msgs) {
				if (msg instanceof String) {
					msgout.writeUTF((String) msg);
				}
				else if (msg instanceof Integer) {
					msgout.writeInt((Integer) msg);
				}
				else if (msg instanceof Double) {
					msgout.writeDouble((Double) msg);
				}
				else if (msg instanceof Boolean) {
					msgout.writeBoolean((Boolean) msg);
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());
		if (p == null) {
			Bukkit.getLogger().warning("[NeoCore] Could not send pluginmsg in channel " + channel + " due to no online players:");
			for (Object msg : msgs) {
				Bukkit.getLogger().warning("[NeoCore] - " + msg);
			}
			return;
		}
		p.sendPluginMessage(NeoCore.inst(), "BungeeCord", out.toByteArray());
	}
}
