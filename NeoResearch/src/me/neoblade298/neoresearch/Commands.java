package me.neoblade298.neoresearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;


public class Commands implements CommandExecutor{
	
	Research main;
	
	public Commands(Research main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("mycommand.staff") || sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§c/nr reload");
				sender.sendMessage("§c/nr createbook(alias) [player] [mob] [points] (display)");
				sender.sendMessage("§c/nr spawnbook(alias) [player] [mob] [points] (display)");
				sender.sendMessage("§c/nr givepoints/kills(alias) [player] [mob] [amt] (display)");
				sender.sendMessage("§c/nr setpoints/kills [player] [mob] [amt]");
				sender.sendMessage("§c/nr setlevel [player] [amt]");
				sender.sendMessage("§c/nr setexp [player] [amt]");
				sender.sendMessage("§c/nr takegoal [player] [goal]");
				sender.sendMessage("§c/nr inspect [player] [mob]");
				sender.sendMessage("§c/nr inspectgoals [player]");
				sender.sendMessage("§c/nr updateattrs [player]");
			}

			// /nr reload
			else if (args[0].equalsIgnoreCase("reload")) {
				main.loadConfig();
				sender.sendMessage("§4[§c§lMLMC§4] §7Reloaded config");
			}

			// /nr spawnbook [player] [internalmob] [point amt]
			else if (args[0].equalsIgnoreCase("spawnbook")) {
				Player p = Bukkit.getPlayer(args[1]);
				if (MythicMobs.inst().getMobManager().getMythicMob(args[2]) == null) {
					sender.sendMessage("§4[§c§lMLMC§4] §cInvalid mob");
					return true;
				}
				String display = MythicMobs.inst().getMobManager().getMythicMob(args[2]).getDisplayName().get();
				int amt = Integer.parseInt(args[3]);

				ItemStack item = new ItemStack(Material.BOOK);
				ItemMeta meta = item.getItemMeta();

				meta.setDisplayName("§9Research Book");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Grants§e " + amt + " §7research points for");
				lore.add(display);

				meta.setCustomModelData(100);
				meta.setLore(lore);
				item.setItemMeta(meta);
				NBTItem nbti = new NBTItem(item);
				nbti.setString("internalmob", args[2]);
				p.getInventory().addItem(nbti.getItem());
				sender.sendMessage("§4[§c§lMLMC§4] §7Spawned research book " + display + " §7to player §e" + p.getName());
			}
			
			// /nr spawnbookalias [player] [mobalias] [point amt] [display]
			else if (args[0].equalsIgnoreCase("spawnbookalias")) {
				Player p = Bukkit.getPlayer(args[1]);
				String display = args[4];
				for (int i = 5; i < args.length; i++) {
					display += " " + args[i];
				}
				display.replaceAll("&", "§");
				int amt = Integer.parseInt(args[3]);

				ItemStack item = new ItemStack(Material.BOOK);
				ItemMeta meta = item.getItemMeta();

				meta.setDisplayName("§9Research Book");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Grants§e " + amt + " §7research points for");
				lore.add(display);

				meta.setCustomModelData(100);
				meta.setLore(lore);
				item.setItemMeta(meta);
				NBTItem nbti = new NBTItem(item);
				nbti.setString("internalmob", args[2]);
				p.getInventory().addItem(nbti.getItem());
				sender.sendMessage("§4[§c§lMLMC§4] §7Spawned research book " + display + " §7to player §e" + p.getName());
			}

			// /nr createbook [player] [internalmob] [point amt]
			else if (args[0].equalsIgnoreCase("createbook")) {
				Player p = Bukkit.getPlayer(args[1]);
				UUID uuid = p.getUniqueId();
				if (MythicMobs.inst().getMobManager().getMythicMob(args[2]) == null) {
					sender.sendMessage("§4[§c§lMLMC§4] §cInvalid mob");
					return true;
				}
				String display = MythicMobs.inst().getMobManager().getMythicMob(args[2]).getDisplayName().get();
				int amt = Integer.parseInt(args[3]);
				
				// First check if the player has enough research points
				int currentPoints = main.playerStats.get(uuid).getResearchPoints().get(args[2]);
				int min = -1;
				for (ResearchItem rItem : main.mobMap.get(args[2])) {
					int goal = rItem.getGoals().get(args[2]);
					if (min < goal && goal < currentPoints) min = goal;
				}

				if (currentPoints - amt < min) {
					p.sendMessage("§4[§c§lMLMC§4] §cYou need at least §e" + (min + amt) + "§cresearch points to do this!");
					return true;
				}
				
				main.playerStats.get(uuid).getResearchPoints().put(args[2], currentPoints - amt);
				ItemStack item = new ItemStack(Material.BOOK);
				ItemMeta meta = item.getItemMeta();

				meta.setDisplayName("§9Research Book");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Grants§e " + amt + " §7research points for");
				lore.add(display);

				meta.setCustomModelData(100);
				meta.setLore(lore);
				item.setItemMeta(meta);
				NBTItem nbti = new NBTItem(item);
				nbti.setString("internalmob", args[2]);
				p.getInventory().addItem(nbti.getItem());
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave research book " + display + " §7to player §e" + p.getName());
			}

