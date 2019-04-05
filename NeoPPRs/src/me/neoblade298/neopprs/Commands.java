package me.neoblade298.neopprs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
						Main.isModifying.put(author, false);
						ppr.preview(p);
					}
				}
				else if (args.length == 3 && args[1].equalsIgnoreCase("name")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						ppr.setUser(args[2]);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length > 2 && args[1].equalsIgnoreCase("offense")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						String offense = "";
						for (int i = 2; i < args.length - 1; i++) {
							offense += args[i] + " ";
						}
						offense += args[args.length - 1];
						ppr.setOffense(offense);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length > 2 && args[1].equalsIgnoreCase("action")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						String action = "";
						for (int i = 2; i < args.length - 1; i++) {
							action += args[i] + " ";
						}
						action += args[args.length - 1];
						ppr.setAction(action);
						ppr.preview(p);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length > 2 && (args[1].equalsIgnoreCase("description") || args[1].equalsIgnoreCase("desc"))) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						String desc = "";
						for (int i = 2; i < args.length - 1; i++) {
							desc += args[i] + " ";
						}
						desc += args[args.length - 1];
						ppr.setDescription(desc);
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
						Main.isModifying.remove(author);
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 1 && args[0].equalsIgnoreCase("post")) {
					if (Main.pprs.containsKey(author)) {
						PPR ppr = Main.pprs.get(author);
						if (ppr.isFilled()) {
							if (Main.isModifying.get(author)) {
								ppr.modify(p);
							}
							else {
								ppr.post(p);
							}
							Main.pprs.remove(author);
							Main.isModifying.remove(author);
						}
						else {
							sender.sendMessage("§4[§c§lMLMC§4] §7You must fill every part of the PPR to post!");
						}
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §7You are not in PPR creation mode!");
					}
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("view")) {
					main.viewPlayer(p, args[1]);
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("modify") && StringUtils.isNumeric(args[1])) {
					if (Main.pprs.containsKey(author)) {
						p.sendMessage("§4[§c§lMLMC§4] §7You are already creating a PPR! §c/ppr view");
					}
					else {
						int id = Integer.parseInt(args[1]);
						PPR ppr = null;
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs = stmt.executeQuery("SELECT * FROM neopprs_pprs WHERE id = " + id + ";");
							while (rs.next()) {
								ppr = new PPR(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						if (ppr != null) {
							p.sendMessage("§4[§c§lMLMC§4] §7You entered PPR creation mode!");
							Main.pprs.put(author, ppr);
							Main.isModifying.put(author, true);
							ppr.preview(p);
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7Could not find that PPR id!");
						}
					}
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("remove") && StringUtils.isNumeric(args[1])) {
					int id = Integer.parseInt(args[1]);
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						int deleted = stmt.executeUpdate("delete from neopprs_pprs WHERE id = " + id + ";");
						if (deleted > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successfully removed PPR!");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7No PPRs matching this id were found.");
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
				}
				else if (args.length == 3 && args[0].equalsIgnoreCase("rename")) {
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						int renamed = stmt.executeUpdate("update neopprs_pprs set username = '" +  args[2] + "' WHERE upper(username) = '" + args[1].toUpperCase() + ";");
						if (renamed > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successful renaming! " + renamed + " PPRs renamed.");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7No users matching this name found.");
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
				}
			}
			return true;
		}
		return false;
	}
}
