package me.neoblade298.neosessions.sessions;

import org.bukkit.Location;

public abstract class SessionInfo {
	public abstract Session createSession(String from, int numPlayers, int multiplier);
	public abstract void cleanupSession();
	public abstract Location getPlayerSpawn();
}
