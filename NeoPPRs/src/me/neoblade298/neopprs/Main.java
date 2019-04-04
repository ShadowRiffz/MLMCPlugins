package me.neoblade298.neopprs;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static HashMap<String, Boolean> creating_ppr;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPPRs Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    // Initiate hashmap
		creating_ppr = new HashMap<String, Boolean>();
	    
	    // Get command listener
	    this.getCommand("ppr").setExecutor(new Commands());
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
}
