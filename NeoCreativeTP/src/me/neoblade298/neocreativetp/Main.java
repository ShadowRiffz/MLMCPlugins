package me.neoblade298.neocreativetp;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	  public void onEnable()
	  {
	    Bukkit.getServer().getLogger().info("NeoChars Enabled");
	    getServer().getPluginManager().registerEvents(this, this);
	  }
	  
	  public void onDisable()
	  {
	    Bukkit.getServer().getLogger().info("NeoChars Disabled");
	  }
	  
	  @EventHandler
	  public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		  Player p = e.getPlayer();
		  String cmd = e.getMessage();
		  if(p.getWorld().getName().equalsIgnoreCase(("Creative"))) {
			  System.out.println(cmd);
			  if(cmd.equalsIgnoreCase("spawn")) {
				  p.setGameMode(GameMode.SURVIVAL);
			  }
		  }
	  }
}
