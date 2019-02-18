package me.neoblade298.neoattrsetter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(args.length == 1) {
			if(Bukkit.getPlayer(args[0]) != null) {
				return true;
			}
		}
		
		if(args.length == 0) {
		}
		
		return true;
		
	}
}