package me.neoblade298.neosessions.sessions;

import java.util.UUID;

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
	
	public Player getPlayer() {
		return p;
	}
	
	public UUID getUUID() {
		return p.getUniqueId();
	}
}
