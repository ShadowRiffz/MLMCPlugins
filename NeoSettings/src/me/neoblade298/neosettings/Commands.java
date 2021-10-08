package me.neoblade298.neosettings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor{
	
	NeoSettings main;
	
	public Commands(NeoSettings main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sender.sendMessage("&c/settings set [setting] [subsetting] [value]");
				sender.sendMessage("&c/settings reset [setting] [subsetting]");
				return true;
			}
			else {
				// /settings set setting subsetting value
				if (args[0].equalsIgnoreCase("set")) {
					if (main.changeSetting(args[1], args[2], args[3], p.getUniqueId())) {
						p.sendMessage("&4[&c&lMLMC&4] &7Setting &e" + args[1] + "." + args[2] + " &7successfully changed!");
					}
					else {
						p.sendMessage("&4[&c&lMLMC&4] &cFailed to change setting " + args[1] + "." + args[2] + "!");
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("reset")) {
					if (main.resetSetting(args[1], args[2], p.getUniqueId())) {
						p.sendMessage("&4[&c&lMLMC&4] &7Setting &e" + args[1] + "." + args[2] + " &7successfully reset!");
					}
					else {
						p.sendMessage("&4[&c&lMLMC&4] &cFailed to reset setting " + args[1] + "." + args[2] + "!");
					}
					return true;
				}
			}
		}
		return false;
	}
}