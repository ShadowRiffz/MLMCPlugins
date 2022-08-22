package me.neoblade298.neolooter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HomeCommand implements CommandExecutor{
	
	Main main;
	
	public HomeCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player) sender;
		if (main.homes.containsKey(p)) {
			p.teleport(main.homes.get(p));
			main.isLooting.remove(p.getName());
			p.sendMessage("Teleported home and toggled off looting!");
		}
		else {
			p.sendMessage("Home not set!");
		}
		return true;
	}
}