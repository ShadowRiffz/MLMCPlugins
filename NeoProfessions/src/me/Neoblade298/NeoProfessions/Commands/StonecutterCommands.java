package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.StonecutterMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.md_5.bungee.api.ChatColor;

public class StonecutterCommands implements CommandExecutor {
	
	Main main;
	StonecutterMethods stonecutterMethods;
	
	public StonecutterCommands(Main main) {
		this.main = main;
		this.stonecutterMethods = main.stonecutterMethods;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("mason.professed") && sender instanceof Player) {
			
			Player p = (Player) sender;
			if(args.length == 0) {
				return true;
			
			}
			else if(args[0].equalsIgnoreCase("create")) {
				return true;
			}
			else if(args[0].equalsIgnoreCase("get")) {
				if(args[1].equalsIgnoreCase("ore")) {
					if(StringUtils.isNumeric(args[3])) {
						if(p.hasPermission("stonecutter.admin")) {
							p.getInventory().addItem(StonecutterItems.getOre(args[2], 1));
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cInvalid level!");
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid subcommand!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cInvalid parameters!");
				return true;
			}
		}
		else {
			Util.sendMessage((Player)sender, "&cYou are not a Stonecutter!");
			return true;
		}
		return false;
	}

}
