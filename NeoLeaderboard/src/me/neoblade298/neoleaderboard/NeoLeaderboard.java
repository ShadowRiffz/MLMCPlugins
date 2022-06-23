package me.neoblade298.neoleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoLeaderboard extends JavaPlugin {
	private static NeoLeaderboard inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLeaderboard Enabled");
		inst = this;
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLeaderboard Disabled");
	    super.onDisable();
	}
	
	public static NeoLeaderboard inst() {
		return inst;
	}
}
