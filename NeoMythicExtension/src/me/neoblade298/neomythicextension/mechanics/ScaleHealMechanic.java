package me.neoblade298.neomythicextension.mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

public class ScaleHealMechanic implements ITargetedEntitySkill {

	protected final int amount;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ScaleHealMechanic(MythicLineConfig config) {
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.isLiving()) {
				if (data.getCaster().getLevel() >= 1) {
					target.setHealth(Math.min(target.getMaxHealth(), target.getHealth() + (this.amount * (0.5 + (data.getCaster().getLevel() * 0.5)))));
				}
				else if (data.getCaster().getLevel() >= -99){
					target.setHealth(Math.min(target.getMaxHealth(), target.getHealth() + (this.amount * (1 + (data.getCaster().getLevel() * 0.01)))));
				}
			}
			return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
