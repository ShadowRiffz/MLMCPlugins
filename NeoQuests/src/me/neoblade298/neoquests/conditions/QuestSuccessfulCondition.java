package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestSuccessfulCondition implements Condition {
	private static final String key;
	private ConditionResult result;
	private String questname;
	private boolean hide, negate;
	private int stage;
	
	static {
		key = "quest-completed";
	}
	
	public QuestSuccessfulCondition() {}
	
	public QuestSuccessfulCondition(LineConfig cfg) {
		result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
		hide = cfg.getBool("hide", false);
		negate = cfg.getBool("negate", false);
		
		questname = cfg.getString("quest", "N/A").toUpperCase();
		stage = cfg.getInt("stage", -1);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (!negate) {
			if (cq == null) {
				return false;
			}
			
			if (cq.getStage() != stage && stage != -1) {
				return false;
			}
			return true;
		}
		else {
			if (cq != null) {
				if (cq.getStage() == stage || stage == -1) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	public String getExplanation(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		
		if (!negate) {
			if (cq == null) {
				return "You have not completed quest " + cq.getQuest().getDisplay();
			}
			
			else if (cq.getStage() != stage && stage != -1) {
				return "You must complete quest " + cq.getQuest().getDisplay() + " in a different way!";
			}
		}
		else {
			if (cq != null) {
				if (cq.getStage() == stage || stage == -1) {
					return "You must not complete quest " + cq.getQuest().getDisplay() + " with this result!";
				}
			}
		}
		return "Error";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new QuestSuccessfulCondition(cfg);
	}

	@Override
	public ConditionResult getResult() {
		return result;
	}
}
