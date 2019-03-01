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
	    Bukkit.getServer().getLogger().info("NeoCreativeTP Enabled");
	    getServer().getPluginManager().registerEvents(this, this);
	  }
	  
	  public void onDisable()
	  {
	    Bukkit.getServer().getLogger().info("NeoCreativeTP Disabled");
	  }
	  
	  @EventHandler
	  public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		  Player p = e.getPlayer();
		  String cmd = e.getMessage();
		  if(p.getWorld().getName().equalsIgnoreCase(("Creative"))) {
			  if(cmd.equalsIgnoreCase("/spawn") ||
					  cmd.equalsIgnoreCase("/t spawn") ||
					  cmd.equalsIgnoreCase("/town spawn")) {
				  System.out.println("Success");
				  p.setGameMode(GameMode.SURVIVAL);
			  }
		  }
	  }
}
