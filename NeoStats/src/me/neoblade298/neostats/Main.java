package me.neoblade298.neostats;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;

import org.bukkit.event.EventHandler;

public class Main extends JavaPlugin implements Listener{
	  
	  public void onEnable()
	  {
	    super.onEnable();
	    Bukkit.getServer().getLogger().info("NeoStats Enabled");
	    getServer().getPluginManager().registerEvents(this, this);
	  }
	  
	  public void onDisable()
	  {
	    Bukkit.getServer().getLogger().info("NeoStats Disabled");
	    super.onDisable();
	  }
	  
	  @EventHandler
	  public void onMMSpawn(MythicMobSpawnEvent e) {
		  if (e.getEntity().getName().contains("§4§lRatface")) {
			  Bukkit.getPlayer("Neoblade298").sendMessage("Success!");
		  }
	  }
}
