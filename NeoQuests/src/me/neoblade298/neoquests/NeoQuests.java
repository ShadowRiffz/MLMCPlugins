package me.neoblade298.neoquests;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoQuests inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("cmd").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuests Disabled");
	    super.onDisable();
	}
	
	public static NeoQuests inst() {
		return inst;
	}
}
