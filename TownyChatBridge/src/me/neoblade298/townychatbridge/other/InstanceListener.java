package me.neoblade298.townychatbridge.other;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.bungee.PluginMessageEvent;
import me.neoblade298.townychatbridge.TownyChatBridge;

public class InstanceListener implements Listener {
	private HashMap<UUID, UUID> playerToTown = new HashMap<UUID, UUID>();
	private HashMap<UUID, UUID> townToNation = new HashMap<UUID, UUID>();
	private HashMap<UUID, HashSet<Player>> onlinePlayers = new HashMap<UUID, HashSet<Player>>();
	
	public void initialize() {
		new BukkitRunnable() {
			public void run() {
				try {
					ResultSet rs = NeoCore.getStatement().executeQuery("SELECT b.uuid, c.uuid FROM MLMC.TOWNY_TOWNS as b,"
							+ " MLMC.TOWNY_NATIONS as c WHERE b.nation = c.name;");
					while (rs.next()) {
						UUID tuuid = UUID.fromString(rs.getString(2));
						UUID nuuid = UUID.fromString(rs.getString(1));
						townToNation.put(tuuid, nuuid);
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		UUID town = UUID.fromString(e.getMessages().get(1));
		if (onlinePlayers.containsKey(town)) {
			for (Player p : onlinePlayers.get(town)) {
				p.sendMessage(e.getMessages().get(0));
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				try {
					ResultSet rs = NeoCore.getStatement().executeQuery("SELECT b.uuid, c.uuid FROM MLMC.TOWNY_RESIDENTS as a, MLMC.TOWNY_TOWNS as b, "
							+ "MLMC.TOWNY_NATIONS as c WHERE a.town = b.name AND b.nation = c.name AND a.name = '" + p.getName() + "';");
					
					if (!rs.next()) return;
					String townuuid = rs.getString(1);
					
					// Add town
					if (townuuid.isEmpty()) return;
					UUID tuuid = UUID.fromString(townuuid);
					HashSet<Player> players = onlinePlayers.getOrDefault(tuuid, new HashSet<Player>());
					players.add(p);
					onlinePlayers.putIfAbsent(tuuid, players);
					String nationuuid = rs.getString(2);
					
					// Add nation
					if (!nationuuid.isEmpty()) return;
					UUID nuuid = UUID.fromString(townuuid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.runTaskAsynchronously(TownyChatBridge.inst());
	}
}
