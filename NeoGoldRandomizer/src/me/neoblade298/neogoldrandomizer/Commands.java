package me.neoblade298.neogoldrandomizer;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	Main main;

	public Commands(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.isOp()) {
			if (args.length == 3) {

				// First check number of players in the instance
				ArrayList<Player> players = main.nbi.getActiveFights().get(args[0]);

				// Get gold min and max for party size, generate gold
				double bonus = 0.9 + (players.size() * 0.1);
				double min = (Integer.parseInt(args[1]) / players.size()) * bonus;
				double max = (Integer.parseInt(args[2]) / players.size()) * bonus;
				double gold = Math.random() * (max - min) + min;
				gold = Math.round(gold / 25.0D) * 25L;
				
				// Give gold to each player
				for (Player player : players) {
					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					String command = "eco give " + player.getName() + " " + (int) gold;
					Bukkit.dispatchCommand(console, command);
					player.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + (int) gold + " §7gold!");
				}
				return true;
			}
		}
		return false;
	}
}
