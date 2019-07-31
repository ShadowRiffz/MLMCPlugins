package me.neoblade298.neoinstantwarps;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class Main extends JavaPlugin implements Listener {
	
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInstantWarps Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		Plugin essPlug = Bukkit.getPluginManager().getPlugin("Essentials");
		Essentials ess = null;
		if (essPlug instanceof Essentials) {
			ess = (Essentials) essPlug;
		}
	    
	    // Get command listener
	    this.getCommand("iwarp").setExecutor(new Commands(this, ess));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoInstantWarps Disabled");
	    super.onDisable();
	}
	
}
