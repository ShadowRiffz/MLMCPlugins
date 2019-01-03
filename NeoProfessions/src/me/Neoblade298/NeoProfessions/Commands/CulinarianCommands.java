package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Methods.CulinarianMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class CulinarianCommands implements CommandExecutor {
	
	Main main;
	CulinarianMethods culinarianMethods;
	
	public CulinarianCommands(Main main) {
		this.main = main;
		culinarianMethods = main.culinarianMethods;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("culinarian.professed") && sender instanceof Player) {
			Player p = (Player) sender;
			
			if(args.length == 0) {
				//TODO help command
			}
			else {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("garnish")) {
						culinarianMethods.garnish(p);
						return true;
					}
					else if(args[0].equalsIgnoreCase("preserve")) {
						culinarianMethods.preserve(p);
						return true;
					}
					else if(args[0].equalsIgnoreCase("spice")) {
						culinarianMethods.spice(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cInvalid sub-command!");
						return true;
					}
				}
				else {
					Util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
		}
		else {
			Util.sendMessage((Player) sender, "&cYou are not a culinarian!");
			return true;
		}
		return false;
	}

}
