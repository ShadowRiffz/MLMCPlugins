package me.neoblade298.neoplaceholders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsPlaceholders extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("NeoResearch") != null;
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
		return "researchkills";
	}

    @Override
    public String getRequiredPlugin(){
        return "NeoResearch";
    }
    
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		String args[] = identifier.split("_");
		
		if (args.length != 1) return "Invalid placeholder";
		String mob = args[0];
		String name = MythicMobs.inst().getMobManager().getMythicMob(mob).getDisplayName().get();
		Research nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
		HashMap<String, Integer> mobKills = nr.getPlayerStats(p).getMobKills();
		if (mobKills.containsKey(mob)) {
			int kills = mobKills.get(mob);
			return name + "§7: §e" + kills + "§7 kills";
		}
    	return "§c???";
	}
}
