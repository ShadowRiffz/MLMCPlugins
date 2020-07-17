package me.neoblade298.neomonopoly;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Monopoly extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoMonopoly Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("cmd").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoMonopoly Disabled");
	    super.onDisable();
	}
	
}
