package me.neoblade298.neoquests;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.PlayerListener;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoQuests inst;
	public static Random rand = new Random();
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	    this.getCommand("cmd").setExecutor(new Commands(this));
	    
	    // Managers
	    new ConversationManager();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuests Disabled");
	    super.onDisable();
	}
	
	public static NeoQuests inst() {
		return inst;
	}
}
