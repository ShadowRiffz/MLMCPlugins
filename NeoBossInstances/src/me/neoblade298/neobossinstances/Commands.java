package me.neoblade298.neobossinstances;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;

import me.neoblade298.neobossinstances.stats.PlayerStat;

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
					UUID uuid = p.getUniqueId();
	
					// Find an open instance
					String instance = main.findInstance(boss);
					if (!instance.equalsIgnoreCase("Not Found") && !instance.equalsIgnoreCase("Failed to connect")) {
						SkillAPI.saveSingle(Bukkit.getPlayer(args[1]));
						p.sendMessage("§4[§c§lBosses§4] §7Starting boss in 3 seconds...");
						try {
							// Connect
							Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser,
									Main.sqlPass);
							Statement stmt = con.createStatement();

							if (main.isDebug) {
								System.out.println("Bosses Debug: INSERT INTO neobossinstances_fights VALUES ('"
										+ uuid + "','" + boss + "','" + instance + "'," + main.settings.getValue(uuid, boss) + ");");
							}
							stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + boss
									+ "','" + instance + "'," + main.settings.getValue(uuid, boss) + ");");
							con.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " sent to boss " + boss + " at instance " + instance + ".");
						
						// Only give cooldown if they've beaten the boss before or it's a raid
						if (main.bossInfo.get(boss).isRaid() || p.hasPermission(main.bossInfo.get(boss).getPermission())) {
							main.cooldowns.get(boss).put(uuid, System.currentTimeMillis());
						}
	
						BukkitRunnable teleport = new BukkitRunnable() {
							public void run() {
								if (main.mainSpawn.getWorld() == null) {
									main.mainSpawn.setWorld(Bukkit.getWorld("Argyll"));
								}
								p.teleport(main.mainSpawn);
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.sendCommand
										.replaceAll("%player%", args[1]).replaceAll("%instance%", instance));
							}
						};
						teleport.runTaskLater(main, 60L);
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
			else if (args.length >= 4 && args[0].equalsIgnoreCase("tp") && !main.isInstance) {
				if (!main.disableFights || (args.length == 5 && args[4].equalsIgnoreCase("force"))) {
					Player p = Bukkit.getPlayer(args[1]);
					SkillAPI.saveSingle(p);
					UUID uuid = p.getUniqueId();
					String boss = WordUtils.capitalize(args[2]);
					String instance = WordUtils.capitalize(args[3]);
					p.sendMessage("§4[§c§lBosses§4] §7Starting boss in 3 seconds...");
					try {
						// Connect
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
						Statement stmt = con.createStatement();
	
						if (main.isDebug) {
							System.out.println("Bosses Debug: INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','"
									+ boss + "','" + instance + "','" + main.settings.getValue(uuid, boss) + "');");
						}
						stmt.executeUpdate("DELETE FROM neobossinstances_fights WHERE uuid = '" + uuid + "';");
						stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + boss + "','"
								+ instance + "','" + main.settings.getValue(uuid, boss) + "');");
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
	
					// Only give cooldown if they've beaten the boss before or it's a raid
					if (main.bossInfo.get(boss).isRaid() || p.hasPermission(main.bossInfo.get(boss).getPermission())) {
						main.cooldowns.get(boss).put(uuid, System.currentTimeMillis());
					}
					if (main.mainSpawn.getWorld() == null) {
						main.mainSpawn.setWorld(Bukkit.getWorld("Argyll"));
					}
					
					BukkitRunnable sendThere = new BukkitRunnable() {
						public void run() {
							p.teleport(main.mainSpawn);
							
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									main.sendCommand.replaceAll("%player%", args[1]).replaceAll("%instance%", instance));
						}
					};
					sendThere.runTaskLater(main, 60L);
				}
				else {
					Bukkit.getPlayer(args[1]).sendMessage("§4[§c§lBosses§4] §7Boss fights are currently disabled!");
				}
				return true;
			}
			// /boss send [player] [boss] [max] [radius]
			else if (args.length == 5 && args[0].equalsIgnoreCase("send")) {
				String boss = args[2];
				Player p = Bukkit.getPlayer(args[1]);
				int max = Integer.parseInt(args[3]);
				double radius = Double.parseDouble(args[4]);
				ArrayList<Entity> nearby = (ArrayList<Entity>) p.getNearbyEntities(radius, radius, radius);
				ArrayList<Player> onCooldown = new ArrayList<Player>(); 
				ArrayList<Player> targets = new ArrayList<Player>(); 
				targets.add(p);
				// Add targets
				for (Entity e : nearby) {
					if (e instanceof Player && !e.getClass().getName().contains("PlayerNPC")) {
						Player target = (Player) e;
		    			targets.add(target);
					}
				}
				
				// Check cooldowns
				for (Player target : targets) {
		    		if (main.cooldowns.get(boss).containsKey(target.getUniqueId())) {
			    		long lastUse = main.cooldowns.get(boss).get(target.getUniqueId());
			    		long currTime = System.currentTimeMillis();
			    		long cooldown = main.bossInfo.get(boss).getCooldown() * 1000;
			    		if (currTime < lastUse + cooldown) {
			    			onCooldown.add(target);
			    		}
		    		}
				}
	    		
				if (targets.size() > max) {
					for (Player target : targets) {
						target.sendMessage("§4[§c§lMLMC§4] §cThere are too many players! Max is §e" + max + "§c, you have §e" + targets.size() + "§c.");
					}
					return true;
				}
				
				String msg = "§4[§c§lMLMC§4] §cThe following players are still on cooldown:";
				if (onCooldown.size() > 0) {
					for (Player cd : onCooldown) {
						msg += "\n§7- §e" + cd.getName() + "§7: " + main.getCooldown(boss, cd);
					}
					for (Player target : targets) {
						target.sendMessage(msg);
					}
					return true;
				}
				
				//Make it find and send to instance
				String instance = main.findInstance(boss);
				if (instance.equals("Not found") || instance.equals("Failed to Connect")) {
					for (Player target : targets) {
						target.sendMessage("§4[§c§lMLMC§4] §cFailed: " + instance + ". Try again later!");
					}
					return true;
				}
				
				// Actually send them
				for (Player target : targets) {
					SkillAPI.saveSingle(target);
					UUID uuid = target.getUniqueId();
					target.sendMessage("§4[§c§lBosses§4] §7Starting boss in 3 seconds...");
					try {
						// Connect
						Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser,
								Main.sqlPass);
						Statement stmt = con.createStatement();

						if (main.isDebug) {
							System.out.println("Bosses Debug: INSERT INTO neobossinstances_fights VALUES ('"
									+ uuid + "','" + boss + "','" + instance + "'," + main.settings.getValue(p.getUniqueId(), boss) + ");");
						}
						// Add boss level here
						stmt.executeUpdate("INSERT INTO neobossinstances_fights VALUES ('" + uuid + "','" + boss
								+ "','" + instance + "'," + main.settings.getValue(p.getUniqueId(), boss) + ");");
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " sent to boss " + boss + " at instance " + instance + ".");
					
					// Only give cooldown if they've beaten the boss before or it's a raid
					if (main.bossInfo.get(boss).isRaid() || p.hasPermission(main.bossInfo.get(boss).getPermission())) {
						main.cooldowns.get(boss).put(uuid, System.currentTimeMillis());
					}

					BukkitRunnable teleport = new BukkitRunnable() {
						public void run() {
							if (main.mainSpawn.getWorld() == null) {
								main.mainSpawn.setWorld(Bukkit.getWorld("Argyll"));
							}
							target.teleport(main.mainSpawn);
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.sendCommand
									.replaceAll("%player%", target.getName()).replaceAll("%instance%", instance));
						}
					};
					teleport.runTaskLater(main, 60L);
				}
				return true;
			}
			// /boss mini [player] [boss] [miniboss]
			else if (args.length == 4 && args[0].equalsIgnoreCase("mini") && main.isInstance) {
				Boss boss = main.bossInfo.get(args[2]);
				RaidBoss rboss = null;
				Player p = Bukkit.getPlayer(args[1]);
				for (RaidBoss rbosses : boss.getRaidBosses()) {
					if (rbosses.getName().equalsIgnoreCase(args[3])) {
						rboss = rbosses;
						break;
					}
				}
				final RaidBoss frboss = rboss;
				
				// Only start fight if fight hasn't been started before
				if (!main.raidBossesFought.contains(rboss.getName())) {
					p.teleport(rboss.getCoords());
					
					// If the boss hasn't been spawned, spawn it (cooldown of 10s)
					if (!main.activeBosses.contains(rboss.getName())) {
						main.activeBosses.add(rboss.getName());
						BukkitRunnable spawnBoss = new BukkitRunnable() {
							public void run() {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), frboss.getCmd().replaceAll("<multiplier>", "" + main.bossMultiplier.get(boss.getName())));
								main.activeBosses.remove(frboss.getName());
								main.raidBossesFought.add(frboss.getName());
							}
						};
						spawnBoss.runTaskLater(main, 200L);
					}
				}
				else {
					p.sendMessage("§4[§c§lMLMC§4] §7This boss has already been fought!");
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
			// /boss reload
			else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage("§4[§c§lBosses§4] §7Bosses reloaded!");
				main.loadConfig();
				return true;
			}
			// /boss resetcd player boss
			else if (args.length == 3 && args[0].equalsIgnoreCase("resetcd") && !main.isInstance) {
				if (main.cooldowns.get(WordUtils.capitalize(args[2]))
						.containsKey(Bukkit.getPlayer(args[1]).getUniqueId())) {
					main.cooldowns.get(WordUtils.capitalize(args[2]))
							.remove(Bukkit.getPlayer(args[1]).getUniqueId());
					sender.sendMessage("§4[§c§lBosses§4] §7Cleared cooldown!");
				}
				return true;
			}
			// /boss resetcds player
			else if (args.length == 2 && args[0].equalsIgnoreCase("resetcds") && !main.isInstance) {
				for (String boss : main.cooldowns.keySet()) {
					if (main.cooldowns.get(boss).containsKey(Bukkit.getPlayer(args[1]).getUniqueId())) {
						main.cooldowns.get(boss).remove(Bukkit.getPlayer(args[1]).getUniqueId());
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
				sender.sendMessage("§4/boss mini [name] [boss] [miniboss]§7- Teleports player to open miniboss");
				sender.sendMessage("§4/boss showstats [boss] [displayname] §7- Shows stats and clears them");
				sender.sendMessage("§4/boss debugstats [boss] [displayname] §7- Shows stats to sender, no clear");
				sender.sendMessage("§4/boss save [name] §7- Manually saves a player");
				sender.sendMessage("§4/boss resetcd [player] [boss]§7- Resets a player cooldown for a boss");
				sender.sendMessage("§4/boss resetcds [player] §7- Resets a player cooldown for all bosses");
				sender.sendMessage("§4/boss resetallcds §7- Resets all player cooldowns");
				sender.sendMessage("§4/boss resetinstances §7- Resets all instances");
				sender.sendMessage("§4/boss send [player] [boss] [max] [radius]§7- Send all nearby players to boss");
				sender.sendMessage("§4/boss addtoboss [player] [boss] §7- Add player to active fight");
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
		else if (args.length == 3 && args[0].equalsIgnoreCase("addtoboss")) {
			String boss = args[2];
			Player p = Bukkit.getPlayer(args[1]);
			if (!main.activeFights.containsKey(args[2])) {
				ArrayList<Player> activeFightsPlayers = new ArrayList<Player>();
				ArrayList<Player> inBossPlayers = new ArrayList<Player>();
				activeFightsPlayers.add(p);
				inBossPlayers.add(p);
				main.activeFights.put(boss, activeFightsPlayers);
				main.inBoss.put(boss, inBossPlayers);
			}
			else {
				main.activeFights.get(boss).add(p);
				main.inBoss.get(boss).add(p);
			}
			main.fightingBoss.put(p.getUniqueId(), boss);
			
			// Recalculate everyone's health bars every time someone joins
			for (Player partyMember : main.activeFights.get(boss)) {
				ArrayList<String> healthList = new ArrayList<String>();
				main.healthbars.put(partyMember.getName(), healthList);
				for (Player bossFighter : main.activeFights.get(boss)) {
					if (!bossFighter.equals(partyMember)) {
						healthList.add(bossFighter.getName());
					}
				}
				Collections.sort(healthList);
			}
			sender.sendMessage("§4[§c§lBosses§4] §7Added §e" + p.getName() + "§7!");
			return true;
		}
		// boss startstats boss
		else if (args[0].equalsIgnoreCase("startstats")) {
			String boss = args[1];
			main.statTimers.put(boss, System.currentTimeMillis());
			
			// Only add players who are still alive
			for (Player p : main.activeFights.get(boss)) {
				main.playerStats.put(p.getName(), new PlayerStat(boss));
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("showstats")) {
			// First, formulate the messages to send
			ArrayList<String> messages = new ArrayList<String>();
			String boss = args[1];
			String display = args[2];
			for (int i = 3; i < args.length; i++) {
				display += " " + args[i];
			}
			display.replaceAll("&", "§");
			display.replaceAll("@", "&");

			// Calculate timer
	        String timer = "n/a";
			if (main.statTimers.containsKey(boss)) {
				long time = System.currentTimeMillis() - main.statTimers.get(boss);
				main.statTimers.remove(boss);
				final long hr = TimeUnit.MILLISECONDS.toHours(time);
		        final long min = TimeUnit.MILLISECONDS.toMinutes(time - TimeUnit.HOURS.toMillis(hr));
		        final long sec = TimeUnit.MILLISECONDS.toSeconds(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		        final long ms = TimeUnit.MILLISECONDS.toMillis(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		        if (hr > 0) {
		        	timer = String.format("%2d:%02d:%02d.%03d", hr, min, sec, ms);
		        }
		        else {
		        	timer = String.format("%2d:%02d.%03d", min, sec, ms);
		        }
			}
			messages.add("§cPost-battle Stats §7(§4§l" + display + "§7) [Time: §c" + timer + "§7]");
			messages.add("§7-----");
			messages.add("§7[§cDamage Dealt §7/ §4Damage Taken §7/ §2Self Healing §7/ §aAlly Healing§7]");
			
			// Calculate each person's stats (and clear them)
			for (Player p : main.inBoss.get(boss)) {
				PlayerStat stats = main.playerStats.get(p.getName());
				if (stats == null) continue;
				String pClass = SkillAPI.getPlayerData(p).getClass("class").getData().getName();
				int damageDealt = (int) Math.round((stats.getDamageDealt() * 100) / 100);
				int damageTaken = (int) Math.round((stats.getDamageTaken() * 100) / 100);
				int selfHeal = (int) Math.round((stats.getSelfHealed() * 100) / 100);
				int allyHeal = (int) Math.round((stats.getAllyHealed() * 100) / 100);

				messages.add("§e" + p.getName() + "§7 (§e" + pClass + "§7) - [§c" + damageDealt + " §7/ §4" + damageTaken + " §7/ §2" + selfHeal + " §7/ §a" + allyHeal + "§7]");
				main.playerStats.remove(p.getName());
			}
			
			// Send stats out to every player
			for (Player receiver : main.inBoss.get(boss)) {
				for (String msg : messages) {
					receiver.sendMessage(msg);
				}
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("debugstats")) {
			// First, formulate the messages to send
			ArrayList<String> messages = new ArrayList<String>();
			String boss = args[1];
			String display = args[2];
			for (int i = 3; i < args.length; i++) {
				display += " " + args[i];
			}
			display.replaceAll("&", "§");
			display.replaceAll("@", "&");

			// Calculate timer
	        String timer = "n/a";
			if (main.statTimers.containsKey(boss)) {
				long time = System.currentTimeMillis() - main.statTimers.get(boss);
				main.statTimers.remove(boss);
				final long hr = TimeUnit.MILLISECONDS.toHours(time);
		        final long min = TimeUnit.MILLISECONDS.toMinutes(time - TimeUnit.HOURS.toMillis(hr));
		        final long sec = TimeUnit.MILLISECONDS.toSeconds(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		        final long ms = TimeUnit.MILLISECONDS.toMillis(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		        if (hr > 0) {
		        	timer = String.format("%2d:%02d:%02d.%03d", hr, min, sec, ms);
		        }
		        else {
		        	timer = String.format("%2d:%02d.%03d", min, sec, ms);
		        }
			}
			messages.add("§cPost-battle Stats §7(§4§l" + display + "§7) [Time:§c" + timer + "§7]");
			messages.add("§7-----");
			messages.add("§7[§cDamage Dealt §7/ §4Damage Taken §7/ §2Self Healing §7/ §aAlly Healing§7]");

			// Calculate each person's stats
			for (Player p : main.inBoss.get(boss)) {
				PlayerStat stats = main.playerStats.get(p.getName());
				if (stats == null) continue;
				String pClass = SkillAPI.getPlayerData(p).getClass("class").getData().getName();
				int damageDealt = (int) Math.round((stats.getDamageDealt() * 100) / 100);
				int damageTaken = (int) Math.round((stats.getDamageTaken() * 100) / 100);
				int selfHeal = (int) Math.round((stats.getSelfHealed() * 100) / 100);
				int allyHeal = (int) Math.round((stats.getAllyHealed() * 100) / 100);

				messages.add("§e" + p.getName() + "§7 (§e" + pClass + "§7) - [§c" + damageDealt + " §7/ §4" + damageTaken + " §7/ §2" + selfHeal + " §7/ §a" + allyHeal + "§7]");
			}
			
			// Send messages
			for (String msg : messages) {
				sender.sendMessage(msg);
			}
			return true;
		}
		else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("return")) {
			if (args.length == 1 && sender instanceof Player) {
				Player p = (Player) sender;
				main.returnToMain(p);
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
				sendBack.runTaskLater(main, 60L);
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
									+ main.bossInfo.get(rs.getString(2)).getDisplayName() + "§7)";
							while (rs.next()) {
								temp += "§7, §e"
										+ Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1))).getName()
										+ " §7(§4" + main.bossInfo.get(rs.getString(2)).getDisplayName() + "§7)";
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
