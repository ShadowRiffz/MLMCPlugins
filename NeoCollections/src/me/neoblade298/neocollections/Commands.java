package me.neoblade298.neocollections;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sucy.skill.api.player.PlayerData;


public class Commands implements CommandExecutor{
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		// Set Command - Resets a player's attributes, then sets them
		return true;
	}
	
	private void resetAttributes(PlayerData player) {
		
	}
}
