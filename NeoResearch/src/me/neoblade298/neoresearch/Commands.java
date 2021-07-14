package me.neoblade298.neoresearch;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
				sender.sendMessage("§c/nr createbook [player] [mob] [amt]");
				sender.sendMessage("§c/nr spawnbook [player] [mob] [amt]");
				sender.sendMessage("§c/nr givepoints [player] [mob] [amt]");
				sender.sendMessage("§c/nr givekills [player] [mob] [amt]");
				sender.sendMessage("§c/nr inspect [player] [mob]");
				sender.sendMessage("§c/nr updateattrs [player] [mob]");
			}

			// /nr reload
			else if (args[0].equalsIgnoreCase("reload")) {
				main.loadConfig();
				sender.sendMessage("§4[§c§lMLMC§4] §7Reloaded config");
			}

			// /nr spawnbook [player] [internalmob] [amount]
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
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave research book " + display + " §7to player §e" + p.getName());
			}

			// /nr createbook [player] [internalmob] [amount]
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
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave research book " + display + " §7to player §e" + p.getName());
			}
			
			// /nr givepoints [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("givepoints")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				if (MythicMobs.inst().getMobManager().getMythicMob(args[2]) == null) {
					sender.sendMessage("§4[§c§lMLMC§4] §cInvalid mob");
					return true;
				}
				main.giveResearchPoints(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave points for " + args[1] + " §7to player §e" + p.getName());
			}
			// /nr givekills [player] [internalmob] [amount]
			else if (args[0].equalsIgnoreCase("givekills")) {
				Player p = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[3]);
				if (MythicMobs.inst().getMobManager().getMythicMob(args[2]) == null) {
					sender.sendMessage("§4[§c§lMLMC§4] §cInvalid mob");
					return true;
				}
				main.giveResearchKills(p, amount, args[2]);
				sender.sendMessage("§4[§c§lMLMC§4] §7Gave points for " + args[1] + " §7to player §e" + p.getName());
			}
			// /nr givekills [player] [internalmob]
			else if (args[0].equalsIgnoreCase("inspect")) {
				Player p = Bukkit.getPlayer(args[1]);
				if (MythicMobs.inst().getMobManager().getMythicMob(args[2]) == null) {
					sender.sendMessage("§4[§c§lMLMC§4] §cInvalid mob");
					return true;
				}
				String mob = args[2];
				PlayerStats stats = main.getPlayerStats(p);
				sender.sendMessage("§4[§c§lMLMC§4] §e" + p.getName() + " §7has §e" + stats.getResearchPoints().get(mob) +
						" §7research points and §e" + stats.getMobKills().get(mob) + " §7kills for this mob.");
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