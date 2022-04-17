package me.Neoblade298.NeoProfessions.Managers;

import java.sql.ResultSet;
import java.sql.Statement;
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
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class ProfessionManager implements IOComponent {
	static Professions main;
	
	static HashMap<UUID, HashMap<ProfessionType, Profession>> accounts = new HashMap<UUID, HashMap<ProfessionType, Profession>>();
	
	public ProfessionManager(Professions main) {
		ProfessionManager.main = main;
	}
	
	public static String getDisplay(Player p, ProfessionType prof) {
		if (accounts.containsKey(p.getUniqueId())) {
			return accounts.get(p.getUniqueId()).get(prof).getType().getDisplay();
		}
		return "";
	}
	
	public static int getLevel(Player p, ProfessionType prof) {
		if (accounts.containsKey(p.getUniqueId())) {
			return accounts.get(p.getUniqueId()).get(prof).getLevel();
		}
		return -1;
	}
	
	public static int getExp(Player p, ProfessionType prof) {
		if (accounts.containsKey(p.getUniqueId())) {
			return accounts.get(p.getUniqueId()).get(prof).getExp();
		}
		return -1;
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (accounts.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<ProfessionType, Profession> profs = new HashMap<ProfessionType, Profession>();
		for (ProfessionType prof : ProfessionType.values()) {
			profs.put(prof, new Profession(prof));
		}
		accounts.put(p.getUniqueId(), profs);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_accounts WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				ProfessionType prof = ProfessionType.valueOf(rs.getString(2));
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
			for (Entry<ProfessionType, Profession> entry : accounts.get(uuid).entrySet()) {
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

	@Override
	public void cleanup(Statement stmt) {
		
	}
	
	public static HashMap<ProfessionType, Profession> getAccount(UUID uuid) {
		return accounts.get(uuid);
	}
	
	@Override
	public String getComponentName() {
		return "ProfessionManager";
	}
	
	public static void convertPlayer(UUID uuid, HashMap<ProfessionType, Profession> profs, Statement stmt) {
		try {
			for (Entry<ProfessionType, Profession> entry : profs.entrySet()) {
				Profession prof = entry.getValue();
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + entry.getKey() + "'," + prof.getLevel() + "," +
						prof.getExp()  +");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save professions for uuid " + uuid);
			e.printStackTrace();
		}
	}
}
