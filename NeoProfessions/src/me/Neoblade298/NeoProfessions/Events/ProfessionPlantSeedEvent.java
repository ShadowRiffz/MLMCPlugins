package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.Neoblade298.NeoProfessions.Gardens.Fertilizer;
import me.Neoblade298.NeoProfessions.Gardens.Garden;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class ProfessionPlantSeedEvent extends Event {
	StoredItem seed;
	Fertilizer fertilizer;
	Garden garden;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionPlantSeedEvent(StoredItem seed, Fertilizer fertilizer, Garden garden) {
		this.seed = seed;
		this.fertilizer = fertilizer;
		this.garden = garden;
	}
	
	public StoredItem getSeed() {
		return seed;
	}
	
	public Fertilizer getFertilizer() {
		return fertilizer;
	}

	public Garden getGarden() {
		return garden;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
