package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public class ProfessionSlotSuccessEvent extends Event {
	ItemStack slotted;
	Augment augment;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionSlotSuccessEvent(ItemStack slotted, Augment augment) {
		this.slotted = slotted;
		this.augment = augment;
	}
	
	public ItemStack getSlotted() {
		return slotted;
	}
	
	public Augment getAugment() {
		return augment;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
