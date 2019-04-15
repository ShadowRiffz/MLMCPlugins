package me.neoblade298.neoreports;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoreports.Main;

public class ReportCommand implements CommandExecutor {
	Main main;
	
	public ReportCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neoreports.user") && sender instanceof Player) {
			Player p = (Player) sender;
			String author = p.getName();
			if(args.length == 0) {
				p.sendMessage("§7--- §cNeoReports §7---");
				p.sendMessage("§c/report bug [description] §7- Reports a bug to the staff");
				p.sendMessage("§c/report urgent [description] §7- Reports an urgent bug to the staff, use for time-sensitive issues!");
				p.sendMessage("§c/reports list §7- Lists all reports made by you");
				p.sendMessage("§c/reports remove [bug ID] §7- Removes a report made by you (you should do this after your bug is marked as resolved!)");
				if (sender.hasPermission("neoreports.admin")) {
					p.sendMessage("§4/reports check §7- Simple message showing how many reports exist at the moment");
					p.sendMessage("§4/reports list [bug/urgent/resolved] §7- Lists all bugs of a certain type");
					p.sendMessage("§4/reports resolve [bug id] [comment] §7- Resolves a bug, marking it with the comment");
					p.sendMessage("§4/reports unresolve [bug id] §7- Unresolves a bug");
					p.sendMessage("§4/reports edit [bug id] [comment] §7- Edits an existing comment (only for resolved bugs)");
				}
				return true;
			}
			else if (args[0].equalsIgnoreCase("bug") && args.length > 1) {
				String desc = args[1];
				for (int i = 2; i < args.length; i++) {
					desc += " " + args[i];
				}
				Report rep = new Report(Main.nextReport, author, desc.replaceAll("'", "\'"), false);
				rep.post(p);
				return true;
			}
			else if (args[0].equalsIgnoreCase("urgent") && args.length > 1) {
				String desc = args[1];
				for (int i = 2; i < args.length; i++) {
					desc += " " + args[i];
				}
				Report rep = new Report(Main.nextReport, author, desc.replaceAll("'", "\'"), true);
				rep.post(p);
				return true;
			}
			else {
				p.sendMessage("§c[§4§lMLMC§c] §7Invalid command!");
				return true;
			}
		}
		return false;
	}
}
