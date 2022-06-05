package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestNotCompletedCondition implements Condition {
	private static final String key;
	private ConditionResult result;
	private String questname;
	private boolean success;
	private int stage;
	
	static {
		key = "quest-not-completed";
	}
	
	public QuestNotCompletedCondition() {}
	
	public QuestNotCompletedCondition(String questname, int stage, boolean success, ConditionResult result) {
		this.questname = questname;
		this.stage = stage;
		this.success = success;
		this.result = result;
	}
	
	public QuestNotCompletedCondition(LineConfig cfg) {
		questname = cfg.getString("quest", "N/A").toUpperCase();
		result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
		stage = cfg.getInt("stage", -1);
		success = cfg.getBool("success", false); // If boolean matches, pass condition
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
		if (stage == -1) {
			return cq.isSuccess() == success;
		}
		else {
			return cq.isSuccess() == success && stage == cq.getStage();
		}
	}

	@Override
	public String getExplanation(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (cq != null) {
			return "You have already completed this quest!";
		}
		return "Error";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new QuestNotCompletedCondition(cfg);
	}

	@Override
	public ConditionResult getResult() {
		return result;
	}
}
