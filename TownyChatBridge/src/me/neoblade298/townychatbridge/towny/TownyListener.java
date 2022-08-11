package me.neoblade298.townychatbridge.towny;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.chatcontrol.api.ChatChannelEvent;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import me.neoblade298.neocore.bungee.BungeeAPI;

public class TownyListener implements Listener {
	TownyAPI api;
	
	@EventHandler
	public void onTownyMessageTown(ChatChannelEvent e) {
		if (!(e.getSender() instanceof Player)) return;
		Player p = (Player) e.getSender();
		
		if (api == null) api = TownyAPI.getInstance();
		Town t = api.getResidentTownOrNull(api.getResident(p));
		
		// Only send message of players with a town
		if (t == null) return;
		BungeeAPI.sendPluginMessage(p, "townychatout", Arrays.asList(e.getMessage(), t.getUUID()));
	}
}
