package me.neoblade298.neomythicextension.conditions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class StrongPlayerWithin extends SkillCondition implements IEntityCondition {
	private double radius;
    
    public StrongPlayerWithin(MythicLineConfig mlc) {
        super(mlc.getLine());
        this.radius = mlc.getDouble("radius", 40);
    }

    public boolean check(AbstractEntity t) {
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
}
