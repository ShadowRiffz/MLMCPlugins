package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neobossinstances.Main;

public class WarnMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final int radius;
	protected String msg;
	protected final boolean warnTarget;
	Main nbi;

	public WarnMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.radius = config.getInteger(new String[] {"radius", "r"}, 30);
        this.msg = config.getString(new String[] {"message", "msg", "m"}, "Default message").replaceAll("<&sp>", " ").replaceAll("<&cm>", ",").replaceAll("<&sq>", "'").replaceAll("&", "§");
        this.msg = msg.substring(1, msg.length() - 1);
        this.warnTarget = config.getBoolean(new String[] {"warntarget", "wt"}, false);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
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
    	return true;
    }
}
