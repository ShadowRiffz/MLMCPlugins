package me.neoblade298.neopvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoPvp extends JavaPlugin {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPvp Enabled");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPvp Disabled");
	    super.onDisable();
	}
	
}
