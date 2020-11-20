package me.neoblade298.neoautotag;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoAutoTag Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("autotag").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoAutoTag Disabled");
	    super.onDisable();
	}
	
}
