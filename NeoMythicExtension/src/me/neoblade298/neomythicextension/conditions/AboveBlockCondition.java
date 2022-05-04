package me.neoblade298.neomythicextension.conditions;

import org.bukkit.Location;
import org.bukkit.Material;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;


public class AboveBlockCondition implements IEntityCondition {
	private Material block;
    
    public AboveBlockCondition(MythicLineConfig mlc) {
        this.block = Material.getMaterial(mlc.getString("block", "STONE").toUpperCase());
    }

    public boolean check(AbstractEntity t) {
    	try {
        	Location loc = t.getBukkitEntity().getLocation();
        	while (loc.getBlock().getType().equals(Material.AIR) || loc.getBlock().isLiquid()) {
        		loc.setY(loc.getY() - 1);
        	}
        	return loc.getBlock().getType().equals(this.block);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
}
