package me.neoblade298.neosessions;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neosessions.sessions.SessionInfo;

public class Main extends JavaPlugin {
	private static Main inst;
	private static HashMap<String, SessionInfo> sessionInfo;
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoSessions Enabled");
		// Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoSessions Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		CommandManager mngr = new CommandManager("session", this);
	}
	
	public static Main inst() {
		return inst;
	}
}
