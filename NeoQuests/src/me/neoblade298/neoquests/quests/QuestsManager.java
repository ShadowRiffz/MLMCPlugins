package me.neoblade298.neoquests.quests;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;

public class QuestsManager implements IOComponent, Manager {
	private static HashMap<UUID, HashMap<Integer, Quester>> questers = new HashMap<UUID, HashMap<Integer, Quester>>();
	private static HashMap<String, Quest> quests = new HashMap<String, Quest>();
	private static HashMap<String, Questline> questlines = new HashMap<String, Questline>();
	private static ArrayList<QuestRecommendation> recommendations = new ArrayList<QuestRecommendation>();
	private static ArrayList<QuestRecommendation> challenges = new ArrayList<QuestRecommendation>();
	private static File data = new File(NeoQuests.inst().getDataFolder(), "quests");
	private static FileLoader questsLoader, questlinesLoader, recommendationsLoader, challengesLoader;
	
	static {
		questsLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					if (quests.containsKey(key.toUpperCase())) {
						NeoQuests.showWarning("Duplicate quest " + key + " in file " + file.getPath() + ", " +
								"the loaded quest with this key is in " + quests.get(key.toUpperCase()).getFileLocation());
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
					if (questlines.containsKey(key.toUpperCase())) {
						NeoQuests.showWarning("Duplicate questline " + key + " in file " + file.getPath() + ", " +
								"the loaded questline with this key is in " + questlines.get(key.toUpperCase()).getFileLocation());
						continue;
					}
					questlines.put(key.toUpperCase(), new Questline(cfg.getConfigurationSection(key), file));
				} catch (Exception e) {
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
		
		challengesLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					for (String line : cfg.getStringList(key)) {
						challenges.add(new QuestRecommendation(new LineConfig(line)));
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
		HashMap<Integer, Quester> accts = new HashMap<Integer, Quester>();
		int active = SkillAPI.getPlayerAccountData(p).getActiveId();
		accts.put(SkillAPI.getPlayerAccountData(p).getActiveId(), new Quester(p, active));
		questers.put(p.getUniqueId(), accts);
		try {
			// Completed quests
			ResultSet rs = stmt.executeQuery("SELECT * FROM quests_completed WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				Quester quester = initializeOrGetQuester(p, rs.getInt(2));
				Quest quest = quests.get(rs.getString(3));
				if (quest == null) {
					Bukkit.getLogger().warning("[NeoQuests] Failed to load completed quest for player: " + rs.getString(3));
					continue;
				}
				quester.addCompletedQuest(new CompletedQuest(quest, rs.getInt(4), rs.getBoolean(5)));
			}
			
			// Active questlines
			rs = stmt.executeQuery("SELECT * FROM quests_questlines WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				Quester quester = initializeOrGetQuester(p, rs.getInt(2));
				Questline ql = questlines.get(rs.getString(3).toUpperCase());
				if (ql == null) {
					Bukkit.getLogger().warning("[NeoQuests] Failed to load questline for player: " + rs.getString(3));
					continue;
				}
				quester.addQuestline(ql);
			}
			
			// Account info
			rs = stmt.executeQuery("SELECT * FROM quests_accounts WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				Quester quester = initializeOrGetQuester(p, rs.getInt(2));
				double x = rs.getDouble(3);
				double y = rs.getDouble(4);
				double z = rs.getDouble(5);
				World w = Bukkit.getWorld(rs.getString(6));
				quester.setLocation(new Location(w, x, y, z));
			}
			
			// Active quests
			rs = stmt.executeQuery("SELECT * FROM quests_quests WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				Quester quester = initializeOrGetQuester(p, rs.getInt(2));
				
				String qname = rs.getString(3);
				int stage = rs.getInt(4);
				String set = rs.getString(5);
				
				// Parse counts
				String[] scounts = rs.getString(6).split(",");
				ArrayList<Integer> counts = new ArrayList<Integer>(scounts.length);
				for (int i = 0; i < scounts.length; i++) {
					counts.add(Integer.parseInt(scounts[i]));
				}
				
				Quest quest = quests.get(qname);
				if (quest == null) {
					Bukkit.getLogger().warning("[NeoQuests] Failed to load active quest for player: " + qname);
					continue;
				}
				QuestInstance qi = quester.getActiveQuestsHashMap().getOrDefault(qname, new QuestInstance(quester, quest, stage));
				quester.addActiveQuest(qi);
				qi.setupInstances(false); // Only start listening to the main account (in the finally clause)
				qi.getObjectiveSetInstance(set).setObjectiveCounts(counts);
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("Quests failed to load or init quest data for user " + p.getName());
			e.printStackTrace();
		}
		// Regardless of if we load in from sql, we need to initialize the proper quests
		finally {
			QuestsManager.initializeOrGetQuester(p).startListening();
		}
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		// Save player location if they're in quest world
		if (SkillAPI.getSettings().isWorldEnabled(p.getWorld())) {
			initializeOrGetQuester(p).setLocation(p.getLocation());
		}
		
		try {
			for (int acct : questers.get(p.getUniqueId()).keySet()) {
				Quester quester = questers.get(p.getUniqueId()).get(acct);

				// Save user
				for (QuestInstance qi : quester.getActiveQuests()) {
					// Delete existing active quests
					delete.addBatch("DELETE FROM quests_quests WHERE uuid = '" + p.getUniqueId() + "';"); 
					for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
						// Replace with new ones
						insert.addBatch("REPLACE INTO quests_quests VALUES('"
								+ p.getUniqueId() + "'," + acct + ",'" + qi.getQuest().getKey() + "'," + qi.getStage()
								+ ",'" + osi.getKey() + "','" + osi.serializeCounts() + "');");
					}
				}
				
				// Save completed quests
				for (CompletedQuest cq : quester.getCompletedQuests()) {
					insert.addBatch("REPLACE INTO quests_completed VALUES('"
							+ p.getUniqueId() + "'," + acct + ",'" + cq.getQuest().getKey() + "'," + cq.getStage()
							+ ",'" + (cq.isSuccess() ? "1" : "0") + "');");
				}
				
				// Save active questlines
				for (Questline ql : quester.getActiveQuestlines()) {
					insert.addBatch("REPLACE INTO quests_questlines VALUES ('"
							+ p.getUniqueId() + "'," + acct + ",'" + ql.getKey() + "');");
				}
				
				// Save account info
				if (quester.getLocation() != null) {
					Location loc = quester.getLocation();
					double x = Math.round(loc.getX() * 100) / 100;
					double y = Math.round(loc.getY() * 100) / 100;
					double z = Math.round(loc.getZ() * 100) / 100;
					String w = loc.getWorld().getName();
					insert.addBatch("REPLACE INTO quests_accounts VALUES ('"
							+ p.getUniqueId() + "'," + acct + "," + x + "," + y + "," + z + ",'" + w + "');");
				}
			}
			questers.remove(p.getUniqueId());
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("Quests failed to save quest data for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static Quester initializeOrGetQuester(Player p) {
		return initializeOrGetQuester(p, SkillAPI.getPlayerAccountData(p).getActiveId());
	}
	
	public static Quester initializeOrGetQuester(Player p, int acct) {
		HashMap<Integer, Quester> accts = questers.get(p.getUniqueId());
		Quester quester = accts.getOrDefault(acct, new Quester(p, acct));
		accts.putIfAbsent(acct, quester);
		return quester;
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
			quests.clear();
			questlines.clear();
			recommendations.clear();
			NeoCore.loadFiles(new File(data, "quests"), questsLoader);
			NeoCore.loadFiles(new File(data, "questlines"), questlinesLoader);
			NeoCore.loadFiles(new File(data, "recommendations.yml"), recommendationsLoader);
			NeoCore.loadFiles(new File(data, "challenges.yml"), challengesLoader);
			
			for (HashMap<Integer, Quester> pmap : questers.values()) {
				for (Quester quester : pmap.values()) {
					quester.reloadQuests();
				}
			}
		} catch (Exception e) {
			NeoQuests.showWarning("Failed to reload QuestsManager", e);
		}
	}
	
	public static void startQuest(Player p, String quest) {
		startQuest(p, quest, false);
	}
	
	public static void startQuest(Player p, String quest, boolean ignoreConditions) {
		Quest q = quests.get(quest.toUpperCase());
		if (q == null) {
			Bukkit.getLogger().warning("[NeoQuests] Failed to start quest " + quest + " for player " + p.getName() + ", quest doesn't exist.");
			return;
		}
		getQuester(p).startQuest(q, ignoreConditions);
	}
	
	public static Collection<Quester> getAllAccounts(Player p) {
		return questers.get(p.getUniqueId()).values();
	}
	
	public static Quester getQuester(Player p) {
		return getQuester(p, SkillAPI.getPlayerAccountData(p).getActiveId());
	}
	
	public static Quester getQuester(Player p, int account) {
		if (!questers.containsKey(p.getUniqueId())) {
			return null;
		}
		return questers.get(p.getUniqueId()).get(account);
	}
	
	public static Quest getQuest(String quest) {
		return quests.get(quest.toUpperCase());
	}
	
	public static File getDataFolder() {
		return data;
	}
	
	public static ArrayList<QuestRecommendation> getRecommendations() {
		return recommendations;
	}
	
	public static ArrayList<QuestRecommendation> getChallenges() {
		return challenges;
	}
	
	public static Questline getQuestline(String key) {
		return questlines.get(key.toUpperCase());
	}

	@Override
	public void cleanup() {	}
}
