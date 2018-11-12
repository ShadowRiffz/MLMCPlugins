package me.neoblade298.skillapiflagcondition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.externals.ConditionAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ConditionAnnotation(name="hasflag", author="Neoblade298")
public class SkillAPIFlagCondition
extends 
AbstractCustomCondition
implements
IEntityCondition {
    private String[] flags;
    public SkillAPIFlagCondition(String line, MythicLineConfig mlc) {
        super(line,mlc);
        this.flags = mlc.getString("flag").trim().split(",");
    }

    @Override
    public boolean check(AbstractEntity t) {
        Entity target = t.getBukkitEntity();
        ActiveMob am = Utils.mobmanager.getMythicMobInstance(target);
        boolean result = false;
        if (am != null) {
        	LivingEntity ent = am.getLivingEntity();
        	for (String flag : flags) {
        		if (FlagManager.hasFlag(ent, flag)) {
        			result = true;
        		}
        		else {
        			result = false;
        			break;
        		}
        	}
        }
        return result;
    }
}
