package me.neoblade298.neomythicextension.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import me.neoblade298.neomythicextension.MythicExt;

public class GlobalScoreCondition implements IEntityCondition {
    private String operation;
    private String objective;
    private int value;
    private MythicExt nme;
    
    public GlobalScoreCondition(MythicLineConfig mlc, MythicExt nme) {
        operation = mlc.getString(new String[] {"operation", "op"});
        objective = mlc.getString(new String[] {"objective", "obj", "o"});
        value = mlc.getInteger(new String[] {"value", "v"});
        this.nme = nme;
    }

    public boolean check(AbstractEntity t) {
    	try {
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
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
