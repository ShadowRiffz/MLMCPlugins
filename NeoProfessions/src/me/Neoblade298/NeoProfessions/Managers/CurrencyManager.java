package me.Neoblade298.NeoProfessions.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class CurrencyManager implements IOComponent {
	// UUID, essence/oretype, amount
	Professions main;
	
	private static HashMap<UUID, HashMap<Integer, Integer>> essence;
	static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	
	public CurrencyManager(Professions main) {
		this.main = main;
		essence = new HashMap<UUID, HashMap<Integer, Integer>>();
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p) {
		// Check if player exists already
		if (essence.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<Integer, Integer> essences = new HashMap<Integer, Integer>();
		essence.put(p.getUniqueId(), essences);
		
		// Check if player exists on SQL
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM professions_currency WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				essences.put(rs.getInt(2), rs.getInt(3));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init currency for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Connection con, Statement stmt, boolean savingMultiple) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		if (!essence.containsKey(p.getUniqueId())) {
			return;
		}
		
		try {
			for (Entry<Integer, Integer> entry : essence.get(uuid).entrySet()) {
				if (entry.getValue() == 0) {
					continue;
				}
				stmt.addBatch("REPLACE INTO professions_essence "
						+ "VALUES ('" + uuid + "', " + entry.getKey() + "," + entry.getValue() + ");");
			}
			
			// Set to true if you're saving several users at once
			if (!savingMultiple) {
				stmt.executeBatch();
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save currency for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void saveAll() {
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
	
	public static void add(Player p, int level, int amount) {
		// Standardize the level
		level -= (level % 5);
		
		if (level > 60 || level < 5) {
			return;
		}
		
		if (amount < 0) {
			subtract(p, level, -amount);
			return;
		}
		
		HashMap<Integer, Integer> essences = essence.get(p.getUniqueId());
		int newAmount = essences.getOrDefault(level, 0) + amount;
		essences.put(level, newAmount);
		Util.sendMessage(p, "&a+" + amount + " &7(§f" + newAmount + "§7) §6Lv " + level + " §7Essence.");
	}
	
	public static void subtract(Player p, int level, int amount) {
		HashMap<Integer, Integer> essences = essence.get(p.getUniqueId());
		int newAmount = essences.getOrDefault(level, 0) - amount;
		essences.put(level, newAmount);
		Util.sendMessage(p, "&c-" + amount + " &7(§f" + newAmount + "§7) §6Lv " + level + " §7Essence.");
	}
	
	public static int get(Player p, int level) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return -1;
		}
		return essence.get(p.getUniqueId()).getOrDefault(level, 0);
	}
	
	public static boolean hasEnough(Player p, int level, int compare) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return false;
		}
		HashMap<Integer, Integer> pCurrency = essence.get(p.getUniqueId());
		return pCurrency.getOrDefault(level, 0) >= compare;
	}
}