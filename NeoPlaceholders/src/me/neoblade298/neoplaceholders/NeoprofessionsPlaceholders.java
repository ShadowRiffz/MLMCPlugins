package me.neoblade298.neoplaceholders;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class NeoprofessionsPlaceholders extends PlaceholderExpansion {
	private me.Neoblade298.NeoProfessions.Main plugin;

    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("NeoProfessions") != null;
    }
    
    @Override
    public boolean register(){
    	if (!canRegister()) return false;
    	plugin = (me.Neoblade298.NeoProfessions.Main) Bukkit.getPluginManager().getPlugin("NeoProfessions");
    	if (plugin == null) return false;
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
		return "professions";
	}

    @Override
    public String getRequiredPlugin(){
        return "NeoProfessions";
    }
    
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		String args[] = identifier.split("_");
		
		if (args.length != 2) return "Invalid placeholder";
		if (!plugin.cManager.containsKey(args[0])) return "Invalid placeholder";
		if (!StringUtils.isNumeric(args[1])) return "Invalid placeholder";
		int lvl = Integer.parseInt(args[1]);
		if (!(lvl % 5 == 0 && lvl >= 5 && lvl <= 60)) return "Invalid placeholder";
		return "" + plugin.cManager.get(p, args[0], lvl);
	}
}
