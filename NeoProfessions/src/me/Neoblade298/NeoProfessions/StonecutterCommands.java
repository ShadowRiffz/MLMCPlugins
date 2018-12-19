package me.Neoblade298.NeoProfessions;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StonecutterCommands implements CommandExecutor {
	
	Main main;
	
	public StonecutterCommands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neoprofessions.stonecutter") && sender instanceof Player) {
			
			Player p = (Player) sender;
			// Actual commands
			if(args.length == 3) {
				
				// Create
				if(args[0].equalsIgnoreCase("create")) {
					if(StringUtils.isNumeric(args[2])) {
						Utilities.createItem(p, "stonecutter", args[1].toLowerCase(), args[2]);
					}
					else {
						Utilities.sendMessage(p, "&cInvalid level!");
					}
				}
				
				if(args[0].equalsIgnoreCase("upgrade")) {
					
				}
				
			}
			
			
		}
		
		
		Utilities.sendMessage((Player)sender, "&cYou are not a Mason!");
		return true;
	}

}
