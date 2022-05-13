package me.neoblade298.neoquests.conditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;

public interface Condition {
	static HashMap<String, Condition> conditions = new HashMap<String, Condition>();
	
	public String getKey();
	public boolean passes(Player p);
	public String getExplanation(Player p);
	public Condition newInstance(LineConfig cfg);
	public ConditionResult getResult();
	
	public static void register(String key, Condition cond) {
		conditions.put(key, cond);
	}
	
	public static void clear() {
		conditions.clear();
	}
	
	public static Condition getNew(LineConfig cfg) {
		return conditions.get(cfg.getKey()).newInstance(cfg);
	}
	
	public static ArrayList<Condition> parseConditions(List<String> conditionLines) {
		ArrayList<Condition> conditions = new ArrayList<Condition>(conditionLines.size());
		for (String line : conditionLines) {
			conditions.add(getNew(new LineConfig(line)));
		}
		return conditions;
	}
	
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
