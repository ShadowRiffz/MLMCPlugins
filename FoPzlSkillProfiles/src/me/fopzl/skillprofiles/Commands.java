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
		if(args.length < 1 || !(sender instanceof Player)) return false;
		
		switch(args[0]) {
			case "save":
				if(args.length < 2) return false;
				return main.save((Player)sender, args[1]);
			case "load":
				if(args.length < 2) return false;
				return main.load((Player)sender, args[1]);
			case "list":
			case "ls":
				return main.list((Player)sender);
			case "delete":
			case "del":
				if(args.length < 2) return false;
				return main.delete((Player)sender, args[1]);
			case "debug":
				if(!sender.hasPermission("fopzlskillprofiles.admin")) return false;
				return main.toggleDebug(sender);
			default:
				return false;
		}
	}
}
