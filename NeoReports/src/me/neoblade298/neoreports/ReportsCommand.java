package me.neoblade298.neoreports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoreports.Main;

public class ReportsCommand implements CommandExecutor {
	Main main;
	static int NUM_REPORTS_PER_PAGE = 10;
	
	public ReportsCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neoreports.user") && sender instanceof Player) {
			Player p = (Player) sender;
			String author = p.getName();
			if(args.length == 0) {
				p.sendMessage("§7--- §cNeoReports ---");
				p.sendMessage("§c/report bug [description] §7- Reports a bug to the staff");
				p.sendMessage("§c/report urgent [description] §7- Reports an urgent bug to the staff, use for time-sensitive issues!");
				p.sendMessage("§c/reports list §7- Lists all reports made by you");
				p.sendMessage("§c/reports remove [bug ID] §7- Removes a report made by you (do this after your bug is resolved!)");
				if (sender.hasPermission("neoreports.admin")) {
					p.sendMessage("§4/reports check §7- Simple message showing how many reports exist at the moment");
					p.sendMessage("§4/reports list [bug/urgent/resolved] §7- Lists all bugs of a certain type");
					p.sendMessage("§4/reports resolve [bug id] [comment] §7- Resolves a bug, marking it with the comment");
					p.sendMessage("§4/reports edit [bug id] [comment] §7- Edits an existing comment (only for resolved bugs)");
				}
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
				try{  
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					ResultSet rs;
					rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE user = '" + author + "';");
					while(rs.next()) {
						Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
						temp.list(p);
					}
					con.close();
				}
				catch(Exception e) {
					System.out.println(e);
					p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
				}
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("remove") && StringUtils.isNumeric(args[1]) && !p.hasPermission("neoreports.admin")) {
				try{  
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					int deleted = stmt.executeUpdate("DELETE FROM neoreports_bugs WHERE id = " + args[1] + " AND user = '" + author +"';");
					if (deleted > 0) {
						p.sendMessage("§4[§c§lMLMC§4] §7Successfully deleted report!");
					}
					else {
						p.sendMessage("§4[§c§lMLMC§4] §7Failed to delete report! Are you the creator of the report?");
					}
					con.close();
				}
				catch(Exception e) {
					System.out.println(e);
					p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
				}
				return true;
			}
			else if (p.hasPermission("neoreports.admin")) {
				if (args.length == 1 && args[0].equalsIgnoreCase("check")) {
					p.sendMessage("§4[§c§lMLMC§4] §7# Bugs: §e" + Main.numBugs + "§7, # Urgent: §e" + Main.numUrgent);
					return true;
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("remove") && StringUtils.isNumeric(args[1])) {
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						int deleted = stmt.executeUpdate("DELETE FROM neoreports_bugs WHERE id = " + args[1] + ";");
						if (deleted > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successfully deleted report!");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7Failed to delete report! Are you sure the id is correct?");
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
					return true;
				}
				else if ((args.length == 2 || args.length == 3) && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("bug")) {
					// Show first page only
					if (args.length == 2) {
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_urgent = 0 AND is_resolved = 0 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
										rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
								temp.list(p);
								count++;
								if (count >= NUM_REPORTS_PER_PAGE) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
					else if (args.length == 3 && StringUtils.isNumeric(args[2])) {
						int page = Integer.parseInt(args[2]);
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_urgent = 0 AND is_resolved = 0 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								if (NUM_REPORTS_PER_PAGE * (page - 1) < count) {
									Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
											rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
									temp.list(p);
								}
								count++;
								if (count >= NUM_REPORTS_PER_PAGE * page) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
				}
				else if ((args.length == 2 || args.length == 3) && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("urgent")) {
					// Show first page only
					if (args.length == 2) {
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_urgent = 1 AND is_resolved = 0 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
										rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
								temp.list(p);
								count++;
								if (count >= NUM_REPORTS_PER_PAGE) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
					else if (args.length == 3 && StringUtils.isNumeric(args[2])) {
						int page = Integer.parseInt(args[2]);
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_urgent = 1 AND is_resolved = 0 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								if (NUM_REPORTS_PER_PAGE * (page - 1) < count) {
									Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
											rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
									temp.list(p);
								}
								count++;
								if (count >= NUM_REPORTS_PER_PAGE * page) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
				}
				else if ((args.length == 2 || args.length == 3) && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("resolved")) {
					// Show first page only
					if (args.length == 2) {
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_resolved = 1 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
										rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
								temp.list(p);
								count++;
								if (count >= NUM_REPORTS_PER_PAGE) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
					else if (args.length == 3 && StringUtils.isNumeric(args[2])) {
						int page = Integer.parseInt(args[2]);
						try{  
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
							Statement stmt = con.createStatement();
							ResultSet rs;
							rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE is_resolved = 1 ORDER BY id;");
							int count = 1;
							while(rs.next()) {
								if (NUM_REPORTS_PER_PAGE * (page - 1) < count) {
									Report temp = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
											rs.getString(7), rs.getInt(8) == 1, rs.getInt(9) == 1);
									temp.list(p);
								}
								count++;
								if (count >= NUM_REPORTS_PER_PAGE * page) {
									break;
								}
							}
							con.close();
						}
						catch(Exception e) {
							System.out.println(e);
							p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
						}
						return true;
					}
				}
				else if (args.length > 2 && args[0].equalsIgnoreCase("resolve") && StringUtils.isNumeric(args[1])) {
					String desc = args[2];
					for (int i = 2; i < args.length; i++) {
						desc += " " + args[i];
					}
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						ResultSet rs;
						int resolved = stmt.executeUpdate("UPDATE neoreports_bugs SET `is_resolved` = 1, `comment` = '" + desc + "', `resolver` = '" + author + "' WHERE id = " + args[1] + ";");
						if (resolved > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successfully resolved report!");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7Failed to unresolve report!");
						}
						rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE id = " + args[1] + ";");
						if (rs.next()) {
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "mail send " + rs.getString(3) + " Your bug report of ID " + args[1] + " has been resolved! /reports list");
						}
						boolean is_urgent = rs.getInt(9) == 1;
						if (is_urgent) {
							Main.numUrgent--;
						}
						else {
							Main.numBugs--;
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
					return true;
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("unresolve") && StringUtils.isNumeric(args[1])) {
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						ResultSet rs;
						int resolved = stmt.executeUpdate("UPDATE neoreports_bugs SET `is_resolved` = 0 WHERE id = " + args[1] + ";");
						if (resolved > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successfully unresolved report!");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7Failed to unresolve report!");
						}
						rs = stmt.executeQuery("SELECT * FROM neoreports_bugs WHERE id = " + args[1] + ";");
						if (rs.next()) {
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "mail send " + rs.getString(3) + " Your bug report of ID " + args[1] + " has been resolved! /reports list");
						}
						boolean is_urgent = rs.getInt(9) == 1;
						if (is_urgent) {
							Main.numUrgent--;
						}
						else {
							Main.numBugs--;
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
					return true;
				}
				else if (args.length > 2 && args[0].equalsIgnoreCase("edit") && StringUtils.isNumeric(args[1])) {
					String desc = args[2];
					for (int i = 0; i < args.length; i++) {
						desc += " " + args[i];
					}
					try{  
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						int edited = stmt.executeUpdate("UPDATE neoreports_bugs SET `comment` = '" + desc + "' WHERE id = " + args[1] + ";");
						if (edited > 0) {
							p.sendMessage("§4[§c§lMLMC§4] §7Successfully edited comment!");
						}
						else {
							p.sendMessage("§4[§c§lMLMC§4] §7Failed to edit comment!");
						}
						con.close();
					}
					catch(Exception e) {
						System.out.println(e);
						p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
					}
					return true;
				}
				else {
					p.sendMessage("§4[§c§lMLMC§4] §cInvalid command!");
					return true;
				}
			}
			else {
				sender.sendMessage("§4[§c§lMLMC§4] §cInvalid command!");
				return true;
			}
		}
		return false;
	}
}