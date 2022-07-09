package me.neoblade298.neopvp.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neopvp.PvpAccount;
import me.neoblade298.neopvp.PvpManager;

public class PvpListener implements Listener {
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Player killer = victim.getKiller();

		if (killer == null) return;
	}
	
	@EventHandler
	public void onPvp(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player pa = (Player) e.getDamager();
		Player pv = (Player) e.getEntity();
		
		PvpAccount attacker = PvpManager.getAccount(pa);
		PvpAccount victim = PvpManager.getAccount(pv);
		
		// TODO: Make check for if user is in a protection-bypassed region
		
		if (attacker.isProtected()) {
			Util.msg(pa, "&cYou cannot attack others while pvp protected!");
			e.setCancelled(true);
			return;
		}
		
		if (victim.isProtected()) {
			Util.msg(pa, "&cThis user is currently pvp protected!");
			e.setCancelled(true);
			return;
		}
	}
}
