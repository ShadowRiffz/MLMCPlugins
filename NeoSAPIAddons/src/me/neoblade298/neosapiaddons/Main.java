package me.neoblade298.neosapiaddons;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.util.FlagManager;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoSAPIAddons Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    getCommand("neosapiaddons").setExecutor(new Commands());
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoSAPIAddons Disabled");
    super.onDisable();
  }
  
  // Implement curse status properly
  @EventHandler
  public void onHeal(SkillHealEvent e) {
	  LivingEntity target = e.getTarget();
	  if(FlagManager.hasFlag(target, "curse")) {
		  e.setCancelled(true);
		  target.damage(e.getAmount());
	  }
  }
}