package me.neoblade298.neomythicextension.mechanics;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ScaleHealMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;

	public ScaleHealMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		target.setHealth(Math.min(target.getMaxHealth(), target.getHealth() + (this.amount * (0.5 + (data.getCaster().getLevel() * 0.5))));
		return true;
    }
}
