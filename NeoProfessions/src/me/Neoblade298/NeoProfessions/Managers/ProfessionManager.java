package me.Neoblade298.NeoProfessions.Managers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;

public class ProfessionManager implements IOComponent {
	static Professions main;
	
	static HashMap<UUID, HashMap<String, Profession>> accounts = new HashMap<UUID, HashMap<String, Profession>>();
	static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	static ArrayList<String> profNames = new ArrayList<String>();
	
	static {
		profNames.add("stonecutter");
		profNames.add("logger");
		profNames.add("harvester");
		profNames.add("crafter");
	}
	
	public ProfessionManager(Professions main) {
		ProfessionManager.main = main;
	}
	
	public int getLevel(Player p, String prof) {
		if (accounts.containsKey(p.getUniqueId())) {
			return accounts.get(p.getUniqueId()).get(prof).getLevel();
		}
		return -1;
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (accounts.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<String, Profession> profs = new HashMap<String, Profession>();
		for (String prof : profNames) {
			profs.put(prof, new Profession(prof));
		}
		accounts.put(p.getUniqueId(), profs);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_accounts WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				String prof = rs.getString(2);
				profs.get(prof).setLevel(rs.getInt(3));
				profs.get(prof).setExp(rs.getInt(4));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init professions for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		if (!accounts.containsKey(p.getUniqueId())) {
			return;
		}
		
		try {
			for (Entry<String, Profession> entry : accounts.get(uuid).entrySet()) {
				Profession prof = entry.getValue();
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + entry.getKey() + "'," + prof.getLevel() + "," +
						prof.getExp()  +");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save professions for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Profession> getAccount(UUID uuid) {
		return accounts.get(uuid);
	}
}
