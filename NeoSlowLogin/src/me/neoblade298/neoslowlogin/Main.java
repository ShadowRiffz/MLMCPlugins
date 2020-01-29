package me.neoblade298.neoslowlogin;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	HashMap<String, Long> leaveTimes = new HashMap<String, Long>();
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoSlowLogin Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoSlowLogin Disabled");
	}

	@EventHandler
	public void OnJoinFirst(AsyncPlayerPreLoginEvent e) {
		if (Bukkit.getPlayer(e.getName()) != null) {
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					ChatColor.RED + "A player with your name is already logged on!");
		}
		else if (!canJoin(e.getName())) {
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					ChatColor.RED + "Reconnected too quickly! Please wait a few seconds!");
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		handleLeave(e.getPlayer().getName());
	}
	
	public void handleLeave(String name) {
		leaveTimes.put(name, System.currentTimeMillis());
		
		BukkitRunnable endTimer = new BukkitRunnable() {
			public void run() {
				leaveTimes.remove(name);
			}
		};
		endTimer.runTaskLaterAsynchronously(this, 60L);
	}
	
	public boolean canJoin(String name) {
		if (leaveTimes.containsKey(name)) {
			long leaveTime = leaveTimes.get(name);
			long currTime = System.currentTimeMillis();
			long elapsedTime = currTime - leaveTime;
			if (elapsedTime > 3000) {
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
}