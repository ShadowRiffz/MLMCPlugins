package me.neoblade298.neoquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.commands.*;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.GeneralListener;
import me.neoblade298.neoquests.listeners.NavigationListener;
import me.neoblade298.neoquests.listeners.NpcListener;
import me.neoblade298.neoquests.listeners.ObjectiveListener;
import me.neoblade298.neoquests.listeners.QuesterListener;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.objectives.ObjectiveManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	public static Random rand = new Random();
	
	private static NeoQuests inst;
	private static HashSet<Player> debuggers = new HashSet<Player>();
	private static ArrayList<Manager> managers = new ArrayList<Manager>();
	private static HashMap<String, CommandManager> commands = new HashMap<String, CommandManager>();
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");
		
		getServer().getPluginManager().registerEvents(new NpcListener(), this);
		getServer().getPluginManager().registerEvents(new ObjectiveListener(), this);
		getServer().getPluginManager().registerEvents(new GeneralListener(), this);
		getServer().getPluginManager().registerEvents(new NavigationListener(), this);
		getServer().getPluginManager().registerEvents(new QuesterListener(), this);
		
		initCommands();
	    
	    // Managers
		try {
		    new ActionManager();
		    new ObjectiveManager();
		    managers.add(new ConversationManager());
		    managers.add((Manager) NeoCore.registerIOComponent(this, new QuestsManager()));
		    managers.add(new NavigationManager());
		}
		catch (Exception e) {
			showWarning("Failed to enable managers on startup", e);
		}
	}
	
	private void initCommands() {
		String cmd = "quest";
		CommandManager quest = new CommandManager(cmd);
		quest.register(new CmdQuestBase());
	    this.getCommand(cmd).setExecutor(quest);
	    commands.put(cmd, quest);

	    cmd = "quests";
		CommandManager quests = new CommandManager(cmd);
		quests.registerCommandList("");
		quests.register(new CmdQuestsQuit());
		quests.register(new CmdQuestsTake());
		quests.register(new CmdQuestsLog());
		quests.register(new CmdQuestsView());
	    this.getCommand(cmd).setExecutor(quests);
	    commands.put(cmd, quests);

	    cmd = "questadmin";
		CommandManager questadmin = new CommandManager(cmd);
		questadmin.registerCommandList("");
		questadmin.register(new CmdQuestAdminReload());
	    this.getCommand(cmd).setExecutor(questadmin);
	    commands.put(cmd, questadmin);

	    cmd = "navigation";
		CommandManager navigation = new CommandManager(cmd);
		navigation.registerCommandList("");
		navigation.register(new CmdNavigationTo());
		navigation.register(new CmdNavigationFrom());
		navigation.register(new CmdNavigationStart());
		navigation.register(new CmdNavigationStop());
	    this.getCommand(cmd).setExecutor(navigation);
	    commands.put(cmd, navigation);

	    cmd = "adminnavigation";
		CommandManager anavigation = new CommandManager(cmd);
		anavigation.registerCommandList("", ChatColor.DARK_RED);
		anavigation.register(new CmdANavigationSave());
		anavigation.register(new CmdANavigationStart());
		anavigation.register(new CmdANavigationCreate());
		anavigation.register(new CmdANavigationExit());
		anavigation.register(new CmdANavigationTo());
		anavigation.register(new CmdANavigationFrom());
	    this.getCommand(cmd).setExecutor(anavigation);
	    commands.put(cmd, anavigation);
	}
	
	public void onDisable() {
		for (Manager mngr : managers) {
			mngr.cleanup();
		}
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
		for (Manager mngr : managers) {
			mngr.reload();
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
