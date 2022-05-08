package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import com.sucy.skill.api.util.FlagManager;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

public class RemoveFlagMechanic implements ITargetedEntitySkill {

	protected final String flag;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public RemoveFlagMechanic(MythicLineConfig config) {
        this.flag = config.getString(new String[] {"flag", "f"}, "stun");
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof LivingEntity) {
				FlagManager.removeFlag((LivingEntity) target.getBukkitEntity(), this.flag);
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
