package me.Neoblade298.NeoCooldowns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
  FileManager fm;
  
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoCooldowns Enabled");
    
    // Get file manager
    fm = new FileManager(this);
    
    
    // Get command listener
    this.getCommand("cooldown").setExecutor(new Commands(this));
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoCooldowns Disabled");
    super.onDisable();
  }
  
  public FileManager getFM() {
  	return fm;
  }
}