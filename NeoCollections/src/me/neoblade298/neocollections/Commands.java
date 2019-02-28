package me.neoblade298.neocollections;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sucy.skill.SkillAPI;


public class Commands implements CommandExecutor{
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.isOp()) {
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
			// Grants player their collection bonuses, used on SkillAPI initialize and collection update
			if(args[0].equalsIgnoreCase("update")) {
				if(args[1].equalsIgnoreCase("all")) {
					main.updateAll();
				}
				else {
					main.updateBonuses(Bukkit.getPlayer(args[1]));
				}
				return true;
			}
			// neocollections remove [player]
			// Forcibly takes away collections from player
			if(args[0].equalsIgnoreCase("remove")) {
				if(args[1].equalsIgnoreCase("all")) {
					main.removeAll();
				}
				else {
					main.removeBonuses(Bukkit.getPlayer(args[1]));
				}
				return true;
			}
			// Debug command
			if(args[0].equalsIgnoreCase("debug")) {
				main.debug = !main.debug;
				sender.sendMessage("Debug " + main.debug);
				return true;
			}
			sender.sendMessage("Something went wrong.");
			return true;
		}
		sender.sendMessage("Something went wrong.");
		return true;
	}
}
