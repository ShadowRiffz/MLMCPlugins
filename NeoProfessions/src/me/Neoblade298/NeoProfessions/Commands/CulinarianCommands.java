package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
	Util util;
	
	public CulinarianCommands(Main main) {
		this.main = main;
		culinarianMethods = main.culinarianMethods;
		util = new Util();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("culinarian.professed") && sender instanceof Player) {
			Player p = (Player) sender;

			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				util.sendMessage(p, "&8&l[&cCulinarian&8&l]");
				util.sendMessage(p, "&7- &c/culinarian garnish [all]");
				util.sendMessage(p, "&7- &c/culinarian spice [all]");
				util.sendMessage(p, "&7- &c/culinarian preserve [all]");
				util.sendMessage(p, "&7- &c/culinarian special");
				util.sendMessage(p, "&7- &c/culinarian assimilate");
				util.sendMessage(p, "&7- &c/culinarian remedy [stun/root/curse/silence] [all]");
				util.sendMessage(p, "&7- &c/culinarian craft [ingredient/tier1/tier2/tier3/limited/drink/legendary] [itemname] <amount>");
				return true;
			}
			else {
				if(args[0].equalsIgnoreCase("garnish")) {
					if(args.length == 1) {
						culinarianMethods.garnish(p, false);
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
						culinarianMethods.garnish(p, true);
						return true;
					}
					else {
						util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("preserve")) {
					if(args.length == 1) {
						culinarianMethods.preserve(p, false);
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
						culinarianMethods.preserve(p, true);
						return true;
					}
					else {
						util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("spice")) {
					if(args.length == 1) {
						culinarianMethods.spice(p, false);
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
						culinarianMethods.spice(p, true);
						return true;
					}
					else {
						util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("assimilate")) {
					if(args.length == 1) {
						culinarianMethods.assimilate(p);
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
						culinarianMethods.assimilateAll(p);
						return true;
					}
					else {
						util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("special")) {
					if(args.length == 1) {
						culinarianMethods.giveSpecial(p);
						return true;
					}
					else {
						util.sendMessage(p, "&cIncorrect number of arguments!");
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("remedy")) {
					if(args.length == 2) {
						if(args[1].equalsIgnoreCase("stun") || args[1].equalsIgnoreCase("silence") || args[1].equalsIgnoreCase("curse") || args[1].equalsIgnoreCase("root")) {
							culinarianMethods.remedy(p, args[1], false);
							return true;
						}
						else {
							util.sendMessage(p, "&cInvalid parameters!");
							return true;
						}
					}
					else if (args.length == 3) {
						if(args[2].equalsIgnoreCase("all")) {
							culinarianMethods.remedy(p, args[1], true);
							return true;
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
							util.sendMessage(p, "&cInvalid crafting argument!");
							return true;
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
							culinarianMethods.resetPlayer(toReset);
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
		}
		else {
			util.sendMessage((Player) sender, "&cYou are not a culinarian!");
			return true;
		}
	}

}
