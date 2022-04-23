package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import com.sucy.skill.api.util.FlagManager;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;

public class FlagMechanic implements ITargetedEntitySkill {

	protected final int duration;
	protected final String flag;

	public FlagMechanic(MythicLineConfig config) {
        this.flag = config.getString(new String[] {"flag", "f"}, "stun");
        this.duration = config.getInteger(new String[] {"duration", "d"}, 20);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof LivingEntity) {
			FlagManager.addFlag((LivingEntity) data.getCaster().getEntity().getBukkitEntity(),(LivingEntity) target.getBukkitEntity(), this.flag, this.duration);
			return SkillResult.SUCCESS;
		}
		return SkillResult.INVALID_TARGET;
    }
}
