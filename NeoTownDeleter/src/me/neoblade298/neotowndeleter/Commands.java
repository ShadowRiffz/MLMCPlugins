package me.neoblade298.neotowndeleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class Commands implements CommandExecutor {
	
	Main main;
	
	public Commands (Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("sweep") && p.hasPermission("tdeleter.admin")) {
			main.sweepTowns();
			return true;
		}
		else if (args.length == 1 && p.hasPermission("tdeleter.admin")) {
			try {
				Town town = TownyUniverse.getDataSource().getTown(args[0]);
				if (main.checkTownInactive(town, p)) {
					TownyUniverse.getDataSource().removeTown(town);
					Bukkit.broadcastMessage("§bThe town of " + town.getName() + " fell into ruin due to inactivity!");
				}
			}
			catch (Exception e) {
				String error = "§4[§c§lMLMC§4] §7Town not registered.";
				p.sendMessage(error);
			}
			return true;
		}
		else if (!p.hasPermission("tdeleter.admin")) {
			p.sendMessage("You do not have the appropriate permissions.");
			return true;
		}
		return false;
	}
}
