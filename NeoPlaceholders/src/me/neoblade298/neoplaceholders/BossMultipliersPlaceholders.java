package me.neoblade298.neoplaceholders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neosettings.NeoSettings;

public class BossMultipliersPlaceholders extends PlaceholderExpansion {
	private NeoSettings plugin;
	private HashMap<String, Integer> health;

	public BossMultipliersPlaceholders(HashMap<String, Integer> health) {
		this.health = health;
	}


    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("NeoSettings") != null;
    }
    
    @Override
    public boolean register(){
    	if (!canRegister()) return false;
    	Plugin plugin = Bukkit.getPluginManager().getPlugin("NeoSettings");
    	if (plugin == null) return false;
    	this.plugin = (NeoSettings) plugin;
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
		return "bossmults";
	}

    @Override
    public String getRequiredPlugin(){
        return "NeoSettings";
    }
    
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		String args[] = identifier.split("_");
		String boss = args[0];
		String id = args[1];
		if (plugin.getSettings("BossMultipliers", true).getValue(p.getUniqueId(), boss) == null) {
			return "Invalid";
		}
		int level = (int) plugin.getSettings("BossMultipliers", true).getValue(p.getUniqueId(), boss);
		
		if (id.equalsIgnoreCase("gold")) {
			if (level < 1) {
				return "" + 0;
			}
			double scale = Math.min(2, 1 + (0.05 * (level - 1)));
			return "" + scale;
		}
		else if (id.equalsIgnoreCase("chest")) {
			if (level < 1) {
				return "" + 0;
			}
			double scale = Math.min(50, 25 + (0.25 * (level - 1)));
			return "" + scale;
		}
		else if (id.equalsIgnoreCase("damage")) {

			if (level <= 6 && level >= 1) {
				double scale = 1 + (0.1 * (level - 1));
				return "" + scale;
			}
			else if (level > 6) {
				double scale = 1 + (0.3 * (level - 1));
				return "" + scale;
			}
			else if (level < 1) {
				double scale = 1 + (0.01 * (level));
				return "" + scale;
			}
		}
		else if (id.equalsIgnoreCase("health")) {
    		double oldHealth = this.health.get(boss);
    		double newHealth = oldHealth;
    		if (level >= 1) {
    			newHealth *= 0.5 + (Math.min(6, level) * 0.5);
    			if (level > 6) {
    				newHealth += (level - 6) * 0.2 * oldHealth;
    			}
    		}
    		else {
    			newHealth *= 1 + (level  * 0.01);
    		}
			return "" + newHealth;
		}
		return "Invalid placeholder";
	}
}
