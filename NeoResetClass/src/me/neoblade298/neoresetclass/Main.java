package me.neoblade298.neoresetclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

public class Main extends JavaPlugin implements Listener {

	  public void onEnable()
	  {
	    Bukkit.getServer().getLogger().info("NeoResetClass Enabled");
	    getServer().getPluginManager().registerEvents(this, this);
	    
	    // Get command listener
	    this.getCommand("neoresetclass").setExecutor(new Commands(this));
	  }
	  
	  public void onDisable()
	  {
	    Bukkit.getServer().getLogger().info("NeoResetClass Disabled");
	  }
	  
	  public void resetPlayer(Player p) {
		  String classname = SkillAPI.getPlayerData(p).getClass("class").getData().getName();
		  
		  if(classname.equalsIgnoreCase("Swordsman") ||
				  classname.equalsIgnoreCase("Mage") ||
				  classname.equalsIgnoreCase("Thief") ||
				  classname.equalsIgnoreCase("Archer")) {
			  resetTier1(p, "Beginner");
		  }
		  else if(classname.equalsIgnoreCase("Paladin") ||
				  classname.equalsIgnoreCase("Berserker") ||
				  classname.equalsIgnoreCase("Duelist")) {
			  resetTier2(p, "Swordsman");
		  }
		  else if(classname.equalsIgnoreCase("Ranger") ||
				  classname.equalsIgnoreCase("Hunter") ||
				  classname.equalsIgnoreCase("Vanguard")) {
			  resetTier2(p, "Archer");
		  }
		  else if(classname.equalsIgnoreCase("Archmage") ||
				  classname.equalsIgnoreCase("Warlock") ||
				  classname.equalsIgnoreCase("Bishop")) {
			  resetTier2(p, "Mage");
		  }
		  else if(classname.equalsIgnoreCase("Assassin") ||
				  classname.equalsIgnoreCase("Diversionist") ||
				  classname.equalsIgnoreCase("Infiltrator")) {
			  resetTier2(p, "Thief");
		  }
	  }
	  
	  public void resetTier1(Player p, String rclass) {
		  PlayerData data = SkillAPI.getPlayerData(p);
		  data.resetAttribs();
		  data.reset("class");
		  data.setClass(SkillAPI.getClass(rclass));
		  data.setAttribPoints(0);
		  PlayerClass pclass = data.getClass("class");
		  pclass.setLevel(10);
		  pclass.setPoints(20);
		  data.updateHealthAndMana(p);
	  }
	  
	  public void resetTier2(Player p, String rclass) {
		  PlayerData data = SkillAPI.getPlayerData(p);
		  data.resetAttribs();
		  data.reset("class");
		  data.setClass(SkillAPI.getClass(rclass));
		  data.setAttribPoints(60);
		  PlayerClass pclass = data.getClass("class");
		  pclass.setLevel(30);
		  pclass.setPoints(60);
		  data.updateHealthAndMana(p);
	  }
}
