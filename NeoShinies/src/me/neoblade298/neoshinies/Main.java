package me.neoblade298.neoshinies;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("Neoshinies Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    getCommand("neoshinies").setExecutor(new Commands());
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("Neoshinies Disabled");
    super.onDisable();
  }
}
