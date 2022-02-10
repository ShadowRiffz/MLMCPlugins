package me.neoblade298.neomythicextension.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestDropEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private double chance;
	private int dropType;

	public ChestDropEvent(Player p, double chance, int dropType) {
		this.player = p;
		this.chance = chance;
		this.dropType = 0;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public double getChance() {
		return chance;
	}

	public void setChance(double chance) {
		this.chance = chance;
	}
	
	public int getDropType() {
		return this.dropType;
	}
	
	public void setDropType(int dropType) {
		this.dropType = dropType;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
