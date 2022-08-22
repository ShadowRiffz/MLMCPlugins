package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.BukkitAdapter;

public class WarnMechanic implements ITargetedEntitySkill {
	protected final int radius;
	protected String msg;
	protected final boolean warnTarget;

	public WarnMechanic(MythicLineConfig config) {
        this.radius = config.getInteger(new String[] {"radius", "r"}, 30);
        this.msg = config.getString(new String[] {"message", "msg", "m"}, "Default message").replaceAll("<&sp>", " ").replaceAll("<&cm>", ",").replaceAll("<&sq>", "'").replaceAll("&", "§");
        this.msg = msg.substring(1, msg.length() - 1);
        this.warnTarget = config.getBoolean(new String[] {"warntarget", "wt"}, false);
	}

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
	    	LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
	
	    	// Check if target is player
	    	if (bukkitTarget instanceof Player) {
	    		Player p = (Player) bukkitTarget;
	    		
	    		// If player is in hashmap, check their cooldown.
	    		for (Entity e : p.getNearbyEntities(radius, radius, radius)) {
	    			if (e instanceof Player) {
	    				Player msgTarget = (Player) e;
	    				msgTarget.sendMessage(this.msg.replaceAll("<target.name>", target.getName()));
	    			}
	    		}
	    		if (warnTarget) {
					p.sendMessage(this.msg.replaceAll("<target.name>", target.getName()));
	    		}
	    	}
	    	return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
