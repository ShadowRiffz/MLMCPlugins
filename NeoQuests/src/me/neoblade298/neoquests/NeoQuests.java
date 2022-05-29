package me.neoblade298.neoquests;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.commands.NeoCommands;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.commands.QuestBase;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.NpcListener;
import me.neoblade298.neoquests.objectives.ObjectiveManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoQuests inst;
	public static Random rand = new Random();
	public static HashSet<Player> debuggers = new HashSet<Player>();
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		
		getServer().getPluginManager().registerEvents(new NpcListener(), this);
		
		initCommands();
	    
	    // Managers
		try {
		    new ActionManager();
		    new ConversationManager();
		    new ObjectiveManager();
		    new QuestsManager();
		}
		catch (Exception e) {
			showWarning("Failed to enable managers on startup", e);
		}
	}
	
	private void initCommands() {
		CommandManager quest = new CommandManager("quest");
		quest.register(new QuestBase());
	    this.getCommand("quest").setExecutor(quest);
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuests Disabled");
	    super.onDisable();
	}
	
	public static NeoQuests inst() {
		return inst;
	}
	
	public static void showWarning(String warning) {
		Bukkit.getLogger().warning("[NeoQuests] " + warning);
		for (Player p : debuggers) {
			p.sendMessage(warning);
		}
	}
	
	public static void showWarning(String warning, Exception e) {
		Bukkit.getLogger().warning("[NeoQuests] " + warning);
		Bukkit.getLogger().warning("[NeoQuests] " + e.getMessage());
		for (Player p : debuggers) {
			p.sendMessage(warning);
			p.sendMessage(e.getMessage());
		}
		e.printStackTrace();
	}
}
