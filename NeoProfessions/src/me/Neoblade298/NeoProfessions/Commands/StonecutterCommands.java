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
	StonecutterUtils stonecutterUtils;
	StonecutterItems sItems;
	Util util;

	public StonecutterCommands(Main main) {
		this.main = main;
		this.stonecutterMethods = main.stonecutterMethods;
		stonecutterUtils = new StonecutterUtils();
		sItems = new StonecutterItems();
		util = new Util();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if (sender.hasPermission("stonecutter.professed") && sender instanceof Player) {

			Player p = (Player) sender;
			if (args.length == 0) {
				util.sendMessage(p, "&8&l[&cStonecutter&8&l]");
				util.sendMessage(p, "&7- &c/stonecutter create gem [weapon/armor] [attribute] [level]");
				util.sendMessage(p, "&7- &c/stonecutter create overload [weapon/armor] [attribute] [level]");
				util.sendMessage(p, "&7- &c/stonecutter refine [all]");
				return true;
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 5) {
					if (args[1].equalsIgnoreCase("gem")) {
						if (args[2].equalsIgnoreCase("weapon")) {
							if (stonecutterUtils.isWeaponAttribute(args[3])) {
								if (StringUtils.isNumeric(args[4])) {
									int level = Integer.parseInt(args[4]);
									if (level % 5 == 0 && level > 0 && level <= 60) {
										stonecutterMethods.createGem(p, args[3], args[2], Integer.parseInt(args[4]));
										return true;
									} else {
										util.sendMessage(p, "&cInvalid level!");
										return true;
									}
								} else {
									util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							} else {
								util.sendMessage(p, "&cInvalid attribute for weapons!");
								return true;
							}
						} else if (args[2].equalsIgnoreCase("armor")) {
							if (stonecutterUtils.isArmorAttribute(args[3])) {
								if (StringUtils.isNumeric(args[4])) {
									int level = Integer.parseInt(args[4]);
									if (level % 5 == 0 && level > 0 && level <= 60) {
										stonecutterMethods.createGem(p, args[3], args[2], Integer.parseInt(args[4]));
										return true;
									} else {
										util.sendMessage(p, "&cInvalid level!");
										return true;
									}
								} else {
									util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							} else {
								util.sendMessage(p, "&cInvalid attribute for armor!");
								return true;
							}
						} else {
							util.sendMessage(p, "&cInvalid gem type! Must be armor/weapon");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("overload")) {
						if (args[2].equalsIgnoreCase("weapon")) {
							if (stonecutterUtils.isWeaponAttribute(args[3])) {
								if (StringUtils.isNumeric(args[4])) {
									int level = Integer.parseInt(args[4]);
									if (level % 5 == 0 && level > 0 && level <= 60) {
										stonecutterMethods.createOverloadedGem(p, args[3], args[2],
												Integer.parseInt(args[4]));
										return true;
									} else {
										util.sendMessage(p, "&cInvalid level!");
										return true;
									}
								} else {
									util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							} else {
								util.sendMessage(p, "&cInvalid attribute for weapons!");
								return true;
							}
						} else if (args[2].equalsIgnoreCase("armor")) {
							if (stonecutterUtils.isArmorAttribute(args[3])) {
								if (StringUtils.isNumeric(args[4])) {
									int level = Integer.parseInt(args[4]);
									if (level % 5 == 0 && level > 0 && level <= 60) {
										stonecutterMethods.createOverloadedGem(p, args[3], args[2],
												Integer.parseInt(args[4]));
										return true;
									} else {
										util.sendMessage(p, "&cInvalid level!");
										return true;
									}
								} else {
									util.sendMessage(p, "&cInvalid gem level!");
									return true;
								}
							} else {
								util.sendMessage(p, "&cInvalid attribute for armor!");
								return true;
							}
						} else {
							util.sendMessage(p, "&cInvalid gem type! Must be armor/weapon");
							return true;
						}
					} else {
						util.sendMessage(p, "&cInvalid item!");
						return true;
					}
				} else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("refine")) {
				if (args.length == 1) {
					stonecutterMethods.refine(p, false);
					return true;
				} else if (args.length == 2 && args[1].equalsIgnoreCase("all")) {
					stonecutterMethods.refine(p, true);
					return true;
				} else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("get")) {
				if (args.length == 4) {
					if (args[1].equalsIgnoreCase("ore")) {
						if (StringUtils.isNumeric(args[3])) {
							if (p.hasPermission("stonecutter.admin")) {
								p.getInventory().addItem(sItems.getOre(args[2], Integer.parseInt(args[3])));
								return true;
							} else {
								util.sendMessage(p, "&cYou don't have permission to do that!");
								return true;
							}
						} else {
							util.sendMessage(p, "&cInvalid level!");
							return true;
						}
					} else {
						util.sendMessage(p, "&cInvalid parameters!");
						return true;
					}
				} else {
					util.sendMessage(p, "&cIncorrect number of arguments!");
					return true;
				}
			}
			// RESET COMMAND
			else if (args[0].equalsIgnoreCase("reset")) {
				if (args.length == 2) {
					Player toReset = Bukkit.getPlayer(args[1]);
					if (toReset != null) {
						stonecutterMethods.resetPlayer(toReset);
						return true;
					} else {
						util.sendMessage(p, "&cPlayer not found!");
						return true;
					}
				} else {
					sender.sendMessage("§cIncorrect number of arguments!");
					return true;
				}
			} else {
				util.sendMessage(p, "&cInvalid subcommand!");
				return true;
			}
		} else {
			util.sendMessage((Player) sender, "&cYou are not a Stonecutter!");
			return true;
		}
	}

}
