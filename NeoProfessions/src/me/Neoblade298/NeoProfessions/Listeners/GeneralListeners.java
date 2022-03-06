package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class GeneralListeners implements Listener {
	Professions main;
	Util util;

	public GeneralListeners(Professions main) {
		this.main = main;
		util = new Util();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			Professions.cm.initPlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		try {
			Professions.cm.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		try {
			Professions.cm.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
