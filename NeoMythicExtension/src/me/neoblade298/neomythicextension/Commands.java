package me.neoblade298.neomythicextension;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	Main main;
	
	public Commands (Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
	    if(args.length == 2 && args[0].equalsIgnoreCase("debug") && (sender.isOp() || sender.hasPermission("mycommand.staff"))) {
	    	sender.sendMessage("Global score of " + args[1] + " is " + main.globalscores.get(args[1]));
	    	return true;
	    }
    	return false;
	}
}
