package me.neoblade298.townychatbridge;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.chatcontrol.api.ChatChannelEvent;

import me.neoblade298.neocore.commands.CommandManager;

public class TownyChatBridge extends JavaPlugin implements Listener {
	private static TownyChatBridge inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("TownyChatBridge Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		
		inst = this;
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("TownyChatBridge Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		CommandManager mngr = new CommandManager("disenchant", this);
	}
	
	public static TownyChatBridge inst() {
		return inst;
	}
}
