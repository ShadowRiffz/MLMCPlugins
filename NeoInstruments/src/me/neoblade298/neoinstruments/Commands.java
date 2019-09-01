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
		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;

		if (args.length > 0) {

			switch (args[0]) {
			case "edit":
				main.editBook(player);
				break;
			case "tempo":
				if(args.length > 1 && args[1] != null) {
					main.setTempo(player, Integer.parseInt(args[1]));
				}
				break;
			case "sync":
				if(args.length > 1 && args[1] != null) {
					main.askSync(player, args[1]);
				}
				break;
			case "unsync":
				main.unsync(player);
				break;
			case "confirm":
				if(args.length > 1 && args[1] != null) {
					main.confirmSync(player, args[1]);
				}
				break;
			case "deny":
				main.denySync(player);
				break;
			case "getbook":
				if(player.isOp() || player.hasPermission("*")) {
					main.getBook(player);
				}
				break;
			default:
				return false;
			}
			
			return true;
		} else {
			return false;
		}
	}
}