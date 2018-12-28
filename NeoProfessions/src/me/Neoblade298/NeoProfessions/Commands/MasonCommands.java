package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Methods.MasonMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class MasonCommands implements CommandExecutor {
	
	Main main;
	MasonMethods masonMethods;
	
	static final int MAX_SLOTS = 3;
	
	public MasonCommands(Main main) {
		this.main = main;
		this.masonMethods = main.masonMethods;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("mason.professed") && sender instanceof Player) {
			
			Player p = (Player) sender;
			if(args.length == 0) {
			
			}
			else if(args[0].equalsIgnoreCase("create")) {
				if (args[1].equalsIgnoreCase("basic")) {
					if (args[2].equalsIgnoreCase("exp") ||
							args[2].equalsIgnoreCase("drop") ||
							args[2].equalsIgnoreCase("looting") ||
							args[2].equalsIgnoreCase("traveler") ||
							args[2].equalsIgnoreCase("recovery")) {
						masonMethods.createBasicCharm(p, args[2].toLowerCase());
						return true;
					}
					else {
						Util.sendMessage(p, "&cInvalid charm!");
						return true;
					}
				}
				else if (args[1].equalsIgnoreCase("advanced")) {
					if (args[2].equalsIgnoreCase("exp") ||
							args[2].equalsIgnoreCase("drop") ||
							args[2].equalsIgnoreCase("looting") ||
							args[2].equalsIgnoreCase("secondchance") ||
							args[2].equalsIgnoreCase("hunger")) {
						masonMethods.createAdvancedCharm(p, args[2].toLowerCase());
						return true;
					}
					else {
						Util.sendMessage(p, "&cInvalid charm!");
						return true;
					}
				}
				else if(args[1].equalsIgnoreCase("slot")) {
					if(StringUtils.isNumeric(args[2])) {
						masonMethods.createSlot(p, Integer.parseInt(args[2]));
						return true;
					}
					else {
						Util.sendMessage(p, "&cInvalid level!");
						return true;
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid item!");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("slot")) {
				if(StringUtils.isNumeric(args[1])) {
					if(Integer.parseInt(args[1]) <= MAX_SLOTS) {
						masonMethods.slot(p, Integer.parseInt(args[1]));
						return true;
					}
					else {
						Util.sendMessage((Player)sender, "&cYou must specify a valid slot number!");
						return true;
					}
				}
				else {
					Util.sendMessage((Player)sender, "&cYou must specify a slot number!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cInvalid parameters!");
				return true;
			}
		}
		else {
			Util.sendMessage((Player)sender, "&cYou are not a Mason!");
			return true;
		}
		return false;
	}

}
