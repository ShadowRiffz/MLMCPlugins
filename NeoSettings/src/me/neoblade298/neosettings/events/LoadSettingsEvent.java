package me.neoblade298.neosettings.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.neoblade298.neosettings.NeoSettings;

public class LoadSettingsEvent extends Event {
	private NeoSettings plugin;
    private static final HandlerList handlers = new HandlerList();
	
	public LoadSettingsEvent(NeoSettings plugin) {
		this.plugin = plugin;
	}
	
	public NeoSettings getPlugin() {
		return this.plugin;
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}

}
