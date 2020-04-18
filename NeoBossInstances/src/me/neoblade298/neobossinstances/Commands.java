package me.neoblade298.neobossinstances;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;

public class Commands implements CommandExecutor {
	private Main main = null;

	public Commands(Main plugin) {
		main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("bossinstances.admin") || sender.isOp()) {
			// /boss tp player nameofboss
			if (args.length == 3 && args[0].equalsIgnoreCase("tp") && !main.isInstance) {
				if (!main.disableFights) {
					String boss = WordUtils.capitalize(args[2]);
					Player p = Bukkit.getPlayer(args[1]);
					String uuid = p.getUniqueId().toString();
	
					// Find an open instance
					String instance = main.findInstance(boss);
					if (!instance.equalsIgnoreCase("Not Found") && !instance.equalsIgnoreCase("Failed to connect")) {
						SkillAPI.saveSingle(Bukkit.getPlayer(args[1]));
						
						// Only give cooldown if they've beaten the boss before
						if (p.hasPermission(main.bossInfo.get(boss).getPermission())) {
							main.cooldowns.get(boss).put(uuid, System.currentTimeMillis());
						}
	
						// Wait for everyone to enter, then update sql so the instance still shows as
						// empty until everyone leaves
						BukkitRunnable addSql = new BukkitRunnable() {
							public void run() {
								p.teleport(main.mainSpawn);
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.sendCommand
										.replaceAll("%player%", args[1]).replaceAll("%instance%", instance));
								try {
									// Connect
									Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser,
											Main.sqlPass);
									Statement stmt = con.createStatement();
	
									if (main.isDebug) {
										System.out.println("Bosses Debug: INSERT INTO neobossinstances_fights VALUES ('"
												+ uuid + "','" + boss + "','" + instance + "');");
									}
									stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + boss
											+ "','" + instance + "');");
									con.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						addSql.runTaskLater(main, 40L);
					}
					else if (instance.equalsIgnoreCase("Not found")) {
						Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7No available instances! Please wait until one is available! §c/boss instances");
					}
					else {
						Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7Failed to connect!");
					}
				}
				else {
					Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7Boss fights are currently disabled!");
				}
				return true;
			}
			// /boss tp player nameofboss instance
			else if (args.length == 4 && args[0].equalsIgnoreCase("tp") && !main.isInstance) {
				if (!main.disableFights) {
					SkillAPI.saveSingle(Bukkit.getPlayer(args[1]));
					String uuid = Bukkit.getPlayer(args[1]).getUniqueId().toString();
					String boss = WordUtils.capitalize(args[2]);
					String instance = WordUtils.capitalize(args[3]);
	
					main.cooldowns.get(boss).put(uuid, System.currentTimeMillis());
					Bukkit.getPlayer(args[1]).teleport(main.mainSpawn);
	
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							main.sendCommand.replaceAll("%player%", args[1]).replaceAll("%instance%", instance));
					try {
						// Connect
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
	
						if (main.isDebug) {
							System.out.println("Bosses Debug: INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','"
									+ boss + "','" + instance + "');");
						}
						stmt.executeUpdate("DELETE FROM neobossinstances_fights WHERE uuid = '" + uuid + "';");
						stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + boss + "','"
								+ instance + "');");
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else {
					Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7Boss fights are currently disabled!");
				}
				return true;
			}
			// /boss disable
			else if (args.length == 1 && args[0].equalsIgnoreCase("disable") && !main.isInstance) {
				main.disableFights = !main.disableFights;
				if (main.disableFights) sender.sendMessage("§4[§c§lBosses§4] §7Boss fights disabled!");
				else sender.sendMessage("§4[§c§lBosses§4] §7Boss fights enabled!");
				return true;
			}
			// /boss resetcd player boss
			else if (args.length == 3 && args[0].equalsIgnoreCase("resetcd") && !main.isInstance) {
				if (main.cooldowns.get(WordUtils.capitalize(args[2]))
						.containsKey(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
					main.cooldowns.get(WordUtils.capitalize(args[2]))
							.remove(Bukkit.getPlayer(args[1]).getUniqueId().toString());
					sender.sendMessage("§4[§c§lBosses§4] §7Cleared cooldown!");
				}
				return true;
			}
			// /boss resetcds player
			else if (args.length == 2 && args[0].equalsIgnoreCase("resetcds") && !main.isInstance) {
				for (String boss : main.cooldowns.keySet()) {
					if (main.cooldowns.get(boss).containsKey(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
						main.cooldowns.get(boss).remove(Bukkit.getPlayer(args[1]).getUniqueId().toString());
					}
				}
				sender.sendMessage("§4[§c§lBosses§4] §7Cleared all cooldowns for player!");
				return true;
			}
			// /boss resetallcds
			else if (args.length == 1 && args[0].equalsIgnoreCase("resetallcds") && !main.isInstance) {
				for (String boss : main.cooldowns.keySet()) {
					main.cooldowns.get(boss).clear();
				}
				sender.sendMessage("§4[§c§lBosses§4] §7Cleared all cooldowns!");
				return true;
			}
			// /boss resetinstances
			else if (args.length == 1 && args[0].equalsIgnoreCase("resetinstances") && !main.isInstance) {
				try {
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();

					// First clear all the cooldowns on the SQL currently
					int deleted = stmt.executeUpdate("delete from neobossinstances_fights;");
					sender.sendMessage("§4[§c§lBosses§4] §7Deleted §e" + deleted + " §7instances!");
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			// /boss permissions
			else if (args.length == 1 && args[0].equalsIgnoreCase("permissions") && !main.isInstance) {
				sender.sendMessage("§4bossinstances.admin §7- All permissions");
				sender.sendMessage("§4bossinstances.exemptleave §7- Do not teleport player to spawn on leaving");
				sender.sendMessage("§4bossinstances.exemptjoin §7- Do not teleport player to boss fight on joining");
				return true;
			}
			// /boss debug
			else if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
				main.isDebug = !main.isDebug;
				sender.sendMessage("§4[§c§lBosses§4] §7Set debug to §e" + main.isDebug + "§7!");
				return true;
			}
			// /boss debug [player] [boss] (adds player to active boss)
			else if (args.length == 3 && args[0].equalsIgnoreCase("debug")) {
				if (main.activeFights.containsKey(args[2])) {
					ArrayList<Player> list = main.activeFights.get(args[2]);
					list.add(Bukkit.getPlayer(args[1]));
					main.activeFights.put(args[2], list);
				}
				else {
					ArrayList<Player> list = new ArrayList<Player>();
					list.add(Bukkit.getPlayer(args[1]));
					main.activeFights.put(args[2], list);
				}
				sender.sendMessage("§4[§c§lBosses§4] §7Added player to boss!");
				return true;
			}
			// /boss active
			else if (args.length == 1 && args[0].equalsIgnoreCase("active")) {
				for (Entry<String, ArrayList<Player>> entry : main.activeFights.entrySet()) {
					String toSend = "§4" + entry.getKey() + "§7: ";
					for (Player player : entry.getValue()) {
						toSend += player.getName() + " ";
					}
					sender.sendMessage(toSend);
				}
				return true;
			}
			// /boss resetactive
			else if (args.length == 1 && args[0].equalsIgnoreCase("resetactive")) {
				main.activeFights.clear();
				sender.sendMessage("§4[§c§lBosses§4] §7Reset active fights!");
				return true;
			}
		}

		if (args.length == 0) {
			sender.sendMessage("§7=== §4[§c§lBosses§4] §7===");
			sender.sendMessage("§c/boss cd [name] §7- Shows cooldown of a specific boss");
			sender.sendMessage("§c/boss cd all §7- Shows cooldown of all bosses");
			sender.sendMessage("§c/boss instances [boss] §7- Shows instances for boss");
			sender.sendMessage("§c/boss instances §7- Shows all players in instances");
			sender.sendMessage("§c/boss return §7- Returns you safely to the main server");
			if (sender.hasPermission("bossinstances.admin")) {
				sender.sendMessage("§4/boss tp [name] [boss]§7- Teleports player to open boss instance");
				sender.sendMessage("§4/boss disable §7- Toggles whether boss fights are enabled");
				sender.sendMessage("§4/boss save [name] §7- Manually saves a player");
				sender.sendMessage("§4/boss resetcd [player] [boss]§7- Resets a player cooldown for a boss");
				sender.sendMessage("§4/boss resetcds [player] §7- Resets a player cooldown for all bosses");
				sender.sendMessage("§4/boss resetallcds §7- Resets all player cooldowns");
				sender.sendMessage("§4/boss resetinstances §7- Resets all instances");
				sender.sendMessage("§4/boss return {player} §7- Returns player or command user to main server");
				sender.sendMessage("§4/boss permissions §7- Returns a list of plugin permissions");
				sender.sendMessage(
						"§4/boss active §7- Debug, returns list of who's in what fights (ONLY INSTANCE)");
				sender.sendMessage(
						"§4/boss resetactive §7- Resets local active fights");
			}
			return true;
		}
		else if (args.length == 2 && args[0].equalsIgnoreCase("save")) {
			sender.sendMessage("§4[§c§lBosses§4] §e" + args[1] + "§7 saved!");
			SkillAPI.saveSingle(Bukkit.getPlayer(args[1]));
			return true;
		}
		else if (args.length == 2 && args[0].equalsIgnoreCase("cd") && sender instanceof Player) {
			if (!main.isInstance) {
				Player p = (Player) sender;
				String name = WordUtils.capitalize(args[1]);
				if (name.equalsIgnoreCase("all")) {
					for (String boss : main.bossNames) {
						main.getCooldown(boss, p);
					}
					return true;
				}
				else {
					return main.getCooldown(name, p);
				}
			}
			else {
				sender.sendMessage("§4[§c§lBosses§4] §7You can only check cooldowns on the main server!");
			}
			return true;
		}
		else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("return")) {
			if (args.length == 1 && sender instanceof Player) {
				Player p = (Player) sender;
				SkillAPI.saveSingle(p);
				sender.sendMessage("§4[§c§lBosses§4] §7Sending you back...");
				BukkitRunnable sendBack = new BukkitRunnable() {
					public void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								main.returnCommand.replaceAll("%player%", p.getName()));
					}
				};
				sendBack.runTaskLater(main, 20L);
				return true;
			}
			else {
				SkillAPI.saveSingle(Bukkit.getPlayer(args[1]));
				sender.sendMessage("§4[§c§lBosses§4] §7Sending them back...");
				BukkitRunnable sendBack = new BukkitRunnable() {
					public void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								main.returnCommand.replaceAll("%player%", args[1]));
					}
				};
				sendBack.runTaskLater(main, 20L);
				return true;
			}
		}
		// /boss instances
		else if (args.length == 1 && args[0].equalsIgnoreCase("instances")) {
			if (!main.isInstance) {
				try {
					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
					Statement stmt = con.createStatement();
					ResultSet rs;

					// Find available instance
					for (String instance : main.instanceNames) {
						rs = stmt.executeQuery(
								"SELECT * FROM neobossinstances_fights WHERE instance = '" + instance + "';");

						// Empty instance
						if (!rs.next()) {
							sender.sendMessage("§e" + instance + "§7: Empty");
						}
						else {
							String temp = "§e" + instance + "§7: §e"
									+ Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName() + " §7(§4"
									+ rs.getString(2) + "§7)";
							while (rs.next()) {
								temp += "§7, §e"
										+ Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName()
										+ " §7(§4" + rs.getString(2) + "§7)";
							}
							if (temp != null) {
								sender.sendMessage(temp);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				sender.sendMessage("§4[§c§lBosses§4] §7You can only check instances on the main server!");
			}
			return true;
		}
		else if (args.length == 2 && args[0].equalsIgnoreCase("instances") && sender instanceof Player) {
			if (!main.isInstance) {
				Player p = (Player) sender;
				String name = WordUtils.capitalize(args[1]);
				if (main.cooldowns.keySet().contains(name)) {
					try {
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
						ResultSet rs;

						// Find available instance
						for (String instance : main.instanceNames) {
							rs = stmt.executeQuery("SELECT * FROM neobossinstances_fights WHERE boss = '" + name
									+ "' AND instance = '" + instance + "';");

							// Empty instance
							if (!rs.next()) {
								p.sendMessage("§e" + instance + "§7: Empty");
							}
							else {
								String temp = "§e" + instance + "§7: §e"
										+ Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName();
								while (rs.next()) {
									temp += "§7, §e"
											+ Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName();
								}
								if (temp != null) {
									p.sendMessage(temp);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					p.sendMessage("§4[§c§lBosses§4] §7Invalid boss!");
				}
			}
			else {
				sender.sendMessage("§4[§c§lBosses§4] §7You can only check instances on the main server!");
			}
			return true;
		}
		return false;
	}
}
