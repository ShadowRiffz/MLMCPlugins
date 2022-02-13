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
	private HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> currencies;
	public static String[] types = {"essence", "ruby", "amethyst", "sapphire", "emerald", "topaz", "garnet", "adamantium"};
	
	public CurrencyManager(Professions main) {
		currencies = new HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>>();
	}
	
	public void initPlayer(Player p) throws Exception {
		// Check if player exists already
		if (currencies.containsKey(p.getUniqueId())) {
			return;
		}
		
		HashMap<String, HashMap<Integer, Integer>> playerCurrencies = new HashMap<String, HashMap<Integer, Integer>>();
		currencies.put(p.getUniqueId(), playerCurrencies);
		
		// Check if player exists on SQL
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
		Statement stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery("SELECT * FROM neoprofessions_currency WHERE UUID = '" + p.getUniqueId() + "';");
		if (rs.next()) {
			for (int i = 2; i <= 9; i++) {
				String type = types[i-2];
				HashMap<Integer, Integer> typeCurrencies = new HashMap<Integer, Integer>();
				playerCurrencies.put(type, typeCurrencies);
				String vals[] = rs.getString(i).split(":");
				for (int j = 5; j <= 60; j += 5) {
					typeCurrencies.put(j, Integer.parseInt(vals[(j/5)-1]));
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
				HashMap<Integer, Integer> typeCurrencies = new HashMap<Integer, Integer>();
				playerCurrencies.put(type, typeCurrencies);
				for (int i = 5; i <= 60; i += 5) {
					typeCurrencies.put(i, 0);
				}
			}
		}
		con.close();
	}
	
	public void savePlayer(Player p) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
		Statement stmt = con.createStatement();
		HashMap<String, HashMap<Integer, Integer>> playerCurrencies = currencies.get(p.getUniqueId());
		for (String type : types) {
			HashMap<Integer, Integer> typeCurrencies = playerCurrencies.get(type.toLowerCase());
			String sqlString = "" + typeCurrencies.get(5);
			for (int i = 10; i <= 60; i += 5) {
				sqlString += ":" + typeCurrencies.get(i);
			}
			stmt.executeUpdate("UPDATE neoprofessions_currency SET " + type + " = '" + sqlString + "' WHERE uuid = '" + p.getUniqueId() + "';");
		}
	}
	
	public void cleanup() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
		Statement stmt = con.createStatement();
		for (UUID uuid : currencies.keySet()) {
			HashMap<String, HashMap<Integer, Integer>> playerCurrencies = currencies.get(uuid);
			for (String type : types) {
				HashMap<Integer, Integer> typeCurrencies = playerCurrencies.get(type.toLowerCase());
				String sqlString = "" + typeCurrencies.get(5);
				for (int i = 10; i <= 60; i += 5) {
					sqlString += ":" + typeCurrencies.get(i);
				}
				stmt.executeUpdate("UPDATE neoprofessions_currency SET " + type + " = '" + sqlString + "' WHERE uuid = '" + uuid + "';");
			}
		}
	}
	
	public void add(Player p, String type, int level, int amount) {
		if (level > 60 || level < 5) {
			return;
		}

		// Standardize the level
		level -= (level % 5);
		if (amount < 0) {
			subtract(p, type, level, -amount);
			return;
		}
		
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type.toLowerCase());
		int newAmount = typeCurrency.get(level) + amount;
		typeCurrency.put(level, newAmount);
		Util.sendMessage(p, "&7You gained &e" + amount + " &cLv " + level + " " + type + "&7. You now have &e" + newAmount + "&7.");
	}
	
	public void subtract(Player p, String type, int level, int amount) {
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type.toLowerCase());
		int newAmount = typeCurrency.get(level) - amount;
		typeCurrency.put(level, newAmount);
		Util.sendMessage(p, "&7You lost &e" + amount + " &cLv " + level + " " + type + "&7. You now have &e" + newAmount + "&7.");
	}
	
	public int get(Player p, String type, int level) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return -1;
		}
		return currencies.get(p.getUniqueId()).get(type.toLowerCase()).get(level);
	}
	
	public boolean hasEnough(Player p, String type, int level, int compare) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return false;
		}
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type.toLowerCase());
		return typeCurrency.get(level) >= compare;
	}
	
	public boolean validType(String type) {
		for (String validType : types) {
			if (validType.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsKey(String key) {
		for (String type : types) {
			if (type.equalsIgnoreCase(key)) return true;
		}
		return false;
	}
	
	public boolean containsPlayer(Player p) {
		if (currencies.containsKey(p.getUniqueId())) {
			return true;
		}
		return false;
	}
}