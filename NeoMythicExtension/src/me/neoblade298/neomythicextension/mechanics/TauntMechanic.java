package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;

public class TauntMechanic implements ITargetedEntitySkill {
	protected final int amount;

	public TauntMechanic(MythicLineConfig config) {
        this.amount = config.getInteger(new String[] {"amount", "a"}, 50);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (MythicBukkit.inst().getAPIHelper().isMythicMob(target.getBukkitEntity())) {
			MythicBukkit.inst().getAPIHelper().addThreat(target.getBukkitEntity(), (LivingEntity) data.getCaster().getEntity().getBukkitEntity(), this.amount);
		}
    	return SkillResult.SUCCESS;
    }
}
