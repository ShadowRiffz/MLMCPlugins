package me.neoblade298.neoitemutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CmdItems implements CommandExecutor{
	
	ItemUtils main;
	
	public CmdItems(ItemUtils main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		return true;
	}
}