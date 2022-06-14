package me.neoblade298.neosapiaddons.methods;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neosapiaddons.PointSet;
import me.neoblade298.neosapiaddons.SAPIAddons;

public class ResetClass {
	public static void resetPlayer(Player p, CommandSender s) {
		String classname = SkillAPI.getPlayerData(p).getClass("class").getData().getName();
		String classAfter = new String();

		if (classname.equalsIgnoreCase("Swordsman") || classname.equalsIgnoreCase("Mage")
				|| classname.equalsIgnoreCase("Thief") || classname.equalsIgnoreCase("Archer")) {
			resetTier1(p, "Beginner");
			classAfter = "N/A";
		}
		else if (classname.equalsIgnoreCase("Paladin") || classname.equalsIgnoreCase("Berserker")
				|| classname.equalsIgnoreCase("Duelist")) {
			resetTier2(p, "Swordsman");
			classAfter = "Swordsman";
		}
		else if (classname.equalsIgnoreCase("Ranger") || classname.equalsIgnoreCase("Hunter")
				|| classname.equalsIgnoreCase("Vanguard")) {
			resetTier2(p, "Archer");
			classAfter = "Archer";
		}
		else if (classname.equalsIgnoreCase("Archmage") || classname.equalsIgnoreCase("Warlock")
				|| classname.equalsIgnoreCase("Bishop")) {
			resetTier2(p, "Mage");
			classAfter = "Mage";
		}
		else if (classname.equalsIgnoreCase("Assassin") || classname.equalsIgnoreCase("Diversionist")
				|| classname.equalsIgnoreCase("Infiltrator")) {
			resetTier2(p, "Thief");
			classAfter = "Thief";
		}

		s.sendMessage(
				"§7Reset the class of §e" + p.getName() + " §7from §6" + classname + " §7to §6" + classAfter + "§7.");
	}

	private static void resetTier1(Player p, String rclass) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.resetAttribs();
		data.reset("class");
		data.init(p);
	}

	private static void resetTier2(Player p, String rclass) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.resetAttribs();
		data.reset("class");
		data.profess(SkillAPI.getClass(rclass));
		PlayerClass pclass = data.getClass("class");

		int currentLv = pclass.getData().getMaxLevel();
		pclass.setLevel(currentLv);
		PointSet setSP = SAPIAddons.skillPoints.get(currentLv);
		PointSet setAP = SAPIAddons.attrPoints.get(currentLv);
		int expectedSP = setSP.getBasePoints() + setSP.getPointsPerLvl() * (currentLv - setSP.getBaseLvl());
		int expectedAP = setAP.getBasePoints() + setAP.getPointsPerLvl() * (currentLv - setAP.getBaseLvl());

		pclass.setPoints(expectedSP);
		data.setAttribPoints(expectedAP);

		data.init(p);
	}
}
