package me.neoblade298.neoinfinitybow;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener
{
  public Main() {}
  
  public void onEnable()
  {
    super.onEnable();
    org.bukkit.Bukkit.getServer().getLogger().info("NeoInfinityBow Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    getCommand("neoinfinitybow").setExecutor(new Commands());
  }
  
  public void onDisable()
  {
    org.bukkit.Bukkit.getServer().getLogger().info("NeoChangelog Disabled");
    super.onDisable();
  }
}