package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CopyQuestConditionsCondition extends Condition {
	private static final String key;
	private String questname;
	
	static {
		key = "copy-quest-conditions";
	}
	
	public CopyQuestConditionsCondition() {}
	
	public CopyQuestConditionsCondition(LineConfig cfg) {
		super(cfg);
		
		questname = cfg.getString("quest", "N/A").toUpperCase();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		return ConditionManager.getBlockingCondition(p, QuestsManager.getQuest(questname).getConditions()) == null;
	}

	@Override
	public String getExplanation(Player p) {
		return ConditionManager.getBlockingCondition(p, QuestsManager.getQuest(questname).getConditions()).getExplanation(p);
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new CopyQuestConditionsCondition(cfg);
	}
}
