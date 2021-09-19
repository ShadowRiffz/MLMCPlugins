package me.neoblade298.neoresetclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoResetClass Enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// Get command listener
		this.getCommand("neoresetclass").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoResetClass Disabled");
	}

	public void resetPlayer(Player p, CommandSender s) {
		  String classname = SkillAPI.getPlayerData(p).getClass("class").getData().getName();
		  String classAfter = new String();
		  
		  if(classname.equalsIgnoreCase("Swordsman") ||
				  classname.equalsIgnoreCase("Mage") ||
				  classname.equalsIgnoreCase("Thief") ||
				  classname.equalsIgnoreCase("Archer")) {
			  resetTier1(p, "Beginner");
			  classAfter = "Beginner";
		  }
		  else if(classname.equalsIgnoreCase("Paladin") ||
				  classname.equalsIgnoreCase("Berserker") ||
				  classname.equalsIgnoreCase("Duelist")) {
			  resetTier2(p, "Swordsman");
			  classAfter = "Swordsman";
		  }
		  else if(classname.equalsIgnoreCase("Ranger") ||
				  classname.equalsIgnoreCase("Hunter") ||
				  classname.equalsIgnoreCase("Vanguard")) {
			  resetTier2(p, "Archer");
			  classAfter = "Archer";
		  }
		  else if(classname.equalsIgnoreCase("Archmage") ||
				  classname.equalsIgnoreCase("Warlock") ||
				  classname.equalsIgnoreCase("Bishop")) {
			  resetTier2(p, "Mage");
			  classAfter = "Mage";
		  }
		  else if(classname.equalsIgnoreCase("Assassin") ||
				  classname.equalsIgnoreCase("Diversionist") ||
				  classname.equalsIgnoreCase("Infiltrator")) {
			  resetTier2(p, "Thief");
			  classAfter = "Thief";
		  }

		  s.sendMessage("§7Reset the class of §e" + p.getName() + " §7from §6" + classname + " §7to §6" + classAfter + "§7.");
	  }

	public void resetTier1(Player p, String rclass) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.resetAttribs();
		data.reset("class");
		data.profess(SkillAPI.getClass(rclass));
		PlayerClass pclass = data.getClass("class");
		pclass.setLevel(10);
		pclass.setPoints(20);
		data.init(p);
	}

	public void resetTier2(Player p, String rclass) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.resetAttribs();
		data.reset("class");
		data.profess(SkillAPI.getClass("Beginner"));
		PlayerClass pclass = data.getClass("class");
		pclass.setLevel(10);
		data.profess(SkillAPI.getClass(rclass));
		pclass = data.getClass("class");
		data.setAttribPoints(60);
		pclass.setLevel(30);
		pclass.setPoints(60);
		data.init(p);
	}
}
