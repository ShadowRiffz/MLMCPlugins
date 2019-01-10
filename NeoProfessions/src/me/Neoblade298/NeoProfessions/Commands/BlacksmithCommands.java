package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Methods.BlacksmithMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;


public class BlacksmithCommands implements CommandExecutor {
	
	Main main;
	BlacksmithMethods blacksmithMethods;
	
	public BlacksmithCommands(Main main) {
		this.main = main;
		this.blacksmithMethods = main.blacksmithMethods;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("blacksmith.professed") && sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				// TODO: Add help message
				return true;
			}
			else {
				// CREATE COMMAND
				if(args[0].equalsIgnoreCase("create")) {
					if(args.length == 4) {
						// durability: args[0] = create, args[1] = durability, args[2] = weapon/armor, args[3] = level
						if(args[1].equalsIgnoreCase("durability")) {
							if (args[2].equalsIgnoreCase("weapon") || args[2].equalsIgnoreCase("armor")) {
								if(StringUtils.isNumeric(args[3])) {
									blacksmithMethods.createDurabilityItem(p, args[1].toLowerCase(), args[2].toLowerCase(), Integer.parseInt(args[3]));
									return true;
								}
								else {
									Util.sendMessage(p, "&cInvalid level!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid item type!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid item!");
							return true;
						}
					}
					else if(args.length == 3) {
						// repair: args[0] = create, args[1] = repair, args[2] = level
						if(args[1].equalsIgnoreCase("repair")) {
							if(StringUtils.isNumeric(args[2])) {
								blacksmithMethods.createRepairItem(p, args[1].toLowerCase(), Integer.parseInt(args[2]));
								return true;
							}
							else {
								Util.sendMessage(p, "&cInvalid level!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid parameters!");
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				
				
				// UPGRADE COMMAND
				else if(args[0].equalsIgnoreCase("upgrade")) {
					if(args.length == 2) {
						if(args[1].equalsIgnoreCase("unbreaking")) {
							blacksmithMethods.upgradeItem(p);
							return true;
						}
						else {
							Util.sendMessage(p, "&cInvalid upgrade parameter!");
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				
				// REFORGE COMMAND
				else if(args[0].equalsIgnoreCase("reforge")) {
					if(args.length == 1) {
						blacksmithMethods.reforgeItem(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				
				else if(args[0].equalsIgnoreCase("scrap")) {
					if(args.length == 1) {
						blacksmithMethods.scrapItem(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				
				
				// GET COMMAND (Debug)
				else if(args[0].equalsIgnoreCase("get")) {
					if(args.length == 3) {
						if (args[1].equalsIgnoreCase("Essence")) {
							if(StringUtils.isNumeric(args[2])) {
								if(p.hasPermission("blacksmith.admin")) {
									p.getInventory().addItem(CommonItems.getEssence(Integer.parseInt(args[2])));
									Util.sendMessage(p, "&7Successfully spawned Essence!");
									return true;
								}
								else {
									Util.sendMessage(p, "&cYou don't have permission to do that!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid number!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid item!");
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid subcommand!");
					return true;
				}
			}
		}
		else {
			Util.sendMessage((Player)sender, "&cYou are not a Blacksmith!");
			return true;
		}
		
		// Todo: add 0 arg command for help menu
	}
}