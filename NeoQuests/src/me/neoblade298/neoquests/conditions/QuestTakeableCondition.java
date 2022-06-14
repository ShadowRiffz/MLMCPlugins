package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestTakeableCondition implements Condition {
	private static final String key;
	private ConditionResult result;
	private String questname;
	
	static {
		key = "quest-takeable";
	}
	
	public QuestTakeableCondition() {}
	
	public QuestTakeableCondition(LineConfig cfg) {
		result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
		
		questname = cfg.getString("quest", "N/A").toUpperCase();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (cq == null) {
			return true;
		}
		Quest q = cq.getQuest();
		if (cq.isSuccess()) {
			return q.canRepeat();
		}
		return q.canRetry() || q.canRepeat();
	}

	@Override
	public String getExplanation(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (cq == null) {
			return "Error";
		}
		if (cq.isSuccess()) {
			return "You successfully completed this quest and it can't be repeated!";
		}
		return "You can't retry or repeat this quest!";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new QuestTakeableCondition(cfg);
	}

	@Override
	public ConditionResult getResult() {
		return result;
	}
}
