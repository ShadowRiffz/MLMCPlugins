package me.neoblade298.neomythicextension.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import me.neoblade298.neobossinstances.Main;

public class FightingBossCondition extends SkillCondition implements IEntityCondition {
    
    public FightingBossCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
    }

    public boolean check(AbstractEntity t) {
    	return !Main.getSpectators().contains(t.getUniqueId());
    }
}
