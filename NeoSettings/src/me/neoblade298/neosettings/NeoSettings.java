package me.neoblade298.neosettings;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoSettings extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoSettings Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("cmd").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoSettings Disabled");
	    super.onDisable();
	}
	
}
