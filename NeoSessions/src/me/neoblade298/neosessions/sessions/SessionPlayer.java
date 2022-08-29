package me.neoblade298.neosessions.sessions;

import org.bukkit.entity.Player;

public class SessionPlayer {
	private Player p;
	private PlayerStatus status;
	private String sessionKey;
	
	public SessionPlayer(Player p, String sessionKey) {
		this.p = p;
		this.sessionKey = sessionKey;
		this.status = PlayerStatus.JOINING;
	}
}
