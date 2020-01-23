package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
		Player p = e.getPlayer();
	}
}
