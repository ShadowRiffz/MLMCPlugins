package me.neoblade298.neoplaceholders;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class EtcPlaceholders extends PlaceholderExpansion {
    @Override
    public boolean canRegister(){
        return true;
    }

	@Override
	public String getAuthor() {
		return "Neoblade298";
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return "mlmc";
	}
    
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0.0";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if (p == null) return "Loading...";
		
		if (identifier.equalsIgnoreCase("health")) return "" + ((int) p.getHealth());
		
		return "Loading...";
	}
}
