package me.neoblade298.neoquestgps;

import com.live.bemmamin.gps.Vars;
import com.live.bemmamin.gps.api.GPSAPI;
import com.live.bemmamin.gps.logic.Point;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.blackvein.quests.Stage;
import me.blackvein.quests.events.quest.QuestQuitEvent;
import me.blackvein.quests.events.quester.QuesterPostChangeStageEvent;
import me.blackvein.quests.events.quester.QuesterPostCompleteQuestEvent;
import me.blackvein.quests.events.quester.QuesterPostFailQuestEvent;
import me.blackvein.quests.events.quester.QuesterPostStartQuestEvent;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static GPSAPI gpsapi;
	private Quests quests;
	private boolean reload = false;
	private FileConfiguration cfg;
	public boolean citizensToInteract;
	public boolean citizensToKill;
	public boolean locationsToReach;
	public boolean itemDeliveryTargets;
	public HashMap<String, ArrayList<Integer>> enabledQuests;
	public HashMap<String, QuestOverride> overriddenQuests;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		if (pm.getPlugin("GPS") != null) {
			if (!pm.getPlugin("GPS").isEnabled()) {
				getLogger().warning("GPS was detected, but is not enabled! Fix it to allow linkage.");
				return;
			}
			gpsapi = new GPSAPI(this);
			Vars.getInstance().setMaxDistanceToEntry(9999.0D);
		}
		if (pm.getPlugin("Quests") != null) {
			if (!pm.getPlugin("Quests").isEnabled()) {
				getLogger().warning("Quests was detected, but is not enabled! Fix it to allow linkage.");
				return;
			}
			this.quests = ((Quests) pm.getPlugin("Quests"));
		}
		getServer().getPluginManager().registerEvents(new ServerListener(), this);
		getServer().getPluginManager().registerEvents(new QuestsListener(), this);
		if ((gpsapi != null) && (this.quests.isEnabled())) {
			activate();
		}
	}

	public void onDisable() {
		PluginManager pm = getServer().getPluginManager();
		if ((pm.getPlugin("GPS") != null) && (!pm.getPlugin("GPS").isEnabled())) {
			for (Quester q : this.quests.getQuesters()) {
				if ((q != null) && (gpsapi.gpsIsActive(q.getPlayer()))) {
					gpsapi.stopGPS(q.getPlayer());
				}
			}
		}
	}

	private void activate() {
		if (this.reload) {
			reloadConfig();
		} else {
			this.reload = true;
		}
		this.cfg = getConfig();
		this.cfg.options().copyDefaults(true);
		saveConfig();

		this.citizensToInteract = this.cfg.getBoolean("citizens-to-interact", true);
		this.citizensToKill = this.cfg.getBoolean("citizens-to-kill", true);
		this.locationsToReach = this.cfg.getBoolean("locations-to-reach", true);
		this.itemDeliveryTargets = this.cfg.getBoolean("item-delivery-targets", true);
		
		// Load quests that we configure to enable GPS
		enabledQuests = new HashMap<String, ArrayList<Integer>>();
		ConfigurationSection questList = this.cfg.getConfigurationSection("enabled-quests");
		for (String quest : questList.getKeys(false)) {
			enabledQuests.put(quest, (ArrayList<Integer>) questList.getIntegerList(quest));
		}
		
		// Load quests where we override it with a GPS location
		enabledQuests = new HashMap<String, ArrayList<Integer>>();
		ConfigurationSection overrideList = this.cfg.getConfigurationSection("overridden-quests");
		for (String quest : overrideList.getKeys(false)) {
			overriddenQuests.put(quest, new QuestOverride(overrideList.getString(quest)));
		}
	}

	private class ServerListener implements Listener {
		private ServerListener() {
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event) {
			Plugin p = event.getPlugin();
			String name = p.getDescription().getName();
			if (((name.equals("GPS")) || (name.equals("Quests"))) && (Main.gpsapi != null)
					&& (Main.this.quests.isEnabled())) {
				Main.this.activate();
			}
		}
	}

	private class QuestsListener implements Listener {
		private QuestsListener() {
		}

		@EventHandler
		public void onQuesterPostStartQuest(QuesterPostStartQuestEvent event) {
			Main.this.updateGPS(event.getQuest(), event.getQuester());
		}

		@EventHandler
		public void onQuesterPostChangeStage(QuesterPostChangeStageEvent event) {
			Main.this.updateGPS(event.getQuest(), event.getQuester());
		}

		@EventHandler
		public void onQuesterPostCompleteQuest(QuesterPostCompleteQuestEvent event) {
			Main.this.stopGPS(event.getQuest(), event.getQuester());
		}

		@EventHandler
		public void onQuesterPostFailQuest(QuesterPostFailQuestEvent event) {
			Main.this.stopGPS(event.getQuest(), event.getQuester());
		}

		@EventHandler
		public void onQuestQuitEvent(QuestQuitEvent event) {
			Main.this.stopGPS(event.getQuest(), event.getQuester());
		}
	}

	public boolean updateGPS(Quest quest, Quester quester) {
		LinkedList<Location> targetLocations = new LinkedList<Location>();
		Stage stage = quester.getCurrentStage(quest);
		int stageNum = quester.getCurrentQuests().get(quest);
		if (stage == null) {
			getLogger().severe("Called updateGPS() with a null stage from quest " + quest.getName());
			return false;
		}
		// First check for overrides
		if (overriddenQuests.containsKey(quest.getName()) && overriddenQuests.get(quest.getName()).getStage() == stageNum) {
			targetLocations.add(overriddenQuests.get(quest.getName()).getLoc());
		}
		
		// Next check for simple stage enabled quests
		else if (enabledQuests.containsKey(quest.getName()) && enabledQuests.get(quest.getName()).contains(stageNum)) {
			if ((this.citizensToInteract) && (stage.getCitizensToInteract() != null)
					&& (stage.getCitizensToInteract().size() > 0)) {
				if (this.quests.getDependencies().getCitizens() != null) {
					for (Integer i : stage.getCitizensToInteract()) {
						targetLocations.add(this.quests.getNPCLocation(i.intValue()));
					}
				}
			} else if ((this.citizensToKill) && (stage.getCitizensToKill() != null)
					&& (stage.getCitizensToKill().size() > 0)) {
				if (this.quests.getDependencies().getCitizens() != null) {
					for (Integer i : stage.getCitizensToKill()) {
						targetLocations.add(this.quests.getNPCLocation(i.intValue()));
					}
				}
			} else if ((this.locationsToReach) && (stage.getLocationsToReach() != null)
					&& (stage.getLocationsToReach().size() > 0)) {
				targetLocations.addAll(stage.getLocationsToReach());
			} else if ((this.itemDeliveryTargets) && (stage.getItemDeliveryTargets() != null)
					&& (stage.getItemDeliveryTargets().size() > 0)
					&& (this.quests.getDependencies().getCitizens() != null)) {
				for (Integer i : stage.getItemDeliveryTargets()) {
					targetLocations.add(this.quests.getDependencies().getCitizens().getNPCRegistry().getById(i.intValue())
							.getStoredLocation());
				}
			}
			if ((targetLocations != null) && (!targetLocations.isEmpty())) {
				int index = 1;
				String pointName = "quests-" + quester.getPlayer().getUniqueId().toString() + "-" + quest.getName() + "-"
						+ stage.toString() + "-";
				Player p = quester.getPlayer();
				for (Location l : targetLocations) {
					if ((l.getWorld().getName().equals(p.getWorld().getName())) && (!gpsapi.gpsIsActive(p))) {
						gpsapi.addPoint(pointName + index, l);
						index++;
					}
				}
				for (int i = 1; i < targetLocations.size(); i++) {
					if (!gpsapi.gpsIsActive(quester.getPlayer())) {
						gpsapi.connect(pointName + i, pointName + (i + 1), true);
					}
				}
				if (!gpsapi.gpsIsActive(quester.getPlayer())) {
					gpsapi.startGPS(quester.getPlayer(), pointName + (index - 1));
				}
			}
		}
		
		/* Unneeded code, maybe bring back later
		if ((this.citizensToInteract) && (stage.getCitizensToInteract() != null)
				&& (stage.getCitizensToInteract().size() > 0)) {
			if (this.quests.getDependencies().getCitizens() != null) {
				for (Integer i : stage.getCitizensToInteract()) {
					targetLocations.add(this.quests.getNPCLocation(i.intValue()));
				}
			}
		} else if ((this.citizensToKill) && (stage.getCitizensToKill() != null)
				&& (stage.getCitizensToKill().size() > 0)) {
			if (this.quests.getDependencies().getCitizens() != null) {
				for (Integer i : stage.getCitizensToKill()) {
					targetLocations.add(this.quests.getNPCLocation(i.intValue()));
				}
			}
		} else if ((this.locationsToReach) && (stage.getLocationsToReach() != null)
				&& (stage.getLocationsToReach().size() > 0)) {
			targetLocations.addAll(stage.getLocationsToReach());
		} else if ((this.itemDeliveryTargets) && (stage.getItemDeliveryTargets() != null)
				&& (stage.getItemDeliveryTargets().size() > 0)
				&& (this.quests.getDependencies().getCitizens() != null)) {
			for (Integer i : stage.getItemDeliveryTargets()) {
				targetLocations.add(this.quests.getDependencies().getCitizens().getNPCRegistry().getById(i.intValue())
						.getStoredLocation());
			}
		}
		if ((targetLocations != null) && (!targetLocations.isEmpty())) {
			int index = 1;
			String pointName = "quests-" + quester.getPlayer().getUniqueId().toString() + "-" + quest.getName() + "-"
					+ stage.toString() + "-";
			Player p = quester.getPlayer();
			for (Location l : targetLocations) {
				if ((l.getWorld().getName().equals(p.getWorld().getName())) && (!gpsapi.gpsIsActive(p))) {
					gpsapi.addPoint(pointName + index, l);
					index++;
				}
			}
			for (int i = 1; i < targetLocations.size(); i++) {
				if (!gpsapi.gpsIsActive(quester.getPlayer())) {
					gpsapi.connect(pointName + i, pointName + (i + 1), true);
				}
			}
			if (!gpsapi.gpsIsActive(quester.getPlayer())) {
				gpsapi.startGPS(quester.getPlayer(), pointName + (index - 1));
			}
		}
		*/
		return (targetLocations != null) && (!targetLocations.isEmpty());
	}

	public boolean stopGPS(Quest quest, Quester quester) {
		Player p = quester.getPlayer();
		if (gpsapi.gpsIsActive(p)) {
			gpsapi.stopGPS(p);
			for (Point point : gpsapi.getAllPoints()) {
				if (point.getName().startsWith("quests-" + p.getUniqueId().toString() + "-" + quest.getName())) {
					try {
						gpsapi.removePoint(point.getName());
					} catch (ConcurrentModificationException localConcurrentModificationException) {
					}
				}
			}
			return true;
		}
		return false;
	}
}
