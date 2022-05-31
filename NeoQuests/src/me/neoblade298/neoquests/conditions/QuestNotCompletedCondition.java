package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestNotCompletedCondition implements Condition {
	private static final String key;
	private ConditionResult result;
	private String questname;
	
	static {
		key = "quest-not-completed";
	}
	
	public QuestNotCompletedCondition() {}
	
	public QuestNotCompletedCondition(String questname) {
		this.questname = questname;
		this.result = ConditionResult.UNCLICKABLE;
	}
	
	public QuestNotCompletedCondition(LineConfig cfg) {
		questname = cfg.getString("quest", "N/A").toUpperCase();
		result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		return QuestsManager.getQuester(p).getCompletedQuest(questname) == null;
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
