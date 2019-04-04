package me.neoblade298.neopprs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length == 0) {
			// TODO: Help menu
		}
		else if (args.length > 0) {
			if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
				
			}
		}
		return true;
	}
}
