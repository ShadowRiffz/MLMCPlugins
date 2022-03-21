package me.Neoblade298.NeoProfessions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class CurrencyManager {
	// UUID, essence/oretype, amount
	private static HashMap<UUID, HashMap<Integer, Integer>> essence;
	
	public CurrencyManager(Professions main) {
		essence = new HashMap<UUID, HashMap<Integer, Integer>>();
	}
	
	public void initPlayer(Player p) throws Exception {
		// Check if player exists already
		if (essence.containsKey(p.getUniqueId())) {
			return;
		}
		
		HashMap<String, HashMap<Integer, Integer>> playeressence = new HashMap<String, HashMap<Integer, Integer>>();
		essence.put(p.getUniqueId(), playeressence);
		
		// Check if player exists on SQL
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
		Statement stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery("SELECT * FROM neoprofessions_currency WHERE UUID = '" + p.getUniqueId() + "';");
		if (rs.next()) {
			for (int i = 2; i <= 9; i++) {
				String type = types[i-2];
				HashMap<Integer, Integer> typeessence = new HashMap<Integer, Integer>();
				playeressence.put(type, typeessence);
				String vals[] = rs.getString(i).split(":");
				for (int j = 5; j <= 60; j += 5) {
					typeessence.put(j, Integer.parseInt(vals[(j/5)-1]));
				}
			}
		}
		else {
			// User does not exist on sql
			String init = "'0:0:0:0:0:0:0:0:0:0:0:0'";
			for (int i = 1; i < types.length; i++) {
				init += ", '0:0:0:0:0:0:0:0:0:0:0:0'";
			}
			stmt.executeUpdate("INSERT INTO neoprofessions_currency "
					+ "(`uuid`, `essence`, `ruby`, `amethyst`, `sapphire`, `emerald`, `topaz`, `garnet`, `adamantium`) "
					+ "VALUES ('" + p.getUniqueId() + "', " + init + ");");
			for (String type : types) {
				HashMap<Integer, Integer> typeessence = new HashMap<Integer, Integer>();
				playeressence.put(type, typeessence);
				for (int i = 5; i <= 60; i += 5) {
					typeessence.put(i, 0);
				}
			}
		}
		con.close();
	}
	
	public void savePlayer(Player p) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
		Statement stmt = con.createStatement();
		HashMap<String, HashMap<Integer, Integer>> playeressence = essence.get(p.getUniqueId());
		for (String type : types) {
			HashMap<Integer, Integer> typeessence = playeressence.get(type.toLowerCase());
			String sqlString = "" + typeessence.get(5);
			for (int i = 10; i <= 60; i += 5) {
				sqlString += ":" + typeessence.get(i);
			}
			stmt.executeUpdate("UPDATE neoprofessions_currency SET " + type + " = '" + sqlString + "' WHERE uuid = '" + p.getUniqueId() + "';");
		}
	}
	
	public void cleanup() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
		Statement stmt = con.createStatement();
		for (UUID uuid : essence.keySet()) {
			HashMap<String, HashMap<Integer, Integer>> playeressence = essence.get(uuid);
			for (String type : types) {
				HashMap<Integer, Integer> typeessence = playeressence.get(type.toLowerCase());
				String sqlString = "" + typeessence.get(5);
				for (int i = 10; i <= 60; i += 5) {
					sqlString += ":" + typeessence.get(i);
				}
				stmt.executeUpdate("UPDATE neoprofessions_currency SET " + type + " = '" + sqlString + "' WHERE uuid = '" + uuid + "';");
			}
		}
	}
	
	public static void add(Player p, String type, int level, int amount) {
		// Standardize the level
		level -= (level % 5);
		
		if (level > 60 || level < 5) {
			return;
		}
		
		if (amount < 0) {
			subtract(p, type, level, -amount);
			return;
		}
		
		HashMap<Integer, Integer> typeCurrency = essence.get(p.getUniqueId()).get(type.toLowerCase());
		int newAmount = typeCurrency.get(level) + amount;
		typeCurrency.put(level, newAmount);
		Util.sendMessage(p, "&7You gained &e" + amount + " &cLv " + level + " " + type + "&7. You now have &e" + newAmount + "&7.");
	}
	
	public static void subtract(Player p, int level, int amount) {
		HashMap<Integer, Integer> typeCurrency = essence.get(p.getUniqueId()).get(type.toLowerCase());
		int newAmount = typeCurrency.get(level) - amount;
		typeCurrency.put(level, newAmount);
		Util.sendMessage(p, "&7You lost &e" + amount + " &cLv " + level + " " + type + "&7. You now have &e" + newAmount + "&7.");
	}
	
	public static int get(Player p, int level) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return -1;
		}
		return essence.get(p.getUniqueId()).get(level);
	}
	
	public static boolean hasEnough(Player p, int level, int compare) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return false;
		}
		HashMap<Integer, Integer> pCurrency = essence.get(p.getUniqueId());
		return pCurrency.get(level) >= compare;
	}
	
	public boolean containsPlayer(Player p) {
		if (essence.containsKey(p.getUniqueId())) {
			return true;
		}
		return false;
	}
}