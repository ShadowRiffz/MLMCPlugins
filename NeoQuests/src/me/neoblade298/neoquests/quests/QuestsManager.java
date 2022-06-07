package me.neoblade298.neoquests.quests;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;

public class QuestsManager implements IOComponent, Manager {
	private static HashMap<UUID, Quester> questers = new HashMap<UUID, Quester>();
	private static HashMap<String, Quest> quests = new HashMap<String, Quest>();
	private static HashMap<String, Questline> questlines = new HashMap<String, Questline>();
	private static ArrayList<QuestRecommendation> recommendations = new ArrayList<QuestRecommendation>();
	private static File data = new File(NeoQuests.inst().getDataFolder(), "quests");
	private static FileLoader questsLoader, questlinesLoader, recommendationsLoader;
	
	static {
		questsLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					if (quests.containsKey(key)) {
						NeoQuests.showWarning("Duplicate quest " + key + "in file " + file.getPath() + ", " +
								"the loaded quest with this key is in " + quests.get(key).getFileLocation());
						continue;
					}
					quests.put(key.toUpperCase(), new Quest(cfg.getConfigurationSection(key), file));
				} catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load quest " + key, e);
				}
			}
		};
		
		questlinesLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					if (questlines.containsKey(key)) {
						NeoQuests.showWarning("Duplicate questline " + key + "in file " + file.getPath() + ", " +
								"the loaded questline with this key is in " + questlines.get(key).getFileLocation());
						continue;
					}
					questlines.put(key.toUpperCase(), new Questline(cfg.getConfigurationSection(key), file));
				} catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load questline " + key, e);
				}
			}
		};
		
		recommendationsLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					for (String line : cfg.getStringList(key)) {
						recommendations.add(new QuestRecommendation(new LineConfig(line)));
					}
				} catch (NeoIOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public QuestsManager() throws NeoIOException {
		reload();
	}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {	}
	
	@Override
	public void loadPlayer(Player p, Statement stmt) {
		/*
		try {
			Quester quester = new Quester(p);
			questers.put(p.getUniqueId(), quester);
			ResultSet rs = stmt.executeQuery("SELECT * FROM quests_quests WHERE UUID = '" + p.getUniqueId() + "';");
			
			// Active quests
			HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();
			while (rs.next()) {
				String qname = rs.getString(2);
				int stage = rs.getInt(3);
				String set = rs.getString(4);
				
				// Parse counts
				String[] scounts = rs.getString(5).split(",");
				int[] counts = new int[scounts.length];
				for (int i = 0; i < scounts.length; i++) {
					counts[i] = Integer.parseInt(scounts[i]);
				}
				
				Quest quest = quests.get(rs.getString(2));
				QuestInstance qi = activeQuests.getOrDefault(qname, new QuestInstance(quester, quest, stage));
				qi.getObjectiveSetInstance(set).setObjectiveCounts(counts);
				quester.resumeQuest(qi);
			}
			
			// Completed quests
			rs = stmt.executeQuery("SELECT * FROM quests_completed WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				quester.addCompletedQuest(new CompletedQuest(quests.get(rs.getString(2)), rs.getInt(3), rs.getBoolean(4)));
			}
			
			// Active questlines
			rs = stmt.executeQuery("SELECT * FROM quests_questlines WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				quester.addQuestline(questlines.get(rs.getString(2)));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Quests failed to load or init quest data for user " + p.getName());
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		/*
		try {
			Quester quester = questers.get(p.getUniqueId());

			// Save user
			for (QuestInstance qi : quester.getActiveQuests()) {
				// Delete existing active quests
				delete.addBatch("DELETE FROM quests_quests WHERE uuid = '" + p.getUniqueId() + "';"); 
				for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
					// Replace with new ones
					insert.addBatch("REPLACE INTO quests_quests VALUES('"
							+ p.getUniqueId() + "','" + qi.getQuest().getKey() + "'," + qi.getStage()
							+ ",'" + osi.getKey() + "','" + osi.serializeCounts() + "');");
				}
			}
			
			// Save completed quests
			for (CompletedQuest cq : quester.getCompletedQuests()) {
				insert.addBatch("REPLACE INTO quests_completed VALUES('"
						+ p.getUniqueId() + "','" + cq.getQuest().getKey() + "'," + cq.getStage()
						+ ",'" + (cq.isSuccess() ? "1" : "0") + "');");
			}
			
			// Save active questlines
			for (Questline ql : quester.getActiveQuestlines()) {
				insert.addBatch("REPLACE INTO quests_questlines VALUES ('"
						+ p.getUniqueId() + "','" + ql.getKey() + "');");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Quests failed to save quest data for user " + p.getName());
			e.printStackTrace();
		}
		*/
	}
	
	@Override
	// No save-all needed, it happens on player logout anyway
	public void cleanup(Statement insert, Statement delete) {	}

	@Override
	public String getKey() {
		return "QuestsManager";
	}

	@Override
	public void reload() {
		try {
			NeoCore.loadFiles(new File(data, "quests"), questsLoader);
			NeoCore.loadFiles(new File(data, "questlines"), questlinesLoader);
			NeoCore.loadFiles(new File(data, "recommendations.yml"), recommendationsLoader);
		} catch (Exception e) {
			NeoQuests.showWarning("Failed to reload QuestsManager", e);
		}
	}
	
	public static void startQuest(Player p, String quest) {
		Quest q = quests.get(quest.toUpperCase());
		if (q == null) {
			Bukkit.getLogger().warning("[NeoQuests] Failed to start quest " + quest + " for player " + p.getName() + ", quest doesn't exist.");
			return;
		}
		questers.get(p.getUniqueId()).startQuest(q);
	}
	
	public static Quester getQuester(Player p) {
		return questers.get(p.getUniqueId());
	}
	
	public static Quest getQuest(String quest) {
		return quests.get(quest.toUpperCase());
	}
	
	public static File getDataFolder() {
		return data;
	}

	@Override
	public void cleanup() {	}
}
