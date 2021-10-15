package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neosettings.NeoSettings;

public class NeoSettingsPlaceholders extends PlaceholderExpansion {
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
		return "settings";
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
		String key = args[0];
		String subkey = args[1];
		
		return "" + plugin.getSettings(key).getValue(p.getUniqueId(), subkey);
	}
}
