package me.neoblade298.neomythicextension.conditions;

import org.bukkit.Bukkit;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;


public class ScoreCondition implements IEntityCondition {
    private String operation;
    private String objective;
    private String entry;
    private int value;
    
    public ScoreCondition(MythicLineConfig mlc) {
        operation = mlc.getString(new String[] {"operation", "op"});
        objective = mlc.getString(new String[] {"objective", "obj", "o"});
        entry = mlc.getString(new String[] {"entry", "e"});
        value = mlc.getInteger(new String[] {"value", "v"});
    }

    // CURRENTLY UNUSED CONDITION
    public boolean check(AbstractEntity t) {
    	try {
	    	int score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective).getScore(entry).getScore();
	    	
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
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
