package me.neoblade298.neopprs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	Main main;
	
	public Commands(Main main) {
		this.main = main;
		System.out.println("TEST");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neopprs.admin") && sender instanceof Player) {
			Player p = (Player) sender;
			String author = p.getName();
			if(args.length == 0) {
				// TODO: Help menu
			}
			else {
				if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
					if (Main.pprs.containsKey(author)) {
						p.sendMessage("§4[§c§lMLMC§4] §7You are already creating a PPR! §c/ppr view");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §7You entered PPR creation mode!");
						PPR ppr = new PPR(Main.nextPPR, author);
						Main.nextPPR++;
						Main.pprs.put(author, ppr);
						ppr.preview(p);
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("username")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.setUser(args[2]);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("offense")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.setOffense(args[2]);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("action")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.setAction(args[2]);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("description")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.setDescription(args[2]);
						ppr.preview(p);
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 1 && args[0].equalsIgnoreCase("view")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
					if (Main.pprs.containsKey(author)) {
						p.sendMessage("§4[§c§lMLMC§4] §7You exited PPR creation mode!");
						Main.pprs.remove(author);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 1 && args[0].equalsIgnoreCase("post")) {
					if (Main.pprs.containsKey(author)) {
						Main.pprs.get(author).post(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
			}
			return true;
		}
		return false;
	}
}
