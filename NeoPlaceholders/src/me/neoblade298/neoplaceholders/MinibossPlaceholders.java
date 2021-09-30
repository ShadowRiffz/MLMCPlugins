package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class MinibossPlaceholders extends PlaceholderExpansion {

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
		return "minibosses";
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
		
		if (args.length != 2) return "Invalid placeholder";
		if (!args[0].equalsIgnoreCase("cd")) return "Invalid placeholder";
		String miniboss = args[1];
		int time = MythicMobs.inst().getSpawnerManager().getSpawnerByName(miniboss).getRemainingCooldownSeconds();
		int minutes = time / 60;
		int seconds = time % 60;
		if (time > 0) return String.format("�c%d:%02d", minutes, seconds);
    	return "�aReady!";
	}
}
