package me.Neoblade298.NeoProfessions.PlayerProfessions;

import java.sql.Connection;
import java.sql.DriverManager;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Neoblade298.NeoProfessions.Professions;

public class ProfessionManager {
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
	
	public static void loadPlayer(OfflinePlayer p) {
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
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM professions_accounts WHERE UUID = '" + p.getUniqueId() + "';");
			boolean sqlExists = false;
			while (rs.next()) {
				sqlExists = true;
				String prof = rs.getString(2);
				profs.get(prof).setLevel(rs.getInt(3));
				profs.get(prof).setExp(rs.getInt(4));
			}

			if (!sqlExists) {
				// User does not exist on sql
				for (String prof : profNames) {
					stmt.addBatch("INSERT INTO professions_accounts "
							+ "VALUES ('" + p.getUniqueId() + "', '" + prof + "', 1, 0);");
				}
				stmt.executeBatch();
			}
			con.close();
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init professions for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void savePlayer(Player p, Connection con, Statement stmt, boolean savingMultiple) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		try {
			for (Entry<String, Profession> entry : accounts.get(uuid).entrySet()) {
				Profession prof = entry.getValue();
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + entry.getKey() + "'," + prof.getLevel() + "," +
						prof.getExp()  +");");
			}
			
			// Set to true if you're saving several accounts at once
			if (!savingMultiple) {
					stmt.executeBatch();
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save professions for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				savePlayer(p, con, stmt, true);
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();

		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				if (accounts.containsKey(uuid)) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
						Statement stmt = con.createStatement();

						// Save account
						savePlayer(p, con, stmt, false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		save.runTaskAsynchronously(main);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(e.getUniqueId());
		loadPlayer(p);
	}
}
