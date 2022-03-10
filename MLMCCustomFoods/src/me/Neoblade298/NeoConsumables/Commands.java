package me.Neoblade298.NeoConsumables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoConsumables.objects.FoodConsumable;


public class Commands implements CommandExecutor, TabCompleter {
	
	Consumables main;
	
	public Commands(Consumables main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("mycommand.staff")) {
			if (args.length == 0) {
				sender.sendMessage("§7Permission: mycommand.staff");
				sender.sendMessage("§c/cons give boss [player]");
				sender.sendMessage("§c/cons reload");
			}
			// /cons give [player] boss/[key] [amount]
			else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
				Player p = (Player) sender;
				int offset = 0;
				int amt = 1;
				if (Bukkit.getPlayer(args[1]) != null) {
					p = Bukkit.getPlayer(args[1]);
					offset++;
				}
				
				if (args[1 + offset].equalsIgnoreCase("boss")) {
					p.getInventory().addItem(Items.getBossChestToken(p, System.currentTimeMillis()));
					sender.sendMessage("§4[§c§lMLMC§4] §7Successfully gave boss token to §e" + p.getName());
					return true;
				}
				else {
					FoodConsumable cons = (FoodConsumable) Consumables.consumables.get(args[1 + offset]);
					
					if (args.length > 2 + offset) {
						amt = Integer.parseInt(args[2 + offset]);
					}
					
					if (cons != null) {
						p.getInventory().addItem(cons.getItem(amt));
						sender.sendMessage("§4[§c§lMLMC§4] §7Successfully gave " + cons.getDisplay() + " to §e" + p.getName());
						return true;
					}
					else {
						sender.sendMessage("§4[§c§lMLMC§4] §cInvalid key!");
						return true;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage("§4[§c§lMLMC§4] §7Reloaded consumables");
				main.reload();
				return true;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args[0].equalsIgnoreCase("get") && args.length == 2) {
			return new ArrayList<String>(Consumables.food.keySet());
		}
		return null;
	}
}