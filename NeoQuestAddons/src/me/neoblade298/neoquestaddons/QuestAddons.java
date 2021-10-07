package me.neoblade298.neoquestaddons;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import com.live.bemmamin.gps.api.GPSAPI;

import me.blackvein.quests.Quests;
import me.blackvein.quests.Stage;
import me.blackvein.quests.events.quest.QuestQuitEvent;
import me.blackvein.quests.events.quester.QuesterPostChangeStageEvent;
import me.blackvein.quests.events.quester.QuesterPostCompleteQuestEvent;
import me.blackvein.quests.events.quester.QuesterPostStartQuestEvent;

public class QuestAddons extends JavaPlugin implements org.bukkit.event.Listener {
	private Quests quests;
	private GPSAPI gps;
	public HashMap<String, HashMap<Integer, String>> gpsPoints;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoQuestQuitter Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		loadConfig();
		
	    this.getCommand("quitquest").setExecutor(new CmdQuitQuest(this));
	    this.getCommand("viewquest").setExecutor(new CmdViewQuest(this));
	    quests = (Quests) Bukkit.getPluginManager().getPlugin("Quests");
	   //  gps = new GPSAPI(this);
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoQuestQuitter Disabled");
	    for (Player p : Bukkit.getOnlinePlayers()) {
	    	//if (gps.gpsIsActive(p)) {
	    	//	gps.stopGPS(p);
	    	//}
	    }
	    super.onDisable();
	}
	
	public Quests getQuests() {
		return quests;
	}
	
	public void loadConfig() {
		gpsPoints = new HashMap<String, HashMap<Integer, String>>();
		
		File file = new File(getDataFolder(), "gps.yml");
		if (!file.exists()) {
			saveResource("gps.yml", false);
		}
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		
		ConfigurationSection gpsSec = yaml.getConfigurationSection("GPS");
		for (String quest : gpsSec.getKeys(false)) {
			HashMap<Integer,String> pointsInQuest = new HashMap<Integer,String>();
			ConfigurationSection questSec = gpsSec.getConfigurationSection(quest);
			for (String num : questSec.getKeys(false)) {
				pointsInQuest.put(Integer.parseInt(num), questSec.getString(num));
			}
			gpsPoints.put(quest, pointsInQuest);
		}
	}
	
	/*
    @EventHandler
    public void onQuitQuest(final QuestQuitEvent e) {
    	if (gps.gpsIsActive(e.getQuester().getPlayer())) {
    		gps.stopGPS(e.getQuester().getPlayer());
    	}
    }

    @EventHandler
    public void onQuestComplete(final QuesterPostCompleteQuestEvent e) {
    	if (gps.gpsIsActive(e.getQuester().getPlayer())) {
    		gps.stopGPS(e.getQuester().getPlayer());
    	}
    }

    @EventHandler
    public void onTakeQuest(final QuesterPostStartQuestEvent e) {
    	String quest = e.getQuest().getName();

		// See if we have a gps for that stage of that quest
		if (gpsPoints.containsKey(quest)) {
			HashMap<Integer, String> questPoints = gpsPoints.get(quest);
		
			if (questPoints.containsKey(0)) {
				gps.startGPS(e.getQuester().getPlayer(), questPoints.get(0));
			}
		}
    }
	

    @EventHandler
    public void onStageChange(final QuesterPostChangeStageEvent e) {
    	LinkedList<Stage> stages = e.getQuest().getStages();
    	int stageNum = -1;
    	String quest = e.getQuest().getName();

		// See if we have a gps for that stage of that quest
		if (gpsPoints.containsKey(quest)) {
			HashMap<Integer, String> questPoints = gpsPoints.get(quest);
    	
	    	/// Iterate through every stage in the quest, find the one we're on
			Iterator<Stage> iter = stages.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Stage stage = iter.next();
				if (stage.equals(e.getCurrentStage())) {
					stageNum = i;
					break;
				}
				i++;
			}
		
			if (questPoints.containsKey(stageNum)) {
				gps.startGPS(e.getQuester().getPlayer(), questPoints.get(stageNum));
			}
		}
    }
    */
}
