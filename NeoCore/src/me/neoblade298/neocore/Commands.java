package me.neoblade298.neocore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	
	NeoCore main;
	
	public Commands(NeoCore main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		return true;
	}
}