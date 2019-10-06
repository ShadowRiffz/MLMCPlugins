package me.neoblade298.neomythicextension.conditions;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import me.neoblade298.neomythicextension.Main;

public class GlobalScoreCondition extends SkillCondition implements IEntityCondition {
    private String operation;
    private String objective;
    private int value;
    private Main nme;
    
    public GlobalScoreCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
        operation = mlc.getString(new String[] {"operation", "op"});
        objective = mlc.getString(new String[] {"objective", "obj", "o"});
        value = mlc.getInteger(new String[] {"value", "v"});
        this.nme = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoMythicExtension");
    }

    public boolean check(AbstractEntity t) {
    	boolean toReturn = false;
    	
    	// Check if the value even exists
    	if (nme.globalscores.containsKey(objective)) {
    		int score = nme.globalscores.get(objective);
        	switch (operation) {
        	case "==":
        		toReturn = score == value; break;
        	case "!=":
        		toReturn = score != value; break;
        	case ">=":
        		toReturn = score >= value; break;
        	case "<=":
        		toReturn = score <= value; break;
        	case ">":
        		toReturn = score > value; break;
        	case "<":
        		toReturn = score < value; break;
        	}
    	}
    	return toReturn;
    }
}
