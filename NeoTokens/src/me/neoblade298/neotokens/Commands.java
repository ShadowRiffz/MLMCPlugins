package me.neoblade298.neotokens;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor{
	
	Tokens main;
	
	public Commands(Tokens main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("mycommand.staff")) {
			if (args.length == 0) {
				sender.sendMessage("§7Permission: mycommand.staff");
				sender.sendMessage("§c/tokens give boss [player]");
			}
			else if (args[0].equalsIgnoreCase("give")) {
				if (args[1].equalsIgnoreCase("boss")) {
					Player p = Bukkit.getPlayer(args[2]);
					p.getInventory().addItem(Items.getBossChestToken(p, System.currentTimeMillis()));
					sender.sendMessage("§4[§c§lMLMC§4] §7Successfully gave boss token to §e" + p.getName());
					return true;
				}
			}
		}
		return false;
	}
}