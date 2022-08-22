package me.neoblade298.neolooter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SethomeCommand implements CommandExecutor{
	
	Main main;
	
	public SethomeCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player) sender;
		main.homes.put(p, p.getLocation());
		p.sendMessage("Home set!");
		return true;
	}
}