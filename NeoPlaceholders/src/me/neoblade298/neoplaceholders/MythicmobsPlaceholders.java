package me.neoblade298.neoplaceholders;

import java.util.HashMap;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class MythicmobsPlaceholders extends PlaceholderExpansion {
	private HashMap<String, Integer> health;
	
	public MythicmobsPlaceholders(HashMap<String, Integer> health) {
		this.health = health;
	}

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
		return "neomythicmobs";
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
		
		if (args[0].equalsIgnoreCase("mhealth")) {
			if (health.containsKey(args[1])) {
				return "" + health.get(args[1]);
			}
			return "§cHealth not defined!";
		}
		else if (args[0].equalsIgnoreCase("itemlore")) {
			Optional<MythicItem> item = MythicBukkit.inst().getItemManager().getItem(args[1]);
			if (item.isPresent() && item.get().getLore().size() > 0) {
				String lore = item.get().getLore().get(0);
				for (int i = 1; i < item.get().getLore().size(); i++) {
					lore += "\n" + item.get().getLore().get(i);
				}
				return lore;
			}
			else {
				return "§cLore not defined";
			}
		}
    	return "§cInvalid placeholder!";
	}
}
