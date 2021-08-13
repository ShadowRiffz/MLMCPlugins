package me.neoblade298.neoplaceholders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import com.sucy.skill.api.util.FlagManager;

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
		
		if (args[0].equalsIgnoreCase("cd")) {
			String boss = args[1];
			return plugin.getCooldownPlaceholder(boss, p);
		}
		// %bosses_partyhealth_1-5%
		else if (args[0].equalsIgnoreCase("partyhealth")) {
			ArrayList<String> partyMembers = plugin.getHealthBars(p);
			if (partyMembers == null) {
				return "";
			}
			int num = Integer.parseInt(args[1]);
			if (num >= partyMembers.size()) {
				return "";
			}
			Player partyMember = Bukkit.getPlayer(partyMembers.get(num));
			if (partyMember == null) {
				return "";
			}
			
			double percenthp = partyMember.getHealth() / partyMember.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			percenthp *= 100;
			int php = (int) percenthp;
			String color = "§a";
			if (php < 50 && php >= 25) {
				color = "§e";
			}
			else if (php < 25) {
				color = "§c";
			}
			else if (FlagManager.hasFlag(partyMember, "curse")) {
				color = "§8";
			}
			
			String bar = "" + color;
			// Add 5 so 25% is still 3/10 on the health bar
			int phpmod = (php + 5) / 10;
			for (int i = 0; i < phpmod; i++) {
				bar += "|";
			}
			bar += "§7";
			for (int i = 0; i < (10 - phpmod); i++) {
				bar += "|";
			}
			
			return color + partyMember.getName() + " " + bar;
		}
		return "Invalid Placeholder";
	}
}
