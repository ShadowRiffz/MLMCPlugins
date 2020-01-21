package me.neoblade298.neogear;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;


public class Commands implements CommandExecutor{
	
	Main main;
	ArrayList<String> validAttrs;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neogear.admin")) {
			
			// Gear get [rarity] [type] [lvl]
			if (args.length == 4 && args[0].equalsIgnoreCase("get") && sender instanceof Player) {
				Player p = (Player) sender;
				String rarity = selectRarity(args[1]);
				String type = selectType(args[2], p);
				int lvl = selectLevel(args[3], (Player) sender);
				
				if (rarity == null) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: rarity");
					return true;
				}
				if (type == null) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: type");
					return true;
				}
				if (lvl == -1) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: level");
					return true;
				}
				int failures = p.getInventory().addItem(main.settings.get(type).get(lvl).generateItem(rarity, lvl)).size();
				if (failures > 0) {
					sender.sendMessage("§4§l[§cMLMC§4] §cFailed to give item, inventory full");
				}
				else {
					sender.sendMessage("§4§l[§cMLMC§4] §7Successfully spawned item");
				}
			}

			// Gear give [player] [rarity] [type] [lvl]
			else if (args.length == 5 && args[0].equalsIgnoreCase("give")) {
				Player p = Bukkit.getPlayer(args[1]);
				String rarity = selectRarity(args[2]);
				String type = selectType(args[3], Bukkit.getPlayer(args[1]));
				int lvl = selectLevel(args[4], p);

				if (p == null) {
					sender.sendMessage("§4§l[§cMLMC§4] §cPlayer not found");
					return true;
				}
				if (rarity == null) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: rarity");
					return true;
				}
				if (type == null) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: type");
					return true;
				}
				if (lvl == -1) {
					sender.sendMessage("§4§l[§cMLMC§4] §cIncorrect format: level");
					return true;
				}
				
				int failures = p.getInventory().addItem(main.settings.get(type).get(lvl).generateItem(rarity, lvl)).size();
				if (failures > 0) {
					sender.sendMessage("§4§l[§cMLMC§4] §cFailed to give item, inventory full");
				}
				else {
					sender.sendMessage("§4§l[§cMLMC§4] §7Successfully spawned item");
				}
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				main.loadConfigs();
				sender.sendMessage("§4§l[§cMLMC§4] §7Successfully reloaded");
			}
			else {
				sender.sendMessage("§4§l[§cMLMC§4] §c/gear get {rarity/set} {type/set/auto} {lvl/auto/range} §7- Spawns you gear");
				sender.sendMessage("§4§l[§cMLMC§4] §c/gear give [player] {rarity/set} {type/set} {lvl/auto/range} §7- Spawns a player gear");
				sender.sendMessage("§4§l[§cMLMC§4] §7{auto} gives gear according to the player's level or class, {range} is formatted as lvl:lvl");
				sender.sendMessage("§4§l[§cMLMC§4] §7Add :lvl to {auto} if you want to set a max level to the gear (auto:30)");
				sender.sendMessage("§4§l[§cMLMC§4] §c/gear reload §7- Reloads config");
			}
		}
		else {
			sender.sendMessage("§4§l[§cMLMC§4] §cYou don't have permission to do that!");
		}
		return true;
	}
	
	private String selectRarity(String param) {
		if (main.raritySets.containsKey(param)) {
			double selector = main.gen.nextDouble();
			for (String item : main.raritySets.get(param)) {
				String[] iParams = item.split(":");
				selector -= Double.parseDouble(iParams[1]);
				if (selector < 0) {
					return iParams[0];
				}
			}
		}
		else if (main.rarities.containsKey(param)) {
			return param;
		}
		return null;
	}
	
	private String selectType(String param, Player p) {
		if (main.itemSets.containsKey(param)) {
			// return main.itemSets.get(param).get(main.gen.nextInt(main.itemSets.get(param).size()));
			// First get the max possible weight
			return main.itemSets.get(param).pickItem();
		}
		else if (main.settings.containsKey(param)) {
			return param;
		}
		else if (param.equalsIgnoreCase("auto")) {
			if (p.hasPermission("filters.neogear")) {
				String pClass = SkillAPI.getPlayerAccountData(p).getActiveData().getClass("class").getData().getName().toLowerCase();
				if (main.itemSets.containsKey(pClass)) {
					return main.itemSets.get(pClass).pickItem();
				}
			}
			else {
				return main.itemSets.get("random").pickItem();
			}
		}
		return null;
	}
	
	private int selectLevel(String param, Player p) {
		int level = -1;
		if (StringUtils.isNumeric(param)) {
			int temp = Integer.parseInt(param);
			if (temp <= main.lvlMax && temp >= 0) {
				return temp - (temp % main.lvlInterval);	// Round to level interval
			}
		}
		else if (param.startsWith("auto")) {
			String[] iParams = param.split(":");
			int pLvl = SkillAPI.getPlayerData(p).getClass("class").getLevel();
			if (iParams.length > 1) {	// Capped generation of gear according to player level
				int max = Integer.parseInt(iParams[1]);
				return pLvl > max ? max : pLvl - (pLvl % main.lvlInterval);
			}
			else {	// Uncapped
				return pLvl - (pLvl % main.lvlInterval);
			}
		}
		else if (param.contains(":")) {
			String[] iParams = param.split(":");
			int min = Integer.parseInt(iParams[0]);
			int max = Integer.parseInt(iParams[1]);
			if (min <= max) {
				if (min < 0) {
					min = 0;
				}
				if (max > main.lvlMax) {
					max = main.lvlMax;
				}
				int lvl = min + main.gen.nextInt(max - min + 1);
				return lvl - (lvl % main.lvlInterval);
			}
		}
		return level;
	}
}