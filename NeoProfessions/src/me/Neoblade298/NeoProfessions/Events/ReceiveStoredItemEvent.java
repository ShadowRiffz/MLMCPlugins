package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class ReceiveStoredItemEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
	private Player p;
	private StoredItem item;
	public ReceiveStoredItemEvent(Player p, StoredItem item) {
		this.p = p;
		this.item = item;
	}
	public Player getPlayer() {
		return p;
	}
	public StoredItem getItem() {
		return item;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
