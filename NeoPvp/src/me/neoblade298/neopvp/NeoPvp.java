package me.neoblade298.neopvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoPvp extends JavaPlugin {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPPRs Enabled");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
}
