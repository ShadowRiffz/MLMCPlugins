package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.LivingEntity;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class TauntMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final int amount;

	public TauntMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger(new String[] {"amount", "a"}, 50);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		MobManager mm = MythicMobs.inst().getMobManager();
		if (mm.isActiveMob(target)) {
			MythicMobs.inst().getAPIHelper().addThreat(target.getBukkitEntity(), (LivingEntity) data.getCaster().getEntity().getBukkitEntity(), this.amount);
		}
    	return true;
    }
}
