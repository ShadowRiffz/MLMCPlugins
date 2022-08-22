package me.neoblade298.neovoteparty;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	
	VoteParty main;
	
	public Commands(VoteParty main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("mycommand.staff") || sender.isOp()) {
			// /vp add [player]
			if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
				main.incrementVote(Bukkit.getPlayer(args[1]));
			}
			// /vp set [num] 
			else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
				main.count = Integer.parseInt(args[1]);
			}
			// /vp perms
			else if (args.length == 1 && args[0].equalsIgnoreCase("perms")) {
				sender.sendMessage("§7neovoteparty.use");
			}
			// /vp reload
			else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				main.reloadConfig();
			}
		}
		if (sender.hasPermission("neovoteparty.use")) {
			if (args.length == 0) {
				sender.sendMessage("§c/vp status §7- Check # of votes till vote party");
				if (sender.hasPermission("mycommand.staff")) {
					sender.sendMessage("§c/vp add [player] §7- Increments votes by 1");
					sender.sendMessage("§c/vp set §7- Sets vote number");
					sender.sendMessage("§c/vp reload §7- Reloads config");
					sender.sendMessage("§c/vp perms §7- Check what permission this plugin uses");
				}
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
				sender.sendMessage("§4[§c§lMLMC§4] §e" + main.count + " / " + main.voteparty + "§7 votes for a vote party to commence!");
			}
		}
		return true;
	}
}