package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class GeneralListeners implements Listener {
	HashMap<Player, ItemStack> selectedRepair = new HashMap<Player, ItemStack>();

	Main main;
	Util util;

	public GeneralListeners(Main main) {
		this.main = main;
		util = new Util();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			main.cManager.initPlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		try {
			main.cManager.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		try {
			main.cManager.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
