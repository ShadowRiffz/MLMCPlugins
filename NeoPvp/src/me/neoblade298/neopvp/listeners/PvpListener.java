package me.neoblade298.neopvp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.protection.regions.RegionQuery.QueryOption;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neopvp.NeoPvp;
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

		// Make check for if user is in an allowed protection region
		RegionContainer ca = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery qa = ca.createQuery();
		ApplicableRegionSet seta = qa.getApplicableRegions(BukkitAdapter.adapt(pa.getLocation()), QueryOption.NONE);

		RegionContainer cv = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery qv = cv.createQuery();
		ApplicableRegionSet setv = qv.getApplicableRegions(BukkitAdapter.adapt(pv.getLocation()), QueryOption.NONE);

		// pvp protection
		if (seta.testState(WorldGuardPlugin.inst().wrapPlayer(pa), NeoPvp.PROTECTION_ALLOWED_FLAG)
				|| setv.testState(WorldGuardPlugin.inst().wrapPlayer(pv), NeoPvp.PROTECTION_ALLOWED_FLAG)) {

			if (attacker.isProtected()) {
				Util.msg(pa, "&cYou cannot attack others while pvp protected!");
				e.setCancelled(true);
				return;
			}

			if (victim.isProtected()) {
				Util.msg(pa, "&cThis player is currently pvp protected!");
				e.setCancelled(true);
				return;
			}
		}

		PvpManager.handleKill(pa, pv);
	}
}
