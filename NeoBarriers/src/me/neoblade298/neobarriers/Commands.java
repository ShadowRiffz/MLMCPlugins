package me.neoblade298.neobarriers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	Main main;

	public Commands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if (args.length == 1) {
			if (sender instanceof Player && sender.hasPermission("neobarriers.admin")) {
				Player p = (Player) sender;
				if (args[0].equalsIgnoreCase("off")) {
					main.yvalues.remove(p.getName());
					msg(sender, "&7Successfully disabled your barrier placing");
				}
				else if (args[0].equalsIgnoreCase("perms")) {
					msg(sender, "&7neobarriers.admin");
				}
				else if (StringUtils.isNumeric(args[0])) {
					Integer yval = Integer.parseInt(args[0]);
					main.yvalues.put(p.getName(), yval);
					msg(sender, "&7Successfully enabled barrier placing at y value &e" + args[0]);
				}
				return true;
			}
		}

		if (args.length == 0) {
			msg(sender, "&7[&c&lBarrier Helper&7]");
			msg(sender, "&7- &c/barriers &7- Help menu");
			msg(sender, "&7- &c/barriers [y-value] &7- Enable placing barriers up to y value");
			msg(sender, "&7- &c/barriers off &7- Disable placing barriers");
			msg(sender, "&7- &c/barriers perms &7- Display what permissions");
			return true;
		}
		return true;
	}
	
	public void msg(CommandSender p, String msg) {
		p.sendMessage(msg.replaceAll("&", "§"));
	}
	public void msgP(CommandSender p, String msg) {
		p.sendMessage("§4[§c§lMLMC§4] " + msg.replaceAll("&", "§"));
	}
}