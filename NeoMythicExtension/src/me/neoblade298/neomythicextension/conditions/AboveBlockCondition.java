package me.neoblade298.neomythicextension.conditions;

import org.bukkit.Location;
import org.bukkit.Material;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class AboveBlockCondition extends SkillCondition implements IEntityCondition {
	private Material block;
    
    public AboveBlockCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
        this.block = Material.getMaterial(mlc.getString("block", "STONE").toUpperCase());
    }

    public boolean check(AbstractEntity t) {
    	Location loc = t.getBukkitEntity().getLocation();
    	while (loc.getBlock().getType().equals(Material.AIR) || loc.getBlock().isLiquid()) {
    		loc.setY(loc.getY() - 1);
    	}
    	return loc.getBlock().getType().equals(this.block);
    }
}
