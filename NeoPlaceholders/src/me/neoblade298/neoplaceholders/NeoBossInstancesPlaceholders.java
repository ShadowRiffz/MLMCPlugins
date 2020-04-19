package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class NeoBossInstancesPlaceholders extends PlaceholderExpansion {
	private me.neoblade298.neobossinstances.Main plugin;

    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("NeoBossInstances") != null;
    }
    
    @Override
    public boolean register(){
    	if (!canRegister()) return false;
    	plugin = (me.neoblade298.neobossinstances.Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");
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
		return "bosses";
	}

    @Override
    public String getRequiredPlugin(){
        return "NeoBossInstances";
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
		if (!args[0].equalsIgnoreCase("cd")) return "Invalid placeholder";
		String boss = args[1];
		return plugin.getCooldownPlaceholder(boss, p);
	}
}
