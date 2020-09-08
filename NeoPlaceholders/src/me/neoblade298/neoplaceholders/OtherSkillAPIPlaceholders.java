package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class OtherSkillAPIPlaceholders extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("SkillAPI") != null;
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
		return "nsapi";
	}

    @Override
    public String getRequiredPlugin(){
        return "SkillAPI";
    }
    
	@Override
	public String getVersion() {
		return "1.0.1";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		String args[] = identifier.split("_");
		
		if (args.length != 1) return "Invalid placeholder";
		if (args[0].equalsIgnoreCase("account")) {
			return "" + SkillAPI.getPlayerAccountData(p).getActiveId();
		}
		if (args[0].equalsIgnoreCase("profession")) {
			PlayerClass prof = SkillAPI.getPlayerData(p).getClass("profession");
			if (prof != null) {
				return prof.getData().getPrefix();
			}
			return "N/A";
		}
	 	return "Invalid placeholder";
	}
}
