package me.neoblade298.neoquestaddons;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.blackvein.quests.Quests;

public class QuestAddons extends JavaPlugin implements org.bukkit.event.Listener {
	private Quests quests;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoQuestQuitter Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("quitquest").setExecutor(new CmdQuitQuest(this));
	    this.getCommand("viewquest").setExecutor(new CmdViewQuest(this));
	    quests = (Quests) Bukkit.getPluginManager().getPlugin("Quests");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuestQuitter Disabled");
	    super.onDisable();
	}
	
	public Quests getQuests() {
		return quests;
	}
	
}
