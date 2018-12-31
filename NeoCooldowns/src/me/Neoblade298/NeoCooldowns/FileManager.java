package me.Neoblade298.NeoCooldowns;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {

  File file = null;
  File signfile = null;
  Main main = null;
  FileConfiguration config = null;
  static String filepath = "/home/MLMC/ServerTowny/plugins/ServerSigns/signs/";
  ConfigurationSection cooldowns;
  ConfigurationSection groups;
  
  public FileManager(Plugin main)
  {
  	file = new File(main.getDataFolder(), "config.yml");
    if (!file.exists()) {
      main.saveResource("config.yml", false);
    }
  	config = YamlConfiguration.loadConfiguration(this.file);
  	cooldowns = config.getConfigurationSection("Cooldowns");
  	groups = config.getConfigurationSection("Groups");
  }
  
  public int getCooldownKey(String arg) {
  	Set<String> cooldownKeys = cooldowns.getKeys(false);
  	Set<String> groupKeys = groups.getKeys(false);
  	Iterator<String> cooldownIter = cooldownKeys.iterator();
  	Iterator<String> groupIter = groupKeys.iterator();
  	
  	// Check if the arg exists in cooldowns
  	while(cooldownIter.hasNext()) {
  		String curr = cooldownIter.next();
  		if(curr.equalsIgnoreCase(arg)) {
  			return 1;
  		}
  	}
  	
  	// Check if the arg exists in groups
  	while(groupIter.hasNext()) {
  		String curr = groupIter.next();
  		if(curr.equalsIgnoreCase(arg)) {
  			return 2;
  		}
  	}
  	return 0;
  }
  
  public double getCooldown(String arg) {
  	String filename = filepath + arg;
  	signfile = new File(filename);
  	Scanner scan = null;
		try {
			scan = new Scanner(signfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String toParse = null;
		for(int i = 1; i <= 12; i++) {
			toParse = scan.nextLine();
		}
		toParse = toParse.replaceAll("globalCooldown: ", "");
  	int cooldown = Integer.parseInt(toParse) * 1000;
  	
  	
  	toParse = scan.nextLine();
		toParse = toParse.replaceAll("lastGlobalUse: ", "");
		long lastUse = Long.parseLong(toParse);
		
		long currTime = System.currentTimeMillis();
		scan.close();
		
		if(currTime > lastUse + cooldown) {
			return 0;
		}
		else {
			double temp = (lastUse + cooldown - currTime) / 6000;
			return temp / 10;
		}
  }

	public String[] getList() {
		String[] list = new String[config.getStringList("List").size()];
		list = config.getStringList("List").toArray(list);
		return list;
	}
	
	public String getKey(String arg) {
  	Set<String> cooldownKeys = cooldowns.getKeys(false);
  	Iterator<String> cooldownIter = cooldownKeys.iterator();
  	
  	while(cooldownIter.hasNext()) {
  		String curr = cooldownIter.next();
  		if(curr.equalsIgnoreCase(arg)) {
  			return curr;
  		}
  	}
  	return null;
	}
	
	public List<String> getKeys(String arg) {
  	Set<String> groupKeys = groups.getKeys(false);
  	Iterator<String> groupIter = groupKeys.iterator();
  	
  	// Check if the arg exists in groups
  	while(groupIter.hasNext()) {
  		String curr = groupIter.next();
  		if(curr.equalsIgnoreCase(arg)) {
  			return groups.getStringList(curr);
  		}
  	}
  	return null;
		
	}
}
