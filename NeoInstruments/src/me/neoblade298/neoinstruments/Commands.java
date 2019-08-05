package me.neoblade298.neoinstruments;

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

		if (args.length > 0) {

			switch (args[0]) {
			case "edit":
				main.editBook((Player) sender);
				break;
			case "tempo":
				if(args.length > 1 && args[1] != null) {
					main.setTempo((Player) sender, Integer.parseInt(args[1]));
					((Player)sender).sendMessage("§4[§c§lMLMC§4] §7Tempo set to " + args[1]);
				}
				break;
			case "sync":
				if(args.length > 1 && args[1] != null) {
					main.sync((Player) sender, args[1]);
					((Player)sender).sendMessage("§4[§c§lMLMC§4] §7Synced with " + args[1]);
				}
				break;
			case "unsync":
				main.unsync((Player) sender);
				((Player)sender).sendMessage("§4[§c§lMLMC§4] §7Unsynced");
				break;
			case "superalex":
				if(((Player) sender).getName().toLowerCase().contains("superalex")){
					main.superalex();
				}
				break;
			}
		}
		return true;
	}
}