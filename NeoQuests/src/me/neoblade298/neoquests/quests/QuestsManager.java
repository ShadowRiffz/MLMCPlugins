package me.neoblade298.neoquests.quests;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
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
	private HashMap<Player, Quester> questers = new HashMap<Player, Quester>();
	private HashMap<String, Quest> quests = new HashMap<String, Quest>();
	private static FileLoader questsLoader;
	
	static {
		questsLoader = (cfg) -> {
			
		};
	}

	public QuestsManager() {
		// IOListener.register(NeoQuests.inst(), this);
		try {
			FileReader.loadRecursive(new File(NeoQuests.inst().getDataFolder(), "quests"), questsLoader);
		} catch (NeoIOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quests_users WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				// TODO: Set up user
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
}
