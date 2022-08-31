package me.neoblade298.neosessions.sessions;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerLoadCompleteEvent;
import com.sucy.skill.api.player.PlayerAccounts;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neosessions.NeoSessions;

// Only loaded in session hosts, NOT from sender hosts
public class SessionManager implements Listener {
	private static HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	private static HashMap<String, Session> sessions = new HashMap<String, Session>();
	private static HashMap<String, SessionInfo> info = new HashMap<String, SessionInfo>();
	
	private static Location spawn;
	
	public SessionManager(ConfigurationSection cfg) {
		spawn = Util.stringToLoc(cfg.getString("spawn"));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			public void run() {
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
		}.runTaskAsynchronously(NeoSessions.inst());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onLoad(PlayerLoadCompleteEvent e) {
		Player p = e.getPlayer();
		if (!players.containsKey(p.getUniqueId())) {
			Bukkit.getServer().getLogger().warning("[NeoSessions] Failed to run onLoad for " + p.getName()
			+ ", sessionplayer doesn't exist.");
			return;
		}
		SessionPlayer sp = players.get(p.getUniqueId());
		if (sp.getSession() == null) {
			Bukkit.getServer().getLogger().warning("[NeoSessions] Failed to run onLoad for " + p.getName()
			+ ", sessionplayer doesn't belong to a session.");
			return;
		}
		sp.setStatus(PlayerStatus.AWAITING_PLAYERS);
		Session s = sp.getSession();

		// If last one to load, start the session
		if (s.canStart()) {
			// Heal players
			new BukkitRunnable() {
				public void run() {
					for (SessionPlayer sp : s.getSessionPlayers().values()) {
						Player p = sp.getPlayer();
						// Second heal to be safe
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
						PlayerData pd = SkillAPI.getPlayerData(p);
						if (pd.getClass("class") != null && pd.getClass("class").getData().getManaName().contains("MP")) {
							pd.setMana(pd.getMaxMana());
						}
					}
				}
			}.runTaskLater(NeoSessions.inst(), 40L);

			// Wait 3 seconds so everyone can reorient themselves
			new BukkitRunnable() {
				public void run() {
					s.start();
				}
			}.runTaskLater(NeoSessions.inst(), 60L);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		handleLeaveFight(p);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {
			public void run() {
				Player p = e.getPlayer();
				if (!players.containsKey(p.getUniqueId())) return; // The session player doesn't exist
				SessionPlayer sp = players.get(p.getUniqueId());
				if (sp.getStatus() == PlayerStatus.JOINING) return; // If a player logged in already dead
				if (sp.getStatus() == PlayerStatus.LEAVING) return; // If last dead, don't make them spectate

				// If everyone in the fight is dead, return everyone
				if (spectatingBoss.containsKey(p.getUniqueId())) {
					p.teleport(spectatingBoss.get(p.getUniqueId()).getCoords()); // Tp after death to boss
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vanish " + p.getName() + " on");
					p.setGameMode(GameMode.ADVENTURE);
					p.setInvulnerable(true);
					PlayerAccounts accs = SkillAPI.getPlayerAccountData(p);
					spectatorAcc.put(p.getUniqueId(), accs.getActiveId());
					SkillAPI.getPlayerAccountData(p).setAccount(13);
					p.sendMessage(
							"§4[§c§lMLMC§4] §7You died! You can now spectate, or leave with §c/boss return§7.");
				}
				else {
					p.teleport(instanceSpawn);
				}
			}
		}.runTaskLater(this, 20L);
	}
}
