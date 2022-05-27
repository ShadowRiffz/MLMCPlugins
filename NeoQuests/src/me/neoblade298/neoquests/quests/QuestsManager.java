package me.neoblade298.neoquests.quests;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.FileReader;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;

public class QuestsManager implements IOComponent, Reloadable {
	private static HashMap<UUID, Quester> questers = new HashMap<UUID, Quester>();
	private static HashMap<String, Quest> quests = new HashMap<String, Quest>();
	private static FileLoader questsLoader;
	
	static {
		questsLoader = (cfg) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					quests.put(key.toUpperCase(), new Quest(key, cfg.getConfigurationSection(key)));
				} catch (NeoIOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public QuestsManager() throws NeoIOException {
		// IOListener.register(NeoQuests.inst(), this);
		reload();
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quests_users WHERE UUID = '" + p.getUniqueId() + "';");
			Quester quester = new Quester(p.getUniqueId());
			questers.put(p.getUniqueId(), quester);
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
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Quests failed to load or init quest data for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		try {
			Quester quester = questers.get(p);
			quester.hashCode();
			// TODO: Save user
			stmt.addBatch("REPLACE INTO quests_quests "
					+ "VALUES ()");
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Quests failed to save quest data for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	@Override
	public void cleanup(Statement stmt) {}

	@Override
	public String getKey() {
		return "QuestsManager";
	}

	@Override
	public void reload() throws NeoIOException {
		try {
			FileReader.loadRecursive(new File(NeoQuests.inst().getDataFolder(), "quests"), questsLoader);
		} catch (NeoIOException e) {
			e.printStackTrace();
		}
	}
	
	public static void startQuest(Player p, String quest) {
		Quest q = quests.get(quest.toUpperCase());
		if (q == null) {
			Bukkit.getLogger().warning("[NeoQuests] Failed to start quest " + quest + " for player " + p.getName() + ", quest doesn't exist.");
			return;
		}
	}
}
