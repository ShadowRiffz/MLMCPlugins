package me.neoblade298.neocreativetp;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Main extends JavaPlugin implements Listener {
	
	HashMap<String, Boolean> bars = new HashMap<String,Boolean>();

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
		  cmd = cmd.toLowerCase();
		  if(cmd.equals("/warp creative") && p.hasPermission("essentials.warps.creative")) {
			  PlayerData data = SkillAPI.getPlayerData(p);
			  if(data.getSkillBar().isEnabled()) {
				  data.getSkillBar().toggleEnabled();
				  bars.put(p.getName(), true);
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
		  Player p = e.getPlayer();
		  if(e.getPlayer().getWorld().getName().equalsIgnoreCase("Argyll") ||
				  e.getPlayer().getWorld().getName().equalsIgnoreCase("ClassPVP")) {
			  if(bars.containsKey(p.getName())) {
				  bars.remove(p.getName());
				  SkillAPI.getPlayerData(p).getSkillBar().toggleEnabled();
			  }
		  }
	  }
}
