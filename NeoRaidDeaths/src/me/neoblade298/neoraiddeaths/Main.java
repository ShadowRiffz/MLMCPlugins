package me.neoblade298.neoraiddeaths;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoRaidDeaths Enabled");
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoRaidDeaths Disabled");
    super.onDisable();
  }
  
  // Implement curse status properly
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
	  e.getEntity().removeScoreboardTag("KaravRaidTag");
  }
}