package me.neoblade298.neosessions.sessions;

import java.util.HashMap;
import java.util.UUID;

public abstract class Session {
	private int multiplier;
	private HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	public Session(int multiplier) {
		this.multiplier = multiplier;
	}
	
	public abstract void start();
	public abstract void end();
	
	public void addPlayer(SessionPlayer sp) {
		players.put(sp.getUUID(), sp);
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
}
