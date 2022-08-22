package me.neoblade298.neostats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.neoblade298.neostats.Main;

public class Commands implements CommandExecutor {
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mycommand.staff")) {
			if(args.length == 3) {
				
				// neostats showdamage [Bossname] [DisplayName]
				if(args[0].equalsIgnoreCase("showdamage")) {
					main.displayStats(args[1], args[2].replaceAll("_", " "));
					return true;
				}
			}
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("debug")) {
					main.debug = !main.debug;
					if(main.debug) {
						sender.sendMessage("§7Debug enabled");
					}
					else {
						sender.sendMessage("§7Debug disabled");
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("report")) {
					main.report = !main.report;
					if(main.report) {
						sender.sendMessage("§7Reports enabled");
					}
					else {
						sender.sendMessage("§7Reports disabled");
					}
					return true;
				}
			}
		}
		sender.sendMessage("Something went wrong.");
		return true;
	}

}
