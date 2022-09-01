package me.neoblade298.neosessions.sessions;

import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neosessions.NeoSessions;
import me.neoblade298.neosessions.sessions.stats.Stats;

public abstract class Session {
	private SessionInfo info;
	private Location lastCheckpoint;
	private int numPlayers, multiplier;
	private String from;
	private HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	private HashMap<String, Stats> stats = new HashMap<String, Stats>();

	public Session(SessionInfo info, String from, int numPlayers, int multiplier) {
		this.info = info;
		this.from = from;
		this.numPlayers = numPlayers;
		this.multiplier = multiplier;
		this.lastCheckpoint = info.getSpawn();
	}

	public void start() {

	}

	public void end() {
		new BukkitRunnable() {
			public void run() {
				for (SessionPlayer sp : players.values()) {
					Player p = sp.getPlayer();
					if (p.isOnline()) {
						SessionManager.returnPlayer(p);
					}
				}

				try {
					Statement stmt = NeoCore.getStatement();
					stmt.addBatch("DELETE FROM sessions_sessions WHERE `key` = '" + info.getKey() + "';");
					stmt.addBatch("DELETE FROM sessions_players WHERE session_key = '" + info.getKey() + "';");
					stmt.executeBatch();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.runTaskLater(NeoSessions.inst(), 20L);
	}

	public void addPlayer(SessionPlayer sp) {
		players.put(sp.getUUID(), sp);
		sp.setSession(this);
	}

	public void removePlayer(SessionPlayer sp) {
		players.remove(sp.getUUID());
	}

	public HashMap<UUID, SessionPlayer> getSessionPlayers() {
		return players;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public SessionInfo getSessionInfo() {
		return info;
	}

	public boolean canStart() {
		if (players.size() < numPlayers) {
			return false;
		}
		for (SessionPlayer sp : players.values()) {
			if (sp.getStatus() != PlayerStatus.AWAITING_PLAYERS) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		for (SessionPlayer sp : players.values()) {
			if (sp.getStatus() == PlayerStatus.PARTICIPATING) {
				return false;
			}
		}
		return true;
	}

	public Location getLastCheckpoint() {
		return lastCheckpoint;
	}

	public void setLastCheckpoint(Location loc) {
		this.lastCheckpoint = loc;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void startStats(String key, String display) {
		stats.put(key, new Stats(key, display, multiplier, players.values()));
	}
	
	public Stats stopStats(String key) {
		return stats.remove(key);
	}
	
	public Stats getStats(String key) {
		return stats.get(key);
	}
	
	public Collection<Stats> getStats() {
		return stats.values();
	}
}
