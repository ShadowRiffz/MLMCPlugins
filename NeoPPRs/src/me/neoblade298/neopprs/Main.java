package me.neoblade298.neopprs;

import java.util.HashMap;
import java.sql.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static HashMap<String, PPR> pprs;
	static int nextPPR;
	static int nextAlt;
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC";
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPPRs Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		// Get next available IDs from SQL
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
		}
		catch(Exception e) {
			getServer().getPluginManager().disablePlugin(this);
			System.out.println(e);
		}  
		
	    // Initiate hashmap
		pprs = new HashMap<String, PPR>();
		
		System.out.println("Test");
		System.out.println(this);
		System.out.println(this.getCommand("ppr"));
		System.out.println(new Commands(this));
		System.out.println("Test2");
	    
	    // Get command listener
	    this.getCommand("ppr").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
}
