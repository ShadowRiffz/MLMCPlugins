package me.neoblade298.neopprs;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static HashMap<String, PPR> pprs;
	static HashMap<String, String> uuids;
	static HashMap<String, Boolean> isModifying;
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
			ResultSet rs = stmt.executeQuery("select MAX(id) from neopprs_pprs");
			while (rs.next()) {
				nextPPR = rs.getInt(1) + 1;
			}
			rs = stmt.executeQuery("select MAX(id) from neopprs_alts");
			while (rs.next()) {
				nextAlt = rs.getInt(1) + 1;
			}
			con.close();
		}
		catch(Exception e) {
			getServer().getPluginManager().disablePlugin(this);
			System.out.println(e);
		}  
		
	    // Initiate hashmap
		pprs = new HashMap<String, PPR>();
		uuids = new HashMap<String, String>();
	    
	    // Get command listener
	    this.getCommand("ppr").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPPRs Disabled");
	    super.onDisable();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!uuids.containsKey(e.getPlayer().getName())) {
			String name = e.getPlayer().getName();
			uuids.put(name, e.getPlayer().getUniqueId().toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void viewPlayer(Player viewer, String user) {
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			
			// Get UUID of user
			String uuid = null;
			if (uuids.containsKey(user)) {
				uuids.get(user);
			}
			else {
				uuid = Bukkit.getServer().getOfflinePlayer(user).getUniqueId().toString();
			}
			
			// Get all alt accounts together
			ArrayList<String> accounts = new ArrayList<String>();
			accounts.add(uuid);
			ResultSet rs = stmt.executeQuery("SELECT * FROM neopprs_alts WHERE uuid = '" + uuid + "';");
			while (rs.next()) {
				accounts.add(rs.getString(6));
			}
			
			// Show all relevant PPRs
			for (String account : accounts) {
				rs = stmt.executeQuery("SELECT * FROM neopprs_pprs WHERE uuid = '" + account + "';");
				while (rs.next()) {
					PPR temp = new PPR(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
					temp.show(viewer);
				}
			}
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
			viewer.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
		}
	}
	
}
