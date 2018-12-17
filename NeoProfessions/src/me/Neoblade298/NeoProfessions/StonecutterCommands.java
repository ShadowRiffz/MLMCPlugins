package me.Neoblade298.NeoProfessions;

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
					if (args[1].equalsIgnoreCase("charm")) {
						Utilities.createItem(p, "mason", "charm", args[2]);
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
