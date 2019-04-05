package me.neoblade298.neopprs;

import java.util.HashMap;
import java.sql.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static HashMap<String, PPR> ppr;
	static int nextPPR;
	static int nextAlt;
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://localhost:3306";
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPPRs Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		// Connect to MYSQL
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(connection, sqlUser, sqlPass);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from neopprs_next");
			while (rs.next()) {
				if (rs.getString(1).equals("pprs")) {
					nextPPR = rs.getInt(2);
				}
				else if (rs.getString(1).equals("alts")) {
					nextAlt = rs.getInt(2);
				}
			}
			con.close();
			System.out.println("Next PPR: " + nextPPR + ", next alt: " + nextAlt);
		}
		catch(Exception e) {
			System.out.println(e);
			onDisable();
		}  
		
	    // Initiate hashmap
		ppr = new HashMap<String, PPR>();
		
		// Get next available id from sql
	    
	    // Get command listener
	    this.getCommand("ppr").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
	public void createPPR(String author) {
		ppr.put(author, new PPR(nextPPR, author));
	}
	
}
