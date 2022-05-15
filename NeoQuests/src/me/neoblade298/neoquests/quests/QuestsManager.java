package me.neoblade298.neoquests.quests;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import me.neoblade298.neocore.io.IOComponent;

public class QuestsManager implements IOComponent {
	private HashMap<Player, Quester> questers;

	public QuestsManager() {
		// IOListener.register(NeoQuests.inst(), this);
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
