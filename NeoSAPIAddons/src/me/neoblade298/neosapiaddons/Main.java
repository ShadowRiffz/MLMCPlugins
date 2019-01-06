package me.neoblade298.neosapiaddons;

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
    Bukkit.getServer().getLogger().info("NeoSAPIAddons Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    getCommand("neosapiaddons").setExecutor(new Commands());
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoSAPIAddons Disabled");
    super.onDisable();
  }
}