package me.neoblade298.neomythicextension.conditions;

import org.bukkit.Bukkit;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
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
    }

    public boolean check(AbstractEntity t) {
    	int score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective).getScore("__GLOBAL__").getScore();
    	boolean toReturn = false;
    	
    	switch (operation) {
    	case "==":
    		System.out.println("Equals " + score + " " + value + ", op " + operation);
    		toReturn = score == value; break;
    	case "!=":
    		System.out.println("NE " + score + " " + value + ", op " + operation);
    		toReturn = score != value; break;
    	case ">=":
    		System.out.println("GE " + score + " " + value + ", op " + operation);
    		toReturn = score >= value; break;
    	case "<=":
    		System.out.println("LE " + score + " " + value + ", op " + operation);
    		toReturn = score <= value; break;
    	case ">":
    		System.out.println("GT " + score + " " + value + ", op " + operation);
    		toReturn = score > value; break;
    	case "<":
    		System.out.println("LT " + score + " " + value + ", op " + operation);
    		toReturn = score < value; break;
    	}

    	return toReturn;
    }
}
