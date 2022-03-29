package me.Neoblade298.NeoProfessions.Events;

import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProfessionHarvestEvent extends Event {
	MinigameParameters params;
	Player player;
	ProfessionType type;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionHarvestEvent(Player p, MinigameParameters params, ProfessionType type) {
		this.player = p;
		this.params = params;
		this.type = type;
	}

	public MinigameParameters getParams() {
		return params;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public Player getPlayer() {
		return player;
	}
	
	public ProfessionType getType() {
		return type;
	}
}
