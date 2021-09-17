package me.Neoblade298.NeoChars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;


public class Commands implements CommandExecutor{
	
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(args.length == 1) {
			if(Bukkit.getPlayer(args[0]) != null) {
				main.sendPlayerCard((Player) sender, Bukkit.getPlayer(args[0]));
				return true;
			}
			else if (args[0].equalsIgnoreCase("all")) {
				  for (Player p : Bukkit.getOnlinePlayers()) {
					  if (SkillAPI.getPlayerData(p) == null) {
						  sender.sendMessage("§cNull data detected: " + p.getName());
					  }
				  }
			}
		}
		
		if(args.length == 0) {
			main.sendPlayerCard((Player) sender, (Player) sender);
		}
		
		return true;
		
	}
}