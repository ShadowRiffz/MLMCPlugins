package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ReduceThreatMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final int amount;

	public ReduceThreatMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger(new String[] {"amount", "a"}, 50);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		MythicMobs.inst().getAPIHelper().reduceThreat(data.getCaster().getEntity().getBukkitEntity(), (LivingEntity) target.getBukkitEntity(), this.amount);
    	return true;
    }
}
