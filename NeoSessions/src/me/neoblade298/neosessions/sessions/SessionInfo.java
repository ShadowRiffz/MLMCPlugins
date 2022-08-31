package me.neoblade298.neosessions.sessions;

import org.bukkit.Location;

public abstract class SessionInfo {
	private String key;
	private Location spawn;
	public SessionInfo(String key, Location spawn) {
		this.key = key;
		this.spawn = spawn;
	}
	public abstract Session createSession(String from, int numPlayers, int multiplier);
	public abstract void cleanupSession();
	
	public Location getSpawn() {
		return spawn;
	}
	
	public String getKey() {
		return key;
	}
}
