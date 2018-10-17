package me.neoblade298.neocollections;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  File file = null;
  FileConfiguration conf = null;
  HashMap playerMap = new HashMap();
  int maxCollections;
  List<Integer> permCollections;
  List<Attributes> attrBonuses = new ArrayList();
  List<Attributes> shinyBonuses = new ArrayList();
  
  
  public void onEnable() {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoCollections Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    this.getCommand("neocollections").setExecutor(new Commands(this));
    
    // Save config if doesn't exist
    file = new File(getDataFolder(), "changelog.yml");
    if (!file.exists()) {
      saveResource("config.yml", false);
    }
  	conf = YamlConfiguration.loadConfiguration(file);
    
    // Load values from config
  	maxCollections = conf.getInt("Max_Collection");
  	permCollections = conf.getIntegerList("Permanent_Collections");
	ConfigurationSection collection = conf.getConfigurationSection("Collection_Bonuses");
	ConfigurationSection shinyCollection = conf.getConfigurationSection("Shiny_Collection_Bonuses");
  	
  	// Load in all attribute bonuses
  	for(int i = 1; i <= maxCollections; i++) {
  		ConfigurationSection curr_collection = collection.getConfigurationSection(Integer.toString(i));
  		ConfigurationSection curr_shiny = shinyCollection.getConfigurationSection(Integer.toString(i));
  		
  		int c_str = curr_collection.getInt("Strength");
  		int c_dex = curr_collection.getInt("Dexterity");
  		int c_int = curr_collection.getInt("Intelligence");
  		int c_spr = curr_collection.getInt("Spirit");
  		int c_prc = curr_collection.getInt("Perception");
  		int c_end = curr_collection.getInt("Endurance");
  		int c_vit = curr_collection.getInt("Vitality");
  		

  		int s_str = curr_shiny.getInt("Strength");
  		int s_dex = curr_shiny.getInt("Dexterity");
  		int s_int = curr_shiny.getInt("Intelligence");
  		int s_spr = curr_shiny.getInt("Spirit");
  		int s_prc = curr_shiny.getInt("Perception");
  		int s_end = curr_shiny.getInt("Endurance");
  		int s_vit = curr_shiny.getInt("Vitality");
  		
  		Attributes attrs = new Attributes(c_str, c_dex, c_int, c_spr, c_prc, c_end, c_vit);
  		attrBonuses.set(i, attrs);
  		
  		Attributes shiny_attrs = new Attributes(s_str, s_dex, s_int, s_spr, s_prc, s_end, s_vit);
  		shinyBonuses.set(i, shiny_attrs);
  	}
  }
  
  public void onDisable() {
    Bukkit.getServer().getLogger().info("NeoCollections Disabled");
    super.onDisable();
  }
  
  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent e) {
  }
  
  @EventHandler
  public void onPlayerLeave(PlayerKickEvent e) {
  }
  
  private void resetAttributes(Player p) {
	  
  }
}