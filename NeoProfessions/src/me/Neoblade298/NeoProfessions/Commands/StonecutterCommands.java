package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.StonecutterMethods;
import me.Neoblade298.NeoProfessions.Utilities.StonecutterUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class StonecutterCommands implements CommandExecutor {
	
	Main main;
	StonecutterMethods stonecutterMethods;
	
	public StonecutterCommands(Main main) {
		this.main = main;
		this.stonecutterMethods = main.stonecutterMethods;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("stonecutter.professed") && sender instanceof Player) {
			
			Player p = (Player) sender;
			if(args.length == 0) {
				return true;
			
			}
			else if(args[0].equalsIgnoreCase("create")) {
				if(args.length == 5) {
					if(args[1].equalsIgnoreCase("gem")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							if(StonecutterUtils.isWeaponAttribute(args[3])) {
								if(StringUtils.isNumeric(args[4])) {
									stonecutterMethods.createGem(p, args[3], args[2], Integer.parseInt(args[4]));
									return true;
								}
								else {
									Util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid attribute for weapons!");
								return true;
							}
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							if(StonecutterUtils.isArmorAttribute(args[3])) {
								if(StringUtils.isNumeric(args[4])) {
									stonecutterMethods.createGem(p, args[3], args[2], Integer.parseInt(args[4]));
									return true;
								}
								else {
									Util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid attribute for armor!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid gem type! Must be armor/weapon");
							return true;
						}
					}
					else if(args[1].equalsIgnoreCase("overload")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							if(StonecutterUtils.isWeaponAttribute(args[3])) {
								if(StringUtils.isNumeric(args[4])) {
									stonecutterMethods.createOverloadedGem(p, args[3], args[2], Integer.parseInt(args[4]));
									return true;
								}
								else {
									Util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid attribute for weapons!");
								return true;
							}
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							if(StonecutterUtils.isArmorAttribute(args[3])) {
								if(StringUtils.isNumeric(args[4])) {
									stonecutterMethods.createOverloadedGem(p, args[3], args[2], Integer.parseInt(args[4]));
									return true;
								}
								else {
									Util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cInvalid attribute for armor!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid gem type! Must be armor/weapon");
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
			else if(args[0].equalsIgnoreCase("refine")) {
				if(args.length == 1) {
					stonecutterMethods.refine(p);
					return true;
				}
				else {
					Util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("get")) {
				if(args.length == 4) {
					if(args[1].equalsIgnoreCase("ore")) {
						if(StringUtils.isNumeric(args[3])) {
							if(p.hasPermission("stonecutter.admin")) {
								p.getInventory().addItem(StonecutterItems.getOre(args[2], Integer.parseInt(args[3])));
								return true;
							}
							else {
								Util.sendMessage(p, "&cYou don't have permission to do that!");
								return true;
							}
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
			// RESET COMMAND
			else if(args[0].equalsIgnoreCase("reset")) {
				if(args.length == 2) {
					Player toReset = Bukkit.getPlayer(args[1]);
					if(toReset != null) {
						stonecutterMethods.resetPlayer(toReset);
						return true;
					}
					else {
						Util.sendMessage(p, "&cPlayer not found!");
						return true;
					}
				}
				else {
					sender.sendMessage("§cIncorrect number of arguments!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cInvalid subcommand!");
				return true;
			}
		}
		else {
			Util.sendMessage((Player)sender, "&cYou are not a Stonecutter!");
			return true;
		}
	}

}
