package me.neoblade298.townychatbridge.towny;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.chatcontrol.api.ChatChannelEvent;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.event.NewNationEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.object.Town;

import me.neoblade298.neocore.bungee.BungeeAPI;

public class TownyListener implements Listener {
	TownyAPI api;
	
	@EventHandler
	public void onTownChat(ChatChannelEvent e) {
		if (!(e.getSender() instanceof Player)) return;
		if (!e.getChannel().getName().equalsIgnoreCase("town")) return;
		Player p = (Player) e.getSender();
		
		if (api == null) api = TownyAPI.getInstance();
		Town t = api.getResidentTownOrNull(api.getResident(p));
		
		// Only send message of players with a town
		if (t == null) return;
		BungeeAPI.sendPluginMessage(p, "townychatout", e.getMessage(), t.getUUID().toString());
	}

	@EventHandler
	public void onCreateTown(NewTownEvent e) {
		String[] list = new String[2];
		list[0] = "newtown";
		list[1] = e.getTown().getUUID().toString();
		BungeeAPI.sendPluginMessage("townyevents", list);
	}
	@EventHandler
	public void onCreateNation(NewNationEvent e) {
		String[] list = new String[3];
		list[0] = "newnation";
		list[1] = e.getNation().getUUID().toString();
		list[2] = e.getNation().getTowns().get(0).getUUID().toString();
		BungeeAPI.sendPluginMessage("townyevents", list);
	}

	@EventHandler
	public void onJoinNation(NationAddTownEvent e) {
		String[] list = new String[3];
		list[0] = "townjoinnation";
		list[1] = e.getTown().getUUID().toString();
		list[2] = e.getNation().getUUID().toString();
		BungeeAPI.sendPluginMessage("townyevents", list);
	}

	@EventHandler
	public void onLeaveNation(NationRemoveTownEvent e) {
		String[] list = new String[3];
		list[0] = "townleavenation";
		list[1] = e.getTown().getUUID().toString();
		list[2] = e.getNation().getUUID().toString();
		BungeeAPI.sendPluginMessage("townyevents", list);
	}
}
