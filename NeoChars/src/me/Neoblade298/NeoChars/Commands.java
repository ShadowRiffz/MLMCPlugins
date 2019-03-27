package me.Neoblade298.NeoChars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor{
	
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(args.length == 1) {
			if(Bukkit.getPlayer(args[0]) != null) {
				main.sendPlayerCard((Player) sender, Bukkit.getOfflinePlayer(args[0]));
				return true;
			}
		}
		
		if(args.length == 0) {
			main.sendPlayerCard((Player) sender, (Player) sender);
		}
		
		return true;
		
	}
}