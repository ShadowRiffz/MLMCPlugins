package me.neoblade298.neowarnings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("understood") && Main.understood.containsKey(p.getName())) {
			Main.understood.remove(p.getName());
			p.sendMessage("§4[§c§lMLMC§4] §7You successfully acknowledged the warning. Congratulations on the new town!");
		}
		return true;
	}
}
