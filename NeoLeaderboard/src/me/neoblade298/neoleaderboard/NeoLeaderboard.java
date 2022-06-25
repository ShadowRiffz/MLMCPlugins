package me.neoblade298.neoleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.commands.CommandManager;

public class NeoLeaderboard extends JavaPlugin {
	private static NeoLeaderboard inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLeaderboard Enabled");
		inst = this;

		PointsManager.initialize();
		initCommands();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLeaderboard Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		String cmd = "nations";
		CommandManager mngr = new CommandManager(cmd);
		mngr.registerCommandList("");
		this.getCommand(cmd).setExecutor(mngr);
	}
	
	public static NeoLeaderboard inst() {
		return inst;
	}
}
