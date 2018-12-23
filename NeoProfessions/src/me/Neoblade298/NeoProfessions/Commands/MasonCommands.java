package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Util;

public class MasonCommands implements CommandExecutor {
	
	Main main;
	
	public MasonCommands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neoprofessions.mason") && sender instanceof Player) {
			
			Player p = (Player) sender;
			// Actual commands
			if(args.length == 3) {
				
				// Create
				if(args[0].equalsIgnoreCase("create")) {
					if (args[1].equalsIgnoreCase("charm")) {
						p.sendMessage("");
					}
				}
				
				if(args[0].equalsIgnoreCase("upgrade")) {
					
				}
				
			}
			
			
		}
		
		
		Util.sendMessage((Player)sender, "&cYou are not a Mason!");
		return true;
	}

}
