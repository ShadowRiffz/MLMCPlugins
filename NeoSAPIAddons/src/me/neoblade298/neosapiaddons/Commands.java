package me.neoblade298.neosapiaddons;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.skills.Skill;

import me.neoblade298.neosapiaddons.methods.ProfessClass;
import me.neoblade298.neosapiaddons.methods.ResetClass;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	SAPIAddons main;
	public Commands(SAPIAddons main) {
		this.main = main;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neosapiaddons.use")) {
			if (args.length == 0) {
				sender.sendMessage("§c/nsapi takexp [player] [exp] - remove class exp");
				sender.sendMessage("§c/nsapi skillup [player] [skill] - increase a skill");
				sender.sendMessage("§c/nsapi points [player] [classtype] [amount] - give class points");
				sender.sendMessage("§c/nsapi refresh [player] - update equips");
				sender.sendMessage("§c/nsapi profess [player] [class] - safely profess player to next tier");
				sender.sendMessage("§c/nsapi reset [player] - reset player to previous tier");
				sender.sendMessage("§c/nsapi attr [attr] - check attributes, even hidden ones");
				return true;
			}
			if (args[0].equalsIgnoreCase("takexp") && (StringUtils.isNumeric(args[2]))) {
				double exp = Integer.parseInt(args[2]);
				PlayerClass data = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("class");
				data.setExp(data.getExp() - exp);
				sender.sendMessage("§4[§c§lMLMC§4] §c" + args[1] + " §7has §e" + data.getExp() + " §7exp remaining.");
				return true;
			}
			else if (args[0].equalsIgnoreCase("reset")) {
				ResetClass.resetPlayer(Bukkit.getPlayer(args[1]), sender);
				return true;
			}
			else if (args[0].equalsIgnoreCase("profess")) {
				ProfessClass.professPlayer(Bukkit.getPlayer(args[1]), sender, args[2]);
				return true;
			}
			else if (args[0].equalsIgnoreCase("skillup")) {
				Skill skill = SkillAPI.getSkill(args[2].replaceAll("_", " "));
				SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).upgradeSkill(skill, true);
				sender.sendMessage("§4[§c§lMLMC§4] §7Skill upgraded.");
				return true;
			}

			else if (args[0].equalsIgnoreCase("debug")) {
				Skill skill = SkillAPI.getSkill(args[2].replaceAll("_", " "));
				SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).upgradeSkill(skill, true);
				sender.sendMessage("§4[§c§lMLMC§4] §7Skill upgraded.");
				return true;
			}

			// neosapiaddons points [player] [classtype] [amount]
			else if (args[0].equalsIgnoreCase("points")) {
				SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass(args[2]).givePoints(Integer.parseInt(args[3]));
				sender.sendMessage("§4[§c§lMLMC§4] §7Points given.");
				return true;
			}
			else if (args[0].equalsIgnoreCase("refresh")) {
	            Player p = Bukkit.getPlayer(args[1]);
                SkillAPI.getPlayerData(p).getEquips().update(p);
                return true;
			}
			else if (args[0].equalsIgnoreCase("attr")) {
	            Player p = (Player) sender;
                sender.sendMessage("§4[§c§lMLMC§4] §7Attribute has: " + SkillAPI.getPlayerData(p).getAttribute(args[1]) + ".");
                return true;
			}

		}
		sender.sendMessage("Something went wrong.");
		return true;
	}
}