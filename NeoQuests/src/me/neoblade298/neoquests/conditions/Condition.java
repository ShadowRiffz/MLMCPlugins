package me.neoblade298.neoquests.conditions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public interface Condition {
	public boolean passes(Player p);
	public default ConditionResult getResult() {
		return ConditionResult.INVISIBLE;
	}
	public String getExplanation(Player p);
	
	public static ArrayList<Condition> getFailedConditions(Player p, ArrayList<Condition> conditions) {
		ArrayList<Condition> failed = new ArrayList<Condition>();
		for (Condition cond : conditions) {
			if (!cond.passes(p)) {
				failed.add(cond);
			}
		}
		return failed;
	}
	
	public static Condition getBlockingCondition(Player p, ArrayList<Condition> conditions) {
		for (Condition cond : conditions) {
			if (!cond.passes(p)) {
				return cond;
			}
		}
		return null;
	}
}
