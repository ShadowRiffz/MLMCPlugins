package me.neoblade298.neoplaceholders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class MythicmobsPlaceholders extends PlaceholderExpansion {
	private HashMap<String, Integer> health;
	
	public MythicmobsPlaceholders(HashMap<String, Integer> health) {
		this.health = health;
	}

    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("MythicMobs") != null;
    }
    
    @Override
    public boolean register(){
    	if (!canRegister()) return false;
    	return super.register();
    }

	@Override
	public String getAuthor() {
		return "Neoblade298";
	}
	
    @Override
    public boolean persist(){
        return true;
    }

	@Override
	public String getIdentifier() {
		return "neomythicmobs";
	}

    @Override
    public String getRequiredPlugin(){
        return "MythicMobs";
    }
    
	@Override
	public String getVersion() {
		return "1.0.1";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		String args[] = identifier.split("_");
		
		if (args.length != 1) return "Invalid placeholder";
		if (health.containsKey(args[0])) {
			return "" + health.get(args[0]);
		}
    	return "§cHealth not defined!";
	}
}
