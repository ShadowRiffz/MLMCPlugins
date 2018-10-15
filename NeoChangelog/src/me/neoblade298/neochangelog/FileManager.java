package me.neoblade298.neochangelog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {

  File logFile = null;
  Main main = null;
  FileConfiguration logConf = null;
  SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
  String lastUpdated = null;
  
  public FileManager(Plugin main)
  {
    logFile = new File(main.getDataFolder(), "changelog.yml");
    if (!logFile.exists()) {
      main.saveResource("changelog.yml", false);
    }
  }
  
  public String getLastUpdated() {
  	List<List> temp = getDays();
  	return (String) temp.get(0).get(0);
  }
  
  public boolean removeLog(int day, int index) {
  	List<List> temp = getDays();

  	// Check if the day and index are properly set
  	if(temp.size() <= day) {
  		return false;
  	}
  	if(temp.get(day).size() <= index ||
  		index == 0) {
  		return false;
  	}
  	
  	// Take the day and remove the indexed number
  	temp.get(day).remove(index);
  	String date = (String) temp.get(day).remove(0);
  	
  	// If the day has no indices left, remove the day entirely
  	if(temp.get(day).size() == 0) {
  		logConf.set(date, null);
  		saveLog();
  		return true;
  	}
  	else {
  		logConf.set(date, temp.get(day));
  		saveLog();
  		return true;
  	}
  }
  
  public boolean removeDays(int days) {
  	List<List> temp = getDays();
  	
  	// Remove every day past the indexed day
  	for(int i = days; i < temp.size(); i++) {
  		logConf.set((String) temp.get(i).get(0), null);
  	}
  	saveLog();
  	return true;
  }
  
  public boolean removeDay(int day) {
  	List<List> temp = getDays();
  	
  	// Make sure the day index exists
  	if(day >= temp.size()) {
  		return false;
  	}
  	
  	// Remove the specific day
  	logConf.set((String) temp.get(day).get(0), null);
  	saveLog();
  	return true;
  }
  
  public void saveLog() {
  	try {
			logConf.save(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
  
  public boolean addLog(String log) {
    List<List> temp = getDays();
    boolean hasDay = false;
    Date date = new Date();
    
  	// First check if today's date exists
    for(int i = 0; i < temp.size(); i++) {
    	if(temp.get(i).contains(format.format(date))) {
    		temp.get(i).remove(0);
    		temp.get(i).add(log);
    		logConf.set(format.format(date), temp.get(i));
    		hasDay = true;
        saveLog();
    		return true;
    	}
    }
    
    // If it doesn't create a new date
    if(!hasDay) {
    	List<String> newLog = new ArrayList<String>();
    	newLog.add(log);
    	logConf.set(format.format(date), newLog);
      saveLog();
    	return true;
    }
    return false;
  }
  
  public boolean addLog(String log, int dateIndex) {
    List<List> temp = getDays();
    if(temp.size() > dateIndex) {
    	String date = (String) temp.get(dateIndex).remove(0);
    	temp.get(dateIndex).add(log);
    	logConf.set(date, temp.get(dateIndex));
    	saveLog();
    	return true;
    }
    return false;
  }
  
  public List<List> getDays()
  {  	
  	// Load the yaml config, check that it contains contents
  	logConf = YamlConfiguration.loadConfiguration(this.logFile);
    String[] days = logConf.getKeys(false).toArray(new String[0]);
    if(days.length == 0) {
    	return null;
    }
    List<List> result = new ArrayList<List>();
    
    // Loop through days in list, add date to front of list
    for(int i = days.length-1; i >= 0; i--) {
    	List<String> temp = new ArrayList<String>();
    	temp.add(days[i]);
	  	temp.addAll(logConf.getStringList(days[i]));
	  	result.add(temp);
    }
    return result;
  }
}
