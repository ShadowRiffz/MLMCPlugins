package me.neoblade298.neosessions.sessions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.instancing.InstanceType;

public class SessionManager implements Listener {
	private static HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	private static HashMap<String, Session> sessions = new HashMap<String, Session>();
	private static HashMap<String, SessionInfo> info = new HashMap<String, SessionInfo>();
	private static boolean isSession = NeoCore.getInstanceType() == InstanceType.OTHER;
	
	public SessionManager() {
		
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

					// Connect
					try {
						Statement stmt = NeoCore.getStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM neosessions_players WHERE uuid = '" + uuid + "';");
						if (!rs.next()) {
							p.teleport(instanceSpawn);
							return;
						}
						
						String sessionKey = rs.getString(2);
						SessionPlayer sp = new SessionPlayer(p, sessionKey);
						SessionInfo si = info.get(sessionKey);
						
						rs = stmt.executeQuery("SELECT * FROM neosessions_sessions WHERE `key` = '" + sessionKey +
								"' AND instance = '" + NeoCore.getInstanceKey() + "';");
						if (!rs.next()) {
							p.teleport(instanceSpawn);
							return;
						}
						String from = rs.getString("from");
						int numPlayers = rs.getInt("numplayers");
						int multiplier = rs.getInt("multiplier");

						// Check if session exists, if not, create it
						Session s = sessions.getOrDefault(sessionKey, info.get(sessionKey).createSession(from, numPlayers, multiplier));
						sessions.putIfAbsent(sessionKey, s);
						s.addPlayer(sp);
						p.teleport(si.getPlayerSpawn());

						// Set up databases
						Bukkit.getServer().getLogger()
								.info("[NeoSessions] " + p.getName() + " sent to session " + sessionKey + ".");

						// Recalculate everyone's health bars every time someone joins
						for (Player partyMember : activeFights.get(boss)) {
							ArrayList<String> healthList = new ArrayList<String>();
							healthbars.put(partyMember.getName(), healthList);
							for (Player bossFighter : activeFights.get(boss)) {
								if (!bossFighter.equals(partyMember)) {
									healthList.add(bossFighter.getName());
								}
							}
							Collections.sort(healthList);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						joiningPlayers.remove(p.getName());
					}
				}
			}
		}
	}
}
