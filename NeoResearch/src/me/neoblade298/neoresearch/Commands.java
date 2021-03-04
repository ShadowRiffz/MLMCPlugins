package me.neoblade298.neoresearch;

import java.util.ArrayList;

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
			// /nr givebook [player] [internalmob] [amount]
			if (args.length == 0) {
				sender.sendMessage("§c/nr givebook [player] [mob] [amt]");
			}

			else if (args[0].equalsIgnoreCase("givebook")) {
				Player p = Bukkit.getPlayer(args[1]);
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
			}
			return true;
		}
		return false;
	}
}