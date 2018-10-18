package me.neoblade298.neocollections;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  File file = null;
  FileConfiguration conf = null;
  HashMap<String, Attributes> playerMap = new HashMap<String, Attributes>();
  int maxCollections;
  List<Integer> permCollections;
  List<Attributes> attrBonuses = new ArrayList<Attributes>(maxCollections + 1);
  List<Attributes> shinyBonuses = new ArrayList<Attributes>(maxCollections + 1);
  
  
  public void onEnable() {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoCollections Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    this.getCommand("neocollections").setExecutor(new Commands(this));
    
    // Save config if doesn't exist
    file = new File(getDataFolder(), "config.yml");
    if (!file.exists()) {
      saveResource("config.yml", false);
    }
  	conf = YamlConfiguration.loadConfiguration(file);
    
    // Load values from config
  	maxCollections = getConfig().getInt("Max_Collection");
  	permCollections = getConfig().getIntegerList("Permanent_Collections");
  	attrBonuses.add(new Attributes(0, 0, 0, 0, 0, 0, 0));
  	shinyBonuses.add(new Attributes(0, 0, 0, 0, 0, 0, 0));
  	ConfigurationSection collection = getConfig().getConfigurationSection("Collection_Bonuses");
  	ConfigurationSection shinyCollection = getConfig().getConfigurationSection("Shiny_Collection_Bonuses");
	
	// Initialize arrays
	for(int i = 0; i < maxCollections + 1; i++) {
		attrBonuses.add(i, new Attributes(0, 0, 0, 0, 0, 0, 0));
		shinyBonuses.add(i, new Attributes(0, 0, 0, 0, 0, 0, 0));
	}
  	
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
  
  public void initializeBonuses(Player p) {
	  int f_str = 0, f_dex = 0, f_int = 0, f_spr = 0, f_prc = 0, f_end = 0, f_vit = 0;
	  
	  // First initialize all permanent bonuses
	  List<Integer> permCollections = conf.getIntegerList("Permanent_Collections");
	  for(int i = 0; i < permCollections.size(); i++) {
		  int numColl = permCollections.get(i);
		  
		  // Add normal permanent collections
		  if(p.hasPermission("collections.permanent." + numColl) || p.hasPermission("collections.unlock." + numColl)) {
			  f_str += attrBonuses.get(numColl).getStrength();
			  f_dex += attrBonuses.get(numColl).getDexterity();
			  f_int += attrBonuses.get(numColl).getIntelligence();
			  f_spr += attrBonuses.get(numColl).getSpirit();
			  f_prc += attrBonuses.get(numColl).getPerception();
			  f_end += attrBonuses.get(numColl).getEndurance();
			  f_vit += attrBonuses.get(numColl).getVitality();
		  }
		  
		  // Add shiny permanent collections
		  if(p.hasPermission("collections.sh.permanent." + numColl) || p.hasPermission("collections.sh.unlock." + numColl)) {
			  f_str += shinyBonuses.get(numColl).getStrength();
			  f_dex += shinyBonuses.get(numColl).getDexterity();
			  f_int += shinyBonuses.get(numColl).getIntelligence();
			  f_spr += shinyBonuses.get(numColl).getSpirit();
			  f_prc += shinyBonuses.get(numColl).getPerception();
			  f_end += shinyBonuses.get(numColl).getEndurance();
			  f_vit += shinyBonuses.get(numColl).getVitality();
		  }
	  }
	  
	  // Next find which collection and shiny the user has equipped
	  boolean foundColl = false, foundShinyColl = false;
	  for(int i = 1; i <= maxCollections; i++) {
		  if(p.hasPermission("collections.use." + i)) {
			  f_str += attrBonuses.get(i).getStrength();
			  f_dex += attrBonuses.get(i).getDexterity();
			  f_int += attrBonuses.get(i).getIntelligence();
			  f_spr += attrBonuses.get(i).getSpirit();
			  f_prc += attrBonuses.get(i).getPerception();
			  f_end += attrBonuses.get(i).getEndurance();
			  f_vit += attrBonuses.get(i).getVitality();
			  foundColl = true;
		  }
		  if(p.hasPermission("collections.sh.use." + i)) {
			  f_str += shinyBonuses.get(i).getStrength();
			  f_dex += shinyBonuses.get(i).getDexterity();
			  f_int += shinyBonuses.get(i).getIntelligence();
			  f_spr += shinyBonuses.get(i).getSpirit();
			  f_prc += shinyBonuses.get(i).getPerception();
			  f_end += shinyBonuses.get(i).getEndurance();
			  f_vit += shinyBonuses.get(i).getVitality();
			  foundShinyColl = true;
		  }
		  
		  // Break out of for loop if both collection and shiny collection has been found
		  if(foundColl && foundShinyColl) {
			  break;
		  }
	  }
	  
	  // Apply the bonuses and map them
	  Attributes attrSet = new Attributes(f_str, f_dex, f_int, f_spr, f_prc, f_end, f_vit);
	  playerMap.put(p.getName(), attrSet);
	  attrSet.applyAttributes(p);
  }
  
  public void updateBonuses(Player p) {
	  if(playerMap.containsKey(p.getName())) {
		  playerMap.get(p.getName()).removeAttributes(p);
		  initializeBonuses(p);
	  }
  }
  
  public void resetBonuses(Player p) {
	  if(playerMap.containsKey(p.getName())) {
		  playerMap.get(p.getName()).removeAttributes(p);
		  playerMap.remove(p.getName());
	  }
  }
  
  public void updateAll() {
  	// TODO
  }
  
  public void resetAll() {
  	// TODO
  }
}