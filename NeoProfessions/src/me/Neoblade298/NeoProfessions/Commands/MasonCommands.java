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
				if(args[1].equalsIgnoreCase("slot")) {
					if(StringUtils.isNumeric(args[2])) {
						masonMethods.createSlot(p, Integer.parseInt(args[2]));
						return true;
					}
					else {
						Util.sendMessage(p, "&cInvalid level!");
						return true;
					}
				}
			}
			else if(args[0].equalsIgnoreCase("slot")) {
				
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
