package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class FlagMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int duration;
	protected final String flag;

	public FlagMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.flag = config.getString(new String[] {"flag", "f"}, "stun");
        this.duration = config.getInteger(new String[] {"duration", "d"}, 20);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof LivingEntity) {
			FlagManager.addFlag((LivingEntity) data.getCaster().getEntity().getBukkitEntity(),(LivingEntity) target.getBukkitEntity(), this.flag, this.duration);
			return true;
		}
		return false;
    }
}
