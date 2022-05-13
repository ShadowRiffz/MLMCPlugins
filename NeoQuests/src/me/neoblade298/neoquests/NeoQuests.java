package me.neoblade298.neoquests;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.PlayerListener;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoQuests inst;
	public static Random rand = new Random();
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	    // this.getCommand("quests").setExecutor(new Commands(this));
	    
	    // Managers
	    new ActionManager();
	    new ConversationManager();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuests Disabled");
	    super.onDisable();
	}
	
	public static NeoQuests inst() {
		return inst;
	}
	
	public static void showWarning(CommandSender s, String warning) {
		if (s == null || s instanceof ConsoleCommandSender) {
			Bukkit.getLogger().warning("[NeoQuests] " + warning);
		}
		else {
			s.sendMessage(warning);
		}
	}
	
	public static void showWarning(CommandSender s, String warning, Exception e) {
		if (s == null || s instanceof ConsoleCommandSender) {
			Bukkit.getLogger().warning("[NeoQuests] " + warning);
			Bukkit.getLogger().warning("[NeoQuests] " + e.getMessage());
		}
		else {
			s.sendMessage(warning);
			s.sendMessage(e.getMessage());
		}
		e.printStackTrace();
	}
}
