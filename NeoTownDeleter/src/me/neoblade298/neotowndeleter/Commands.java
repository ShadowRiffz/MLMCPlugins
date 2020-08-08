package me.neoblade298.neotowndeleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;

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
			if (this.main.deletableTowns.size() > 0) {
				String msg = "§4[§c§lMLMC§4] §7The following towns can be deleted: §e";
				for (Town town : main.deletableTowns) {
					msg += town.getName() + " ";
				}
				p.sendMessage(msg);
			}
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("confirm") && p.hasPermission("tdeleter.admin")) {
			if (this.main.deletableTowns.size() > 0) {
				this.main.deleteTowns();
				return true;
			}
			else {
				p.sendMessage("§4[§c§lMLMC§4] §7No towns can currently be deleted! Try §e/tdelete sweep§7?");
				return true;
			}
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("list") && p.hasPermission("tdeleter.admin")) {
			String msg = "§4[§c§lMLMC§4] §7The following towns can be deleted: §e";
			for (Town town : main.deletableTowns) {
				msg += town.getName() + " ";
			}
			p.sendMessage(msg);
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("debug") && p.hasPermission("tdeleter.admin")) {
			main.debug = !main.debug;
			String msg = "§4[§c§lMLMC§4] §7Debug is now §e" + main.debug;
			p.sendMessage(msg);
			return true;
		}
		else if (args.length == 2 && args[0].equalsIgnoreCase("check") && p.hasPermission("tdeleter.admin")) {
			try {
				Town town = TownyAPI.getInstance().getDataSource().getTown(args[1]);
				if (main.checkTownInactive(town, p)) {
					p.sendMessage("§4[§c§lMLMC§4] §7Town can be deleted.");
				}
			} catch (NotRegisteredException e) {
				String error = "§4[§c§lMLMC§4] §7Town not registered.";
				p.sendMessage(error);
			}
			return true;
		}
		else if (args.length == 1 && p.hasPermission("tdeleter.admin")) {
			try {
				Town town = TownyAPI.getInstance().getDataSource().getTown(args[0]);
				if (main.checkTownInactive(town, p)) {
					try {
						town.getAccount().setBalance(0, "Town deleted");
					} catch (EconomyException e) {
						Bukkit.getServer().getLogger().info("NeoTownDeleter failed to remove town money");
						e.printStackTrace();
					}
					TownyAPI.getInstance().getDataSource().removeTown(town);
					main.deletableTowns.remove(town);
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
