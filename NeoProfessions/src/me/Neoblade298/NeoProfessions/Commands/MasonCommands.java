package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
	
	Util util;
	public MasonCommands(Main main) {
		this.main = main;
		this.masonMethods = main.masonMethods;
		util = new Util();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("mason.professed") && sender instanceof Player) {
			
			Player p = (Player) sender;
			if (args.length == 0) {
				util.sendMessage(p, "&8&l[&cMason&8&l]");
				util.sendMessage(p, "&7- &c/mason create [basic/advanced] [charm]");
				util.sendMessage(p, "&7- &c/mason create slot");
				util.sendMessage(p, "&7- &c/mason slot [slot #]");
				util.sendMessage(p, "&7- &c/mason unslot [slot #]");
				util.sendMessage(p, "&7- &c/mason remove slot [slot #]");
				return true;
			}
			else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 3 && args[1].equalsIgnoreCase("basic")) {
					if (args[2].equalsIgnoreCase("exp") ||
							args[2].equalsIgnoreCase("drop") ||
							args[2].equalsIgnoreCase("looting") ||
							args[2].equalsIgnoreCase("traveler") ||
							args[2].equalsIgnoreCase("recovery")) {
						masonMethods.createBasicCharm(p, args[2].toLowerCase());
						return true;
					}
					else {
						util.sendMessage(p, "&cInvalid charm!");
						return true;
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("advanced")) {
					if (args[2].equalsIgnoreCase("exp") ||
							args[2].equalsIgnoreCase("drop") ||
							args[2].equalsIgnoreCase("looting") ||
							args[2].equalsIgnoreCase("secondchance") ||
							args[2].equalsIgnoreCase("hunger") ||
							args[2].equalsIgnoreCase("quickeat")) {
						masonMethods.createAdvancedCharm(p, args[2].toLowerCase());
						return true;
					}
					else {
						util.sendMessage(p, "&cInvalid charm!");
						return true;
					}
				}
				else if(args.length == 2 && args[1].equalsIgnoreCase("slot")) {
					masonMethods.createSlot(p);
					return true;
				}
				else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				if(args.length == 3) {
					if(args[1].equalsIgnoreCase("slot")) {
						if(StringUtils.isNumeric(args[2])) {
							masonMethods.removeSlot(p, Integer.parseInt(args[2]));
							return true;
						}
						else {
							util.sendMessage((Player)sender, "&cYou must specify a valid slot number!");
							return true;
						}
					}
					else {
						util.sendMessage(p, "&cInvalid parameters!");
						return true;
					}
				}
				else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("slot")) {
				if(args.length == 2) {
					if(StringUtils.isNumeric(args[1])) {
						if(Integer.parseInt(args[1]) <= MAX_SLOTS) {
							masonMethods.slot(p, Integer.parseInt(args[1]));
							return true;
						}
						else {
							util.sendMessage((Player)sender, "&cYou must specify a valid slot number!");
							return true;
						}
					}
					else {
						util.sendMessage((Player)sender, "&cYou must specify a slot number!");
						return true;
					}
				}
				else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("unslot")) {
				if(args.length == 2) {
					if(StringUtils.isNumeric(args[1])) {
						if(Integer.parseInt(args[1]) <= MAX_SLOTS) {
							masonMethods.unslot(p, Integer.parseInt(args[1]));
							return true;
						}
						else {
							util.sendMessage((Player)sender, "&cYou must specify a valid slot number!");
							return true;
						}
					}
				}
				else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			// RESET COMMAND
			else if(args[0].equalsIgnoreCase("reset")) {
				if(args.length == 2) {
					Player toReset = Bukkit.getPlayer(args[1]);
					if(toReset != null) {
						masonMethods.resetPlayer(toReset);
						return true;
					}
					else {
						util.sendMessage(p, "&cPlayer not found!");
						return true;
					}
				}
				else {
					sender.sendMessage("§cIncorrect number of arguments!");
					return true;
				}
			}
			else {
				util.sendMessage(p, "&cInvalid subcommand!");
				return true;
			}
		}
		else {
			util.sendMessage((Player)sender, "&cYou are not a Mason!");
			return true;
		}
		return false;
	}

}
