package me.neoblade298.neoquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.commands.CmdQuestBase;
import me.neoblade298.neoquests.commands.CmdQuestsQuit;
import me.neoblade298.neoquests.commands.CmdQuestsReload;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.GeneralListener;
import me.neoblade298.neoquests.listeners.NpcListener;
import me.neoblade298.neoquests.listeners.ObjectiveListener;
import me.neoblade298.neoquests.objectives.ObjectiveManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	public static Random rand = new Random();
	
	private static NeoQuests inst;
	private static HashSet<Player> debuggers = new HashSet<Player>();
	private static ArrayList<Reloadable> reloadables = new ArrayList<Reloadable>();
	private static HashMap<String, CommandManager> commands = new HashMap<String, CommandManager>();
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		
		getServer().getPluginManager().registerEvents(new NpcListener(), this);
		getServer().getPluginManager().registerEvents(new ObjectiveListener(), this);
		getServer().getPluginManager().registerEvents(new GeneralListener(), this);
		
		initCommands();
	    
	    // Managers
		try {
		    new ActionManager();
		    new ObjectiveManager();
		    reloadables.add(new ConversationManager());
		    reloadables.add(new QuestsManager());
		    NeoCore.registerIOComponent(this, new QuestsManager());
		}
		catch (Exception e) {
			showWarning("Failed to enable managers on startup", e);
		}
	}
	
	private void initCommands() {
		CommandManager quest = new CommandManager("quest");
		quest.register(new CmdQuestBase());
	    this.getCommand("quest").setExecutor(quest);
	    commands.put("quest", quest);

		CommandManager quests = new CommandManager("quests");
		quests.registerCommandList("");
		quests.register(new CmdQuestsReload());
		quests.register(new CmdQuestsQuit());
	    this.getCommand("quests").setExecutor(quests);
	    commands.put("quests", quests);
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
	
	public static boolean reloadAll() {
		for (Reloadable rld : reloadables) {
			try {
				rld.reload();
			} catch (NeoIOException e) {
				showWarning("Failed to reload module " + rld.getKey(), e);
				return false;
			}
		}
		return true;
	}
	
	public static void addDebugger(Player p) {
		debuggers.add(p);
	}
	
	public static HashMap<String, CommandManager> getCommands() {
		return commands;
	}
}
