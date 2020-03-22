package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RemoveFlagMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final String flag;

	public RemoveFlagMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.flag = config.getString(new String[] {"flag", "f"}, "stun");
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof LivingEntity) {
			FlagManager.removeFlag((LivingEntity) target.getBukkitEntity(), this.flag);
			return true;
		}
		return false;
    }
}
