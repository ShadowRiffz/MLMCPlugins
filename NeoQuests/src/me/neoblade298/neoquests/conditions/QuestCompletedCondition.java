package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestCompletedCondition implements Condition {
	private static final String key;
	private ConditionResult result;
	private String questname;
	private int stage;
	private boolean success;
	
	static {
		key = "quest-completed";
	}
	
	public QuestCompletedCondition() {}
	
	public QuestCompletedCondition(LineConfig cfg) {
		questname = cfg.getString("quest", "N/A").toUpperCase();
		result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
		stage = cfg.getInt("stage", -1);
		success = cfg.getBool("success", true);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		
		if (cq == null) {
			return false;
		}
		
		if (cq.getStage() != stage && stage != -1) {
			return false;
		}
		
		if (cq.isSuccess() != success) {
			return false;
		}
		return true;
	}

	@Override
	public String getExplanation(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		
		if (cq == null) {
			return "You have not completed this quest!";
		}
		
		else if (cq.getStage() != stage && stage != -1) {
			return "You must complete this quest another way!";
		}
		
		else if (cq.isSuccess() != success) {
			String type = cq.isSuccess() ? "pass" : "fail";
			return "You must " + type + " this quest!";
		}
		return "Error";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new QuestCompletedCondition(cfg);
	}

	@Override
	public ConditionResult getResult() {
		return result;
	}
}
