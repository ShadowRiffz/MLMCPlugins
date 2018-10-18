package me.neoblade298.neocollections;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.api.player.PlayerData;


public class Commands implements CommandExecutor{
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("*")) {
			// Test command, just to test adding and removing attributes
			if(args[0].equalsIgnoreCase("setBonus")) {
				Player p = Bukkit.getPlayer(args[1]);
				Attributes attrs = new Attributes(24, 24, 24, 24, 24, 24, 24);
				attrs.applyAttributes(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("removeBonus")) {
				Player p = Bukkit.getPlayer(args[1]);
				Attributes attrs = new Attributes(24, 24, 24, 24, 24, 24, 24);
				attrs.removeAttributes(p);
				return true;
			}
		}
		// Set Command - Resets a player's attributes, then sets them
		return true;
	}
}
