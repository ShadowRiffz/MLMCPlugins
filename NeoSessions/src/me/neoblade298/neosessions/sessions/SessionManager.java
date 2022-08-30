package me.neoblade298.neosessions.sessions;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.instancing.InstanceType;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neosessions.NeoSessions;

// Only loaded in session hosts, NOT from sender hosts
public class SessionManager implements Listener {
	private static HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	private static HashMap<String, Session> sessions = new HashMap<String, Session>();
	private static HashMap<String, SessionInfo> info = new HashMap<String, SessionInfo>();
	private static boolean isSessionHost = NeoCore.getInstanceType() == InstanceType.SESSIONS;
	
	private static Location spawn;
	
	public SessionManager(ConfigurationSection cfg) {
		spawn = Util.stringToLoc(cfg.getString("spawn"));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			public void run() {
				if (isSession) {
					Player p = e.getPlayer();
					UUID uuid = p.getUniqueId();

					if (p.isDead()) {
						p.spigot().respawn();
					}

					try {
						Statement stmt = NeoCore.getStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM neosessions_players WHERE uuid = '" + uuid + "';");
						if (!rs.next()) {
							p.teleport(spawn);
							Bukkit.getServer().getLogger().warning("[NeoSessions] Failed to send " + p.getName()
							+ " to session, no such session player exists in SQL.");
							return;
						}
						
						// Create session player
						String sessionKey = rs.getString(2);
						SessionPlayer sp = new SessionPlayer(p, sessionKey);
						SessionInfo si = info.get(sessionKey);
						players.put(p.getUniqueId(), sp);
						
						// Find session, create it if it doesn't exist
						Session s = null;
						if (!sessions.containsKey(sessionKey)) {
							rs = stmt.executeQuery("SELECT * FROM neosessions_sessions WHERE `key` = '" + sessionKey +
									"' AND instance = '" + NeoCore.getInstanceKey() + "';");
							if (!rs.next()) {
								p.teleport(spawn);
								Bukkit.getServer().getLogger().warning("[NeoSessions] Failed to send " + p.getName()
										+ " to session " + sessionKey + ", no such session exists in SQL.");
								return;
							}
							String from = rs.getString("from");
							int numPlayers = rs.getInt("numplayers");
							int multiplier = rs.getInt("multiplier");
							s = info.get(sessionKey).createSession(from, numPlayers, multiplier);
							Bukkit.getServer().getLogger()
							.info("[NeoSessions] " + p.getName() + " created session " + sessionKey + " from " + from +
									" with " + numPlayers + " players and multiplier " + multiplier + ".");
						}
						
						// Add and send player to session
						s.addPlayer(sp);
						p.teleport(si.getPlayerSpawn());
						Bukkit.getServer().getLogger()
						.info("[NeoSessions] " + p.getName() + " sent to session " + sessionKey + ".");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}.runTaskAsynchronously(NeoSessions.inst());
	}
}
