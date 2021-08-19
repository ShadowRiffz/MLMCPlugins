package me.neoblade298.neoattrsetter;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
  
  public void onEnable()
  {
    Bukkit.getServer().getLogger().info("NeoAttrSetter Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    // Get command listener
    this.getCommand("attrs").setExecutor(new Commands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoAttrSetter Disabled");
  }
}
