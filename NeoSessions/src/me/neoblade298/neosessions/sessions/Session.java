package me.neoblade298.neosessions.sessions;

import java.util.HashMap;
import java.util.UUID;

public abstract class Session {
	private SessionInfo info;
	private int multiplier;
	private HashMap<UUID, SessionPlayer> players = new HashMap<UUID, SessionPlayer>();
	public Session(SessionInfo info, int multiplier) {
		this.info = info;
		this.multiplier = multiplier;
	}
	
	public abstract void start();
	public abstract void end();
	
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
		for (SessionPlayer sp : players.values()) {
			if (sp.getStatus() != PlayerStatus.AWAITING_PLAYERS) {
				return false;
			}
		}
		return true;
	}
}