			// /nr createbookalias [player] [alias] [point amt] [display]
			else if (args[0].equalsIgnoreCase("createbookalias")) {
				Player p = Bukkit.getPlayer(args[1]);
				UUID uuid = p.getUniqueId();
				String display = args[4];
				for (int i = 5; i < args.length; i++) {
					display += " " + args[i];
				}
				display.replaceAll("&", "§");
				int amt = Integer.parseInt(args[3]);
				
				// First check if the player has enough research points
				int currentPoints = main.playerStats.get(uuid).getResearchPoints().get(args[2]);
				int min = -1;
				for (ResearchItem rItem : main.mobMap.get(args[2])) {
					int goal = rItem.getGoals().get(args[2]);
					if (min < goal && goal < currentPoints) min = goal;
				}

				if (currentPoints - amt < min) {
					p.sendMessage("§4[§c§lMLMC§4] §cYou need at least §e" + (min + amt) + "§cresearch points to do this!");
					return true;
				}
				
				main.playerStats.get(uuid).getResearchPoints().put(args[2], currentPoints - amt);
				ItemStack item = new ItemStack(Material.BOOK);
				ItemMeta meta = item.getItemMeta();

				meta.setDisplayName("§9Research Book");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Grants§e " + amt + " §7research points for");
				lore.add(display);

				meta.setCustomModelData(100);
				meta.setLore(lore);
				item.setItemMeta(meta);
				NBTItem nbti = new NBTItem(item);
				nbti.setString("internalmob", args[2]);
				p.getInventory().addItem(nbti.getItem());
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave research book " + display + " §7to player §e" + p.getName());
			}
			
			// /nr givepoints [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("givepoints")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				main.giveResearchPoints(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave points for " + args[2] + " §7to player §e" + p.getName());
			}
			// /nr givepointsalias [player] [internalmob] [amount] [display]
			else if (args[0].equalsIgnoreCase("givepointsalias")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				String display = args[4];
				for (int i = 5; i < args.length; i++) {
					display += " " + args[i];
				}
				main.giveResearchPointsAlias(p, amount, args[2], display);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave points for " + args[2] + " §7to player §e" + p.getName());
			}
			// /nr givekills [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("givekills")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				main.giveResearchKills(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave points for " + args[2] + " §7to player §e" + p.getName());
			}
			// /nr setpoints [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("setpoints")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				main.setResearchPoints(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Set points for " + args[2] + " §7to §e" + amount);
			}
			// /nr setkills [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("setkills")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				main.setResearchKills(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Set kills for " + args[2] + " §7to §e" + amount);
			}
			// /nr setlevel [player] [amount]
			else if (args[0].equalsIgnoreCase("setlevel")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[2]);
				main.playerStats.get(p.getUniqueId()).setLevel(amount);
				sender.sendMessage("§4[§c§lMLMC§4] §7Set level for " + p.getName() + " §7to §e" + amount);
			}
			// /nr setexp [player] [amount]
			else if (args[0].equalsIgnoreCase("setexp")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[2]);
				main.playerStats.get(p.getUniqueId()).setExp(amount);
				sender.sendMessage("§4[§c§lMLMC§4] §7Set exp for " + p.getName() + " §7to §e" + amount);
			}
			// /nr inspect [player] [internalmob]
			else if (args[0].equalsIgnoreCase("inspect")) {
				Player p = Bukkit.getPlayer(args[1]);
				String mob = args[2];
				PlayerStats stats = main.getPlayerStats(p);
				sender.sendMessage("§4[§c§lMLMC§4] §e" + p.getName() + " §7has §e" + stats.getResearchPoints().get(mob) +
						" §7research points and §e" + stats.getMobKills().get(mob) + " §7kills for this mob.");
			}
			// /nr inspectgoals [player]
			else if (args[0].equalsIgnoreCase("inspectgoals")) {
				Player p = Bukkit.getPlayer(args[1]);
				PlayerStats stats = main.getPlayerStats(p);
				String msg = new String("§4[§c§lMLMC§4] §e" + p.getName() + " §7has: §e");
				Iterator<String> iter = stats.getCompletedResearchItems().iterator();
				while (iter.hasNext()) {
					msg += iter.next() + " ";
				}
				sender.sendMessage(msg);
			}
			// /nr takegoal [player] [goal]
			else if (args[0].equalsIgnoreCase("takegoal")) {
				Player p = Bukkit.getPlayer(args[1]);
				PlayerStats stats = main.getPlayerStats(p);
				Iterator<String> iter = stats.getCompletedResearchItems().iterator();
				while (iter.hasNext()) {
					if (iter.next().contains(args[2])) {
						sender.sendMessage("§4[§c§lMLMC§4] §7Successfully removed goal");
						iter.remove();
						break;
					}
				}
			}
			// /nr updateattrs [player]
			else if (args[0].equalsIgnoreCase("updateattrs")) {
				Player p = Bukkit.getPlayer(args[1]);
				main.updateBonuses(p);
			}
			return true;
		}
		return false;
	}
}