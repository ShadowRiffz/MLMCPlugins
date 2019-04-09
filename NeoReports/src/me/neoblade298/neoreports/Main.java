package me.neoblade298.neoreports;

import java.sql.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static int nextReport;
	static int numBugs;
	static int numUrgent;
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPPRs Enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// Initialize static vars
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(connection, sqlUser, sqlPass);
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select MAX(id) from neoreports_reports");
			while (rs.next()) {
				nextReport = rs.getInt(1) + 1;
			}
			rs = stmt.executeQuery("select COUNT(*) from neoreports_reports WHERE is_urgent = 0 AND is_resolved = 0");
			while (rs.next()) {
				numBugs = rs.getInt(1);
			}
			rs = stmt.executeQuery("select COUNT(*) from neoreports_reports WHERE is_urgent = 1 AND is_resolved = 0");
			while (rs.next()) {
				numUrgent = rs.getInt(1);
			}
			con.close();
		}
		catch(Exception e) {
			getServer().getPluginManager().disablePlugin(this);
			System.out.println(e);
		}  
	    
	    // Get command listener
	    this.getCommand("report").setExecutor(new ReportCommand(this));
	    this.getCommand("reports").setExecutor(new ReportsCommand(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
}
