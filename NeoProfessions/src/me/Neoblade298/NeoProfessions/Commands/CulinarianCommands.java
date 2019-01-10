package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
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
				if(args[0].equalsIgnoreCase("garnish")) {
					if(args.length == 1) {
						culinarianMethods.garnish(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("preserve")) {
					if(args.length == 1) {
						culinarianMethods.preserve(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("spice")) {
					if(args.length == 1) {
						culinarianMethods.spice(p);
						return true;
					}
					else {
						Util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("craft")) {
					if (args.length > 2) {
						if(args[1].equalsIgnoreCase("ingredient")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseIngredient(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseIngredient(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("tier1")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier1(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier1(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("tier2")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier2(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier2(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("tier3")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier3(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseTier3(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("limited")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseLimitedEdition(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseLimitedEdition(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("legendary")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseLegendary(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseLegendary(p, recipe);
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("drink")) {
							if(StringUtils.isNumeric(args[args.length - 1])) {
								String recipe = "";
								for(int i = 2; i < args.length - 1; i++) {
									recipe += args[i];
									if(i < args.length - 2) {
										recipe += " ";
									}
								}
								culinarianMethods.parseDrink(p, recipe, Integer.parseInt(args[args.length - 1]));
								return true;
							}
							else {
								String recipe = "";
								for(int i = 2; i < args.length; i++) {
									recipe += args[i];
									if(i < args.length - 1) {
										recipe += " ";
									}
								}
								culinarianMethods.parseDrink(p, recipe);
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid crafting argument!");
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
			Util.sendMessage((Player) sender, "&cYou are not a culinarian!");
			return true;
		}
		return false;
	}

}
