package me.neoblade298.neoinstruments;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;

public class PlaySyncedEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Player player;
	
	public PlaySyncedEvent(Player player) {
		this.player = player;
	}
	
	public HandlerList getHandlers(){
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList(){
		return HANDLERS;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
