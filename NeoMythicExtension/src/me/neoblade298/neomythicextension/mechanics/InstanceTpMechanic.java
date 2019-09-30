package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class InstanceTpMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final String boss;
	protected final int max;
	private int count;

	public InstanceTpMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.boss = config.getString("boss", "Ratface");
        this.max = config.getInteger("max", 4);
        this.count = 0;
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
    	LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);

    	// Check if target is player
    	if (bukkitTarget instanceof Player) {
    		Player p = (Player) bukkitTarget;
    		// TODO: Check if player has no cooldown
    		if (true) {
    			if (count < max) {
    				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boss tp " + p.getName() + " " + this.boss);
    				count++;
		    		BukkitRunnable resetCount = new BukkitRunnable() {
		    			public void run() {
		    				count = 0;
		    			}
		    		};
		    		resetCount.runTaskLater(MythicMobs.inst(), 60L);
    			}
    		}
    	}
    	return true;
    }

}
