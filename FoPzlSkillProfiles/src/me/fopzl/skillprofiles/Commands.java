package me.fopzl.skillprofiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
	SkillProfiles main;
	
	public Commands(SkillProfiles main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length < 2) return false;
		
		// TODO: verify args[1] is an int
		int slot = Integer.parseInt(args[1]);
		
		switch(args[0]) {
			case "save":
				main.Save((Player)sender, slot);
				return true;
			case "load":
				main.Load((Player)sender, slot);
				return true;
			default:
				return false;
		}
	}
}
