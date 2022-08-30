package me.neoblade298.neosessions.sessions;

import java.util.HashMap;
import java.util.UUID;

public abstract class Session {
	private int multiplier;
	private ArrayList<SessionPlayer> players = new ArrayList<SessionPlayer>();
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
	public ArrayList<String> getSessionPlayers() {
		return players;
	}
}
