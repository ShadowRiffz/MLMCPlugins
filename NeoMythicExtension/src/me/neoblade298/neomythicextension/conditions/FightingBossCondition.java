package me.neoblade298.neomythicextension.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import me.neoblade298.neobossinstances.BossInstances;

public class FightingBossCondition implements IEntityCondition {
    
    public FightingBossCondition(MythicLineConfig mlc) {
    }

    public boolean check(AbstractEntity t) {
    	try {
    		return !BossInstances.getSpectators().contains(t.getUniqueId());
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
