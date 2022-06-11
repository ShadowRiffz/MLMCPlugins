package me.neoblade298.neoquests.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neoquests.NeoQuests;

public class ConditionManager {
	private static LineConfigManager<Condition> mngr;
	
	public ConditionManager() {
		mngr = new LineConfigManager<Condition>(NeoQuests.inst(), "conditions");
		
		mngr.register(new ClassLevelCondition());
		mngr.register(new QuestSuccessfulCondition());
		mngr.register(new QuestCompletedCondition());
	}
	
	public static ArrayList<Condition> parseConditions(List<String> conditionLines) throws NeoIOException {
		ArrayList<Condition> conds = new ArrayList<Condition>(conditionLines.size());
		for (String line : conditionLines) {
			try {
				conds.add(mngr.get(new LineConfig(line)));
			}
			catch (Exception e) {
				throw new NeoIOException("Failed to load condition line: " + line);
			}
		}
		return conds;
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
