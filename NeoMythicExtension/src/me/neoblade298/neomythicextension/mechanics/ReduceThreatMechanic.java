package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.MythicBukkit;

public class ReduceThreatMechanic implements ITargetedEntitySkill {
	protected final int amount;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ReduceThreatMechanic(MythicLineConfig config) {
        this.amount = config.getInteger(new String[] {"amount", "a"}, 50);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			MythicBukkit.inst().getAPIHelper().reduceThreat(data.getCaster().getEntity().getBukkitEntity(), (LivingEntity) target.getBukkitEntity(), this.amount);
	    	return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
