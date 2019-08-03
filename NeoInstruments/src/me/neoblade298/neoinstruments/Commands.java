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
			case "read":
				main.playBook((Player) sender);
				break;
			case "superalex":
				if(((Player) sender).getName().toLowerCase().contains("superalex")){
					main.superalex();
				}
				break;
			default:
				if((sender.isOp() || sender.hasPermission("*"))) {
					main.playNotes((Player) sender, args);
				}
			}
		}
		return true;
	}
}