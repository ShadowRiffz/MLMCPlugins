package me.neoblade298.neomythicextension.conditions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ICasterCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class GlobalScoreCondition extends SkillCondition implements IEntityCondition {
    private String operation;
    private String objective;
    private int value;
    
    public GlobalScoreCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
        operation = mlc.getString(new String[] {"operation", "op"});
        objective = mlc.getString(new String[] {"objective", "obj", "o"});
        value = mlc.getInteger(new String[] {"value", "v"});
        System.out.println("@NEO operation = " + operation);
    }

    public boolean check(AbstractEntity t) {
    	int score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective).getScore("dummy").getScore();
    	
    	switch (operation) {
    	case "==":
    		return score == value;
    	case "!=":
			return score != value;
    	case ">=":
			return score >= value;
    	case "<=":
    		return score <= value;
    	case ">":
    		return score > value;
    	case "<":
    		return score < value;
    	}
    	return false;
    }
}
