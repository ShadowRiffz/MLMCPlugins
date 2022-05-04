package me.neoblade298.neomythicextension.conditions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;

public class StrongPlayerWithin implements IEntityCondition {
	private double radius;
    
    public StrongPlayerWithin(MythicLineConfig mlc) {
        this.radius = mlc.getDouble("radius", 40);
    }

    public boolean check(AbstractEntity t) {
    	try {
	    	for (Entity e : t.getBukkitEntity().getNearbyEntities(radius, 256, radius)) {
	    		if (e instanceof Player) {
	    			Player p = (Player) e;
	    			ItemStack chestplate = p.getInventory().getArmorContents()[2];
	    			if (chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().hasLore()) {
	    				return true;
	    			}
	    		}
	    	}
	    	return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
