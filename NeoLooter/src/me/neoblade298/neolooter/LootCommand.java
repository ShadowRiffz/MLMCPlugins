package me.neoblade298.neolooter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class LootCommand implements CommandExecutor{
	
	Main main;
	
	public LootCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (main.isLooting.contains(sender.getName())) {
			main.isLooting.remove(sender.getName());
			sender.sendMessage("Looting toggled off!");
		}
		else {
			main.isLooting.add(sender.getName());
			sender.sendMessage("Looting toggled on!");
		}
		return true;
	}
}