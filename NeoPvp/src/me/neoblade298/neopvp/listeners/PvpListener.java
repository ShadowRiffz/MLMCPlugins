package me.neoblade298.neopvp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PvpListener implements Listener {
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Player killer = victim.getKiller();

		if (killer == null) return;
	}
}
