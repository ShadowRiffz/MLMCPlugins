package me.neoblade298.neoplayerdata;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoplayerdata.objects.PlayerFields;


public class Commands implements CommandExecutor{
	
	NeoPlayerData main;
	
	public Commands(NeoPlayerData main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sender.sendMessage("§c/pd <player> set [key] [subkey] [value]");
				sender.sendMessage("§c/pd <player> reset [key] [subkey]");
				sender.sendMessage("§c/pd <player> get [key] [subkey]");
				sender.sendMessage("§c/pd <player> list");
				sender.sendMessage("§c/pd debug");
				return true;
			}
			else if (args[0].equalsIgnoreCase("debug") && p.hasPermission("mycommand.staff")) {
				main.debug = !main.debug;
				p.sendMessage("§4[§c§lMLMC§4] §7Debug set to §e" + main.debug + "§7!");
				return true;
			}
			else {
				// /settings set key subkey value
				Player target = p;
				int cmdArg = 0;
				if (Bukkit.getPlayer(args[0]) != null && p.hasPermission("mycommand.staff")) {
					target = Bukkit.getPlayer(args[0]);
					cmdArg = 1;
				}
				if (args[cmdArg].equalsIgnoreCase("set")) {
					if (main.change(args[cmdArg + 1], args[cmdArg + 2], args[cmdArg + 3], target.getUniqueId(), canAccessHidden(p))) {
						p.sendMessage("§4[§c§lMLMC§4] §7Player data key §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7successfully changed!");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to change player data key " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("reset")) {
					if (main.reset(args[cmdArg + 1], args[cmdArg + 2], target.getUniqueId(), canAccessHidden(p))) {
						p.sendMessage("§4[§c§lMLMC§4] §7Player data key §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7successfully reset!");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to reset player data key " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("get")) {
					PlayerFields settings = main.getKeyedPlayerData(args[cmdArg + 1], canAccessHidden(p));
					if (settings != null) {
						p.sendMessage("§4[§c§lMLMC§4] §7Player data key §e" + args[cmdArg + 1] + "." + args[cmdArg + 2] + " §7set to: §e" + settings.getValue(target.getUniqueId(), args[cmdArg + 2]));
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §cFailed to get player data key " + args[cmdArg + 1] + "." + args[cmdArg + 2] + "!");
					}
					return true;
				}
				else if (args[cmdArg].equalsIgnoreCase("list")) {
					HashMap<String, PlayerFields> settings = main.getAllKeyedPlayerData();
					for (String key : settings.keySet()) {
						PlayerFields subsettings = settings.get(key);
						if (!subsettings.isHidden() || canAccessHidden(p)) {
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
	
	private boolean canAccessHidden(Player p) {
		return p.hasPermission("mycommand.staff");
	}
}