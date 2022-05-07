package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;

public class OpenProfessionInvEvent extends Event {
	ProfessionInventory inv;
    private static final HandlerList handlers = new HandlerList();

	public OpenProfessionInvEvent(ProfessionInventory inv) {
		this.inv = inv;
	}
	
	public ProfessionInventory getInventory() {
		return inv;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
