package me.neoblade298.townychatbridge.other;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.chatcontrol.api.ChatChannelEvent;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neocore.bungee.PluginMessageEvent;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.townychatbridge.TownyChatBridge;

public class InstanceListener implements Listener {
	private HashSet<UUID> towns = new HashSet<UUID>();
	private HashMap<UUID, UUID> townToNation = new HashMap<UUID, UUID>(); // Key town uuid, value nation uuid
	private HashMap<UUID, HashSet<UUID>> nationToTowns = new HashMap<UUID, HashSet<UUID>>();
	private HashMap<UUID, HashSet<Player>> onlinePlayers = new HashMap<UUID, HashSet<Player>>(); // Key town uuid, value player
	private HashMap<Player, UUID> playerToTown = new HashMap<Player, UUID>();

	public InstanceListener() {
		new BukkitRunnable() {
			public void run() {
				try {
					ResultSet rs = NeoCore.getStatement()
							.executeQuery("SELECT b.uuid, c.uuid FROM MLMC.TOWNY_TOWNS as b,"
									+ " MLMC.TOWNY_NATIONS as c WHERE b.nation = c.name;");
					while (rs.next()) {
						UUID tuuid = UUID.fromString(rs.getString(2));
						towns.add(tuuid);
						if (!rs.getString(1).isEmpty()) {
							UUID nuuid = UUID.fromString(rs.getString(1));
							townToNation.put(tuuid, nuuid);
							HashSet<UUID> towns = nationToTowns.getOrDefault(nuuid, new HashSet<UUID>());
							towns.add(tuuid);
							nationToTowns.putIfAbsent(nuuid, towns);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(TownyChatBridge.inst());
	}

	@EventHandler
	public void onTownChat(ChatChannelEvent e) {
		if (!(e.getSender() instanceof Player)) return;
		if (!e.getChannel().getName().equalsIgnoreCase("town")) return;
		Player p = (Player) e.getSender();
		if (!playerToTown.containsKey(p)) return;
		
		BungeeAPI.sendPluginMessage(p, "townchatin", e.getMessage(), playerToTown.get(p).toString());
	}

	@EventHandler
	public void onNationChat(ChatChannelEvent e) {
		if (!(e.getSender() instanceof Player)) return;
		if (!e.getChannel().getName().equalsIgnoreCase("nation")) return;
		Player p = (Player) e.getSender();
		if (!playerToTown.containsKey(p)) return;
		
		BungeeAPI.sendPluginMessage(p, "nationchatin", e.getMessage(), playerToTown.get(p).toString());
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		switch (e.getChannel()) {
		case "townchatout":
			handleTownChat(e);
			break;
		case "nationchatout":
			handleNationChat(e);
			break;
		case "townyevent":
			handleTownyEvent(e);
			break;
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				try {
					ResultSet rs = NeoCore.getStatement().executeQuery(
							"SELECT b.uuid FROM MLMC.TOWNY_RESIDENTS as a, MLMC.TOWNY_TOWNS as b "
									+ "WHERE a.town = b.name AND a.name = '"
									+ p.getName() + "';");

					if (!rs.next()) return;
					String townuuid = rs.getString(1);

					// Add to online players
					if (townuuid.isEmpty()) return;
					UUID tuuid = UUID.fromString(townuuid);
					HashSet<Player> players = onlinePlayers.getOrDefault(tuuid, new HashSet<Player>());
					players.add(p);
					onlinePlayers.putIfAbsent(tuuid, players);
					playerToTown.put(p, tuuid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.runTaskAsynchronously(TownyChatBridge.inst());
	}

	private void handleTownChat(PluginMessageEvent e) {
		UUID town = UUID.fromString(e.getMessages().get(1));
		if (onlinePlayers.containsKey(town)) {
			for (Player p : onlinePlayers.get(town)) {
				Util.msg(p, e.getMessages().get(0));
			}
		}
	}
	
	private void handleNationChat(PluginMessageEvent e) {
		UUID town = UUID.fromString(e.getMessages().get(1));
		if (onlinePlayers.containsKey(town)) {
			UUID nation = townToNation.get(town);
			for (UUID townInNation : nationToTowns.get(nation)) {
				for (Player p : onlinePlayers.get(townInNation)) {
					Util.msg(p, e.getMessages().get(0));
				}
			}
		}
	}

	private void handleTownyEvent(PluginMessageEvent e) {
		String type = e.getMessages().get(0);
		ArrayList<String> msgs = e.getMessages();
		switch (type) {
		case "newtown": towns.add(UUID.fromString(e.getMessages().get(1)));
		break;
		case "newnation": townToNation.put(UUID.fromString(msgs.get(1)), UUID.fromString(msgs.get(2)));
		break;
		case "townjoinnation": townToNation.put(UUID.fromString(msgs.get(1)), UUID.fromString(msgs.get(2)));
		break;
		case "townleavenation": townToNation.remove(UUID.fromString(msgs.get(1)));
		break;
		}
	}
}
