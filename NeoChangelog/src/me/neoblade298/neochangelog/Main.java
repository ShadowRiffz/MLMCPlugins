package me.neoblade298.neochangelog;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
  FileManager fm;
  
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoChangelog Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    // Get changelog
    fm = new FileManager(this);
    
    // Get command listener
    this.getCommand("changelog").setExecutor(new Commands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoChangelog Disabled");
    super.onDisable();
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
  	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
      public void run() {
      	e.getPlayer().sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + ChatColor.BOLD +
      			"MLMC" + ChatColor.DARK_RED + "] " + ChatColor.GRAY + "The changelog was last updated: "
      			+ ChatColor.YELLOW + fm.getLastUpdated());
      }
  	}, 100L);
  }
  
  public FileManager getFM() {
  	return fm;
  }
}