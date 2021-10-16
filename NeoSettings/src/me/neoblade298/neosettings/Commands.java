package me.neoblade298.neosettings;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neosettings.objects.Settings;


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
				sender.sendMessage("§c/settings <player> set [setting] [subsetting] [value]");
				sender.sendMessage("§c/settings <player> reset [setting] [subsetting]");
				sender.sendMessage("§c/settings <player> get [setting] [subsetting]");
				sender.sendMessage("§c/settings <player> list");
				return true;
			}
			else {
				// /settings set setting subsetting value
				Player target = p;
				int cmdArg = 0;
				if (Bukkit.getPlayer(args[0]) != null && p.hasPermission("mycommand.staff")) {
					target = Bukkit.getPlayer(args[0]);
					cmdArg = 1;
				}
				if (args[cmdArg].equalsIgnoreCase("set")) {
					if (main.changeSetting(args[cmdArg + 1], args[cmdArg + 2], args[cmdArg + 3], target.getUniqueId(), p)) {
						p.sendMessage("§4[§c§lMLMC§4] §7Setting §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7successfully changed!");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to change setting " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("reset")) {
					if (main.resetSetting(args[cmdArg + 1], args[cmdArg + 2], target.getUniqueId(), p)) {
						p.sendMessage("§4[§c§lMLMC§4] §7Setting §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7successfully reset!");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to reset setting " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("get")) {
					Settings settings = main.getSettings(args[cmdArg + 1], p);
					if (settings != null) {
						p.sendMessage("§4[§c§lMLMC§4] §7Setting §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7set to: §e" + settings.getValue(target.getUniqueId(), args[cmdArg + 2]));
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to get setting " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("list")) {
					HashMap<String, Settings> settings = main.getAllSettings();
					for (String key : settings.keySet()) {
						Settings subsettings = settings.get(key);
						if (!subsettings.isHidden() || p.hasPermission("mycommand.staff")) {
							for (String subkey : settings.get(key).getAllKeys()) {
								p.sendMessage("§7- §6" + key + "." + subkey + "§7: §e" + settings.get(key).getValue(target.getUniqueId(), subkey));
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
}