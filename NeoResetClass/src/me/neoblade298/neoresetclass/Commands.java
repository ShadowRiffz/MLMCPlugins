package me.neoblade298.neoresetclass;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if(sender.isOp() || sender.hasPermission("*")) {
		    if(args.length == 1) {
		    	main.resetPlayer(Bukkit.getPlayer(args[0]));
		    	return true;
			}
		    sender.sendMessage("Something went wrong even though you're op.");
		    return true;
		}
		sender.sendMessage("Something went wrong.");
		return true;
	}
}
