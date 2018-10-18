package me.neoblade298.neocollections;

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
		
		if(sender.hasPermission("*")) {
			if(args.length == 2) {
				// neocollections init [player]
				// Initializes the bonuses of any player, used on skillAPI init
				if(args[0].equalsIgnoreCase("init")) {
					main.initializeBonuses(Bukkit.getPlayer(args[1]));
					return true;
				}
				// neocollections reset [player]
				// Removes player from map, used on skillAPI cleanup
				if(args[0].equalsIgnoreCase("reset")) {
					if(args[1].equalsIgnoreCase("all")) {
						main.resetAll();
					}
					else {
						main.resetBonuses(Bukkit.getPlayer(args[1]));
					}
					return true;
				}
				// neocollections update [player]
				// Re-initializes player after removing bonuses, used on collection change
				if(args[0].equalsIgnoreCase("update")) {
					if(args[1].equalsIgnoreCase("all")) {
						main.updateAll();
					}
					else {
						main.updateBonuses(Bukkit.getPlayer(args[1]));
					}
					return true;
				}
			}
		}
		// Set Command - Resets a player's attributes, then sets them
		return true;
	}
}
