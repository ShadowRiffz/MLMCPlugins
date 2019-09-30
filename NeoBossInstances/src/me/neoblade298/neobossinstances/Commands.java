package me.neoblade298.neobossinstances;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Commands implements CommandExecutor {
	private Main main = null;

	public Commands(Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
	    if(sender.hasPermission("bossinstances.admin") || sender.isOp()) {
	    	// /boss tp player nameofboss
	    	if (args.length == 3 && args[0].equalsIgnoreCase("tp") && !main.isInstance) {
	    		boolean found = false;
	    		try {
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					ResultSet rs;

		    		// Find available instance
					for (String instance : main.instanceNames) {
						rs = stmt.executeQuery("SELECT * FROM neobossinstances_fights WHERE boss = '" + args[2] + "' AND instance = '" + instance + "';");
						if (!rs.next()) {
							found = true;
	    					String uuid = Bukkit.getPlayer(args[1]).getUniqueId().toString();
				    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bungeee send " + args[1] + " " + instance);
				    		
				    		// Wait for everyone to enter, then update sql so the instance still shows as empty until everyone leaves
				    		BukkitRunnable addSql = new BukkitRunnable() {
				    			public void run() {
				    				try {
				    					// Connect
				    					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				    					Statement stmt = con.createStatement();
				    					

	    								stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + args[2] + "','" + instance + "');");
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
	    			Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7No available instances!");
	    		}
	    		return true;
	    	}
	    }
	    if (args.length == 0) {
			sender.sendMessage("§4[§c§lBosses§4]");
			sender.sendMessage("§c/boss cd [name] §7- Shows cooldown of a specific boss");
			sender.sendMessage("§c/boss cd all §7- Shows cooldown of all bosses");
			sender.sendMessage("§c/boss instances [name] §7- Shows instances for boss");
			return true;
	    }
	    else if (args.length == 2 && args[0].equalsIgnoreCase("cd") && sender instanceof Player) {
	    	Player p = (Player) sender;
	    	String name = WordUtils.capitalize(args[1]);
	    	if (name.equalsIgnoreCase("all")) {
	    		for (String boss : main.cooldowns.keySet()) {
	    			main.getCooldown(boss, p);
	    		}
	    	}
	    	else {
	    		main.getCooldown(name, p);
	    	}
	    }
		return false;
	}
}
