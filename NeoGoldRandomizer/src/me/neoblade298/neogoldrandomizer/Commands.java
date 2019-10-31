package me.neoblade298.neogoldrandomizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Commands implements CommandExecutor {
	Main main;

	public Commands(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.isOp()) {
			if (args.length == 4) {

				// First check number of players in the instance
				ArrayList<String> players = new ArrayList<String>();
				try {
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					ResultSet rs;

					// Find available instance
					rs = stmt.executeQuery("SELECT * FROM neobossinstances_fights WHERE boss = '" + args[1]
							+ "' AND instance = '" + Main.instanceName + "';");

					while (rs.next()) {
						players.add(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Get gold min and max for party size
				double bonus = 0.9 + (players.size() * 0.1);
				double min = Integer.parseInt(args[1]) / players.size() * bonus;
				double max = Integer.parseInt(args[2]) / players.size() * bonus;
				
				// Generate gold for each player
				for (String player : players) {
					double gold = Math.random() * (max - min) + min;
					gold = Math.round(gold / 25.0D) * 25L;
					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					String command = "sync console MLMC eco give " + player + " " + (int) gold;
					Bukkit.dispatchCommand(console, command);
					Bukkit.getPlayer(player).sendMessage("§4[§c§lMLMC§4] §7You gained §e" + (int) gold + " §7gold!");
				}
				return true;
			}
		}
		return false;
	}
}
