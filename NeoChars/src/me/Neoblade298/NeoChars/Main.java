package me.Neoblade298.NeoChars;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Main extends JavaPlugin implements Listener
{
  
  public void onEnable()
  {
    Bukkit.getServer().getLogger().info("NeoChars Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    // Get command listener
    this.getCommand("char").setExecutor(new Commands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoChars Disabled");
  }
  
  public void sendPlayerCard(Player recipient, Player viewed) {
  	if (!SkillAPI.hasPlayerData(viewed) || SkillAPI.getPlayerData(viewed).getClass("class") == null) {
  		Utilities.sendMessage(recipient, "&cThis player has no class");
  		return;
  	}
  	
  	int pLvl = SkillAPI.getPlayerData(viewed).getClass("class").getLevel();
  	String pClass = SkillAPI.getPlayerData(viewed).getClass("class").getData().getName();
  	int profLvl = 0;
  	String profClass = null;
  	if(SkillAPI.getPlayerData(viewed).getClass("profession") != null) {
	  	profLvl = SkillAPI.getPlayerData(viewed).getClass("profession").getLevel();
	  	profClass = SkillAPI.getPlayerData(viewed).getClass("profession").getData().getName();
  	}
  	int xp = (int) SkillAPI.getPlayerData(viewed).getClass("class").getExp();
  	int reqxp = SkillAPI.getPlayerData(viewed).getClass("class").getRequiredExp();
  	PlayerData pData = SkillAPI.getPlayerData(viewed);

  	Utilities.sendMessage(recipient, "&7-- &e" + viewed.getName() + " &6[Lv " + pLvl + " " + pClass + "] &7(" + xp + " / " + reqxp + " XP) --");
  	if(SkillAPI.getPlayerData(viewed).getClass("profession") != null) {
  		Utilities.sendMessage(recipient, "&7-- &e" + "&6[Lv " + profLvl + " " + profClass + "] &7--");
  	}
  	Utilities.sendMessage(recipient, "&e" + pData.getAttribute("Strength") + " &cSTR&7 | &e" + pData.getAttribute("Dexterity") + " &cDEX&7 | &e" +
  			pData.getAttribute("Intelligence") + " &cINT&7 | &e" + pData.getAttribute("Spirit") + " &cSPR&7 | &e" + pData.getAttribute("Perception") + " &cPRC&7 | &e" +
  			pData.getAttribute("Endurance") + " &cEND&7 | &e" + pData.getAttribute("Vitality") + " &cVIT&7 | &e" + (int) viewed.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() +
  			" &cHP");
  }
  
  @EventHandler
  public void onInteract(PlayerInteractEntityEvent e) {
  	// Make sure we're interacting with a player in the quest world
  	if (!(e.getRightClicked() instanceof Player) || !(e.getPlayer().getWorld().getName().equalsIgnoreCase("Argyll"))) {
  		return;
  	}
  	
  	// Only let it happen once
  	if (!(e.getHand() == EquipmentSlot.HAND)) {
  		return;
  	}
  	
  	// Make sure the player being clicked is not an NPC
  	if (Bukkit.getPlayer(e.getRightClicked().getName()) == null) {
  		return;
  	}
  	
  	if (e.getPlayer().isSneaking()) {
  		return;
  	}
  	
  	Player clicked = (Player) e.getRightClicked();
  	sendPlayerCard(e.getPlayer(), clicked);
  }
}