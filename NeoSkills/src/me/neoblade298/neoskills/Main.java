package me.neoblade298.neoskills;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoSkills Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    getCommand("neoskills").setExecutor(new Commands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoSkills Disabled");
    super.onDisable();
  }
}
