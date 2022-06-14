package me.neoblade298.neoplaceholders;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neoresearch.Research;

public class ResearchKillsAliasPlaceholders extends PlaceholderExpansion {

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
		return "researchkillsalias";
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
		String mob = args[0];
		String name = args[1];
		// %researchkillsalias_Hamvil_$4[$6Lv 40$4] $cHammer & Anvil
		for (int i = 2; args.length > i; i++) {
			name += args[i];
		}
		name = name.replaceAll("@", "§");
		HashMap<String, Integer> mobKills = Research.getPlayerStats(p.getUniqueId()).getMobKills();
		if (mobKills.containsKey(mob)) {
			int kills = mobKills.get(mob);
			return name + "§7: §e" + kills;
		}
    	return "§c???";
	}
}
