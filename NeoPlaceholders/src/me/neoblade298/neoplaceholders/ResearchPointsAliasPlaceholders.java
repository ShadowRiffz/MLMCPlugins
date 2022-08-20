package me.neoblade298.neoplaceholders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neocore.info.InfoAPI;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsAliasPlaceholders extends PlaceholderExpansion {

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
		return "researchpointsalias";
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
		
		if (args.length <= 1) return "Invalid placeholder";
		String boss = args[0];
		// %researchkillsalias_Hamvil_$4[$6Lv 40$4] $cHammer & Anvil
		String display = InfoAPI.getBossInfo(boss).getDisplayWithLevel(true);
		HashMap<String, Integer> researchPoints = Research.getPlayerStats(p.getUniqueId()).getResearchPoints();
		if (researchPoints.containsKey(boss)) {
			int points = researchPoints.get(boss);
			return display + "§7: §e" + points;
		}
    	return "§c???";
	}
}
