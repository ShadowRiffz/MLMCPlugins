package me.Neoblade298.NeoProfessions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CurrencyManager {
	// UUID, essence/oretype, amount
	private HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> currencies;
	private static String[] types = {"essence", "ruby", "amethyst", "sapphire", "topaz", "garnet", "adamantium"};
	
	public CurrencyManager(Main main) {
		currencies = new HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>>();
		
	}
	
	public void initPlayer(Player p) throws Exception {
		HashMap<String, HashMap<Integer, Integer>> playerCurrencies = new HashMap<String, HashMap<Integer, Integer>>();
		currencies.put(p.getUniqueId(), playerCurrencies);
		
		// Check if player exists on SQL
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
		Statement stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery("SELECT * FROM neoprofessions_currency WHERE UUID = '" + p.getUniqueId() + "';");
		if (rs.next()) {
			for (int i = 2; i <= 9; i++) {
				String type = types[i-2];
				HashMap<Integer, Integer> typeCurrencies = new HashMap<Integer, Integer>();
				playerCurrencies.put(type, typeCurrencies);
				String vals[] = rs.getString(i).split(":");
				for (int j = 5; i <= 60; i += 5) {
					typeCurrencies.put(j, Integer.parseInt(vals[i]));
				}
			}
		}
		else {
			// User does not exist on sql
			String init = "'0:0:0:0:0:0:0:0:0:0:0:0'";
			for (int i = 1; i < types.length; i++) {
				init += ", '0:0:0:0:0:0:0:0:0:0:0:0'";
			}
			int post = stmt.executeUpdate("INSERT INTO neoprofessions_currency "
					+ "(`uuid`, `essence`, `ruby`, `amethyst`, `sapphire`, `emerald`, `topaz`, `garnet`, `adamantium`) "
					+ "VALUES ('" + p.getUniqueId() + "', " + init + ");");
			for (String type : types) {
				HashMap<Integer, Integer> typeCurrencies = new HashMap<Integer, Integer>();
				playerCurrencies.put(type, typeCurrencies);
				for (int i = 5; i <= 60; i += 5) {
					typeCurrencies.put(i, 0);
				}
			}
			if (post == 0) {
				throw new Exception("Failed to post");
			}
		}
		con.close();
	}
	
	public void cleanup() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
		Statement stmt = con.createStatement();
		for (UUID uuid : currencies.keySet()) {
			HashMap<String, HashMap<Integer, Integer>> playerCurrencies = currencies.get(uuid);
			for (String type : types) {
				HashMap<Integer, Integer> typeCurrencies = playerCurrencies.get(type);
				String sqlString = "" + typeCurrencies.get(5);
				for (int i = 10; i <= 60; i += 5) {
					sqlString = ":" + typeCurrencies.get(i);
				}
				int post = stmt.executeUpdate("UPDATE neoprofessions_currency SET '" + type + "' = '" + sqlString + "' WHERE uuid = '" + uuid + "');");
				if (post == 0) {
					throw new Exception("Failed to update");
				}
			}
		}
	}
	
	public void add(Player p, String type, int level, int amount) {
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type);
		int newAmount = typeCurrency.get(level) + amount;
		typeCurrency.put(level, newAmount);
	}
	
	public void subtract(Player p, String type, int level, int amount) {
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type);
		int newAmount = typeCurrency.get(level) - amount;
		typeCurrency.put(level, newAmount);
	}
	
	public boolean hasEnough(Player p, String type, int level, int compare) {
		HashMap<Integer, Integer> typeCurrency = currencies.get(p.getUniqueId()).get(type);
		return typeCurrency.get(level) >= compare;
	}
}
