package me.neoblade298.neosapiaddons;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.skills.Skill;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
  {
    if ((sender.hasPermission("neosapiaddons.use")) && (args.length == 3))
    {
    	if (args[0].equalsIgnoreCase("takexp") && (StringUtils.isNumeric(args[2]))) {
        double exp = Integer.parseInt(args[2]);
        PlayerClass data = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("class");
        data.setExp(data.getExp() - exp);
        sender.sendMessage("§4[§c§lMLMC§4] §c" + args[1] + " §7has §e" + data.getExp() + " §7exp remaining.");
        return true;
    	}
    	
    	else if (args[0].equalsIgnoreCase("skillup")) {
    		Skill skill = SkillAPI.getSkill(args[2].replaceAll("_", " "));
        SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).upgradeSkill(skill);
        sender.sendMessage("§4[§c§lMLMC§4] §7Skill upgraded.");
        return true;
    	}
    	
    }
    sender.sendMessage("Something went wrong.");
    return true;
  }
}