package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.neoblade298.neoresearch.Attributes;
import me.neoblade298.neoresearch.PlayerStats;
import me.neoblade298.neoresearch.Research;

public class ResearchPlaceholders extends PlaceholderExpansion {

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
		return "research";
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
		Research nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
		PlayerStats stats = nr.getPlayerStats(p);
		if (args[0].equalsIgnoreCase("level")) {
			return "" + stats.getLevel();
		}
		if (args[0].equalsIgnoreCase("exp")) {
			return "" + stats.getExp();
		}
		if (args[0].equalsIgnoreCase("completedresearch")) {
			return "" + stats.getCompletedResearchItems().size();
		}
		if (args[0].equalsIgnoreCase("remainingresearch")) {
			return "" + (nr.getNumResearchItems() - stats.getCompletedResearchItems().size());
		}
		if (args[0].equalsIgnoreCase("neededexp")) {
			return "" + nr.getNextLvl().get(stats.getLevel());
		}
		if (args[0].equalsIgnoreCase("attr")) {
			Attributes attr = nr.getPlayerAttributes(p);
			switch (args[1]) {
				case "str": return "" + attr.getStrength();
				case "dex": return "" + attr.getDexterity();
				case "int": return "" + attr.getIntelligence();
				case "spr": return "" + attr.getSpirit();
				case "prc": return "" + attr.getPerception();
				case "end": return "" + attr.getEndurance();
				case "vit": return "" + attr.getVitality();
			}
		}
    	return "§c???";
	}
}
