package me.neoblade298.neobossinstances;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Commands implements CommandExecutor {
	private Main main = null;

	public Commands(Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
	    if(sender.hasPermission("bossinstances.admin")) {
	    	// /boss tp player nameofboss
	    	if (args.length == 3 && args[0].equalsIgnoreCase("tp") && !main.isInstance) {
	    		boolean found = false;
	    		// Find available instance
	    		try {
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					ResultSet rs;
					
					for (String instance : main.instanceNames) {
						rs = stmt.executeQuery("SELECT * FROM neobossinstances_fights WHERE boss = '" + args[2] + "' AND instance = '" + instance + "';");
						if (!rs.next()) {
							found = true;
				    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bungeee send " + args[1] + " " + instance);
				    		
				    		// Wait for everyone to enter, then update sql
				    		BukkitRunnable addSql = new BukkitRunnable() {
				    			public void run() {
				    				try {
				    					// Connect
				    					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				    					Statement stmt = con.createStatement();
				    					
				    					String uuid = Bukkit.getPlayer(args[1]).getUniqueId().toString();

	    								stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + args[2] + "','" + instance + "',0)");
				    					con.close();
				    				}
				    				catch (Exception e) {
				    					e.printStackTrace();
				    				}
				    			}
				    		};
				    		addSql.runTaskLater(main, 60L);
							break;
						}
					}
	    		}
	    		catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		if (!found) {
	    			Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lMLMC§4] §7No available instances found!");
	    		}
	    		return true;
	    	}
	    }
	    else {
			return false;
	    }
		return false;
	}
}
