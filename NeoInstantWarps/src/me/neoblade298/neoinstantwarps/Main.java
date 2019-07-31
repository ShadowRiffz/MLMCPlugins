package me.neoblade298.neoinstantwarps;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class Main extends JavaPlugin implements Listener {
	
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInstantWarps Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
	    
	    // Get command listener
	    this.getCommand("iwarp").setExecutor(new Commands(this, ess));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
}
