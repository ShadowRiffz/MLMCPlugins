package me.neoblade298.neopprs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length == 0) {
			// TODO: Help menu
		}
		else if (args.length > 0) {
			if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
				main.createPPR(sender.getName());
			}
		}
		return true;
	}
}
