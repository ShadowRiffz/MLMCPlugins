package me.neoblade298.neoinfinitewater;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor{
	
	InfiniteWater main;
	
	public Commands(InfiniteWater main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neoinfinitewater.use") && sender instanceof Player && args.length == 0) {
			main.toggle((Player) sender);
		}
		if (sender.hasPermission("mycommand.staff") && args.length > 0) {
			sender.sendMessage("§cPermission: neoinfinitewater.use");
		}
		return true;
	}
}