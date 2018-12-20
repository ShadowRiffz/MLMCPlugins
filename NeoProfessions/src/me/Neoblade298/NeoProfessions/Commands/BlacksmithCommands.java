package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities;


public class BlacksmithCommands implements CommandExecutor{
	
	Main main;
	
	public BlacksmithCommands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neoprofessions.blacksmith") && sender instanceof Player) {
			
			Player p = (Player) sender;
			// Actual commands
			if(args.length == 4) {
				
				// args[1] = durability, args[2] = weapon/armor, args[3] = level
				if(args[0].equalsIgnoreCase("create")) {
					if((args[2].equalsIgnoreCase("weapon") || args[2].equalsIgnoreCase("armor")) &&
							args[1].equalsIgnoreCase("durability"))
						if(StringUtils.isNumeric(args[3])) {
							Utilities.createItem(p, "blacksmith", args[1].toLowerCase(), args[2].toLowerCase(), Integer.parseInt(args[3]));
						}
						else {
							Utilities.sendMessage(p, "&cInvalid level!");
						}
					else {
						Utilities.sendMessage(p, "&cInvalid parameters!");
					}
					
					
				}
				
				if(args[0].equalsIgnoreCase("upgrade")) {
					
				}
				
			}
			
			
		}
		
		
		Utilities.sendMessage((Player)sender, "&cYou are not a Blacksmith!");
		return true;
	}
}