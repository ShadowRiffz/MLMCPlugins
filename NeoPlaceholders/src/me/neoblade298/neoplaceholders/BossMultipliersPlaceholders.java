package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neosettings.NeoSettings;

public class BossMultipliersPlaceholders extends PlaceholderExpansion {
	private NeoSettings plugin;

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
		
		if (id.equalsIgnoreCase("gold")) {
			int level = (int) plugin.getSettings("BossMultipliers").getValue(p.getUniqueId(), boss);
			double scale = Math.min(2, 1 + (0.05 * (level - 1)));
			return "" + scale;
		}
		else if (id.equalsIgnoreCase("chest")) {
			int level = (int) plugin.getSettings("BossMultipliers").getValue(p.getUniqueId(), boss);
			double scale = Math.min(50, 25 + (0.25 * (level - 1)));
			return "" + scale;
		}
		return "Invalid placeholder";
	}
}
