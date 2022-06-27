package me.neoblade298.neoleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neoleaderboard.commands.*;
import me.neoblade298.neoleaderboard.listeners.PointsListener;
import me.neoblade298.neoleaderboard.points.PointsManager;

public class NeoLeaderboard extends JavaPlugin {
	private static NeoLeaderboard inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLeaderboard Enabled");
		inst = this;

		PointsManager.initialize();
		initCommands();
		
		Bukkit.getPluginManager().registerEvents(new PointsListener(), this);
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLeaderboard Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		CommandManager mngr = new CommandManager("nations", this);
		mngr.register(new CmdNations());
		
		mngr = new CommandManager("nl", this);
		mngr.register(new CmdNLNation());
		mngr.register(new CmdNLTown());
		mngr.registerCommandList("help");
	}
	
	public static NeoLeaderboard inst() {
		return inst;
	}
}
