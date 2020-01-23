package me.Neoblade298.NeoProfessions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.neoblade298.neoreports.Main;
import me.neoblade298.neoreports.Report;

public class CurrencyManager {
	// UUID, essence/oretype, amount
	public HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> currencies;
	public static String[] types = {"essence", "ruby", "amethyst", "sapphire", "topaz", "garnet", "adamantium"};
	
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
				String result = rs.getString(i);
			}
		}
		else {
			// User does not exist on sql
			for (String type : types) {
				HashMap<Integer, Integer> typeCurrencies = new HashMap<Integer, Integer>();
				playerCurrencies.put(type, typeCurrencies);
				for (int i = 5; i <= 60; i += 50) {
					typeCurrencies.put(i, 0);
				}
			}
		}
		con.close();
	}
	
	public void cleanup() {
		
	}
}
