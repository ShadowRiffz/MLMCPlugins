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
	private static HashMap<Player, SessionPlayer> players;
	private static HashMap<String, Session> sessions;
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

						// Check if session exists, if not, create it
						if (sessions.containsKey(sessionKey)) {
							
						}
						Boss b = bossInfo.get(boss);
						p.teleport(b.getCoords());

						// Set up databases
						Bukkit.getServer().getLogger()
								.info("[NeoBossInstances] " + p.getName() + " sent to boss " + boss + ".");
						bossMultiplier.put(boss, multiplier);
						if (!activeFights.containsKey(boss)) {
							ArrayList<Player> activeFightsPlayers = new ArrayList<Player>();
							ArrayList<Player> inBossPlayers = new ArrayList<Player>();
							activeFightsPlayers.add(p);
							inBossPlayers.add(p);
							activeFights.put(boss, activeFightsPlayers);
							inBoss.put(boss, inBossPlayers);
						}
						else {
							activeFights.get(boss).add(p);
							inBoss.get(boss).add(p);
						}
						fightingBoss.put(p.getUniqueId(), boss);

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
