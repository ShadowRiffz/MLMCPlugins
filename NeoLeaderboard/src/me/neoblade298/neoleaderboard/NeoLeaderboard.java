package me.neoblade298.neoleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoLeaderboard extends JavaPlugin {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLeaderboard Enabled");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLeaderboard Disabled");
	    super.onDisable();
	}
	
}
