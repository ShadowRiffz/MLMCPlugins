package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.MasonMethods;
import me.Neoblade298.NeoProfessions.Methods.StonecutterMethods;
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
		
		if(sender.hasPermission("mason.professed") && sender instanceof Player) {
			
			Player p = (Player) sender;
			if(args.length == 0) {
			
			}
			else if(args[0].equalsIgnoreCase("create")) {
			}
			else if(args[0].equalsIgnoreCase("get")) {
				if(args[1].equalsIgnoreCase("ore")) {
					switch (args[2]) {
					case "strength":	p.getInventory().addItem(StonecutterItems.getStrengthGem(1));
					break;
					}
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
