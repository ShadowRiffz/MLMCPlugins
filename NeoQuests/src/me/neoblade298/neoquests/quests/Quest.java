package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conditions.QuestNotCompletedCondition;

public class Quest {
	private String key, name, startConv;
	private ArrayList<Condition> conditions;
	private ArrayList<RewardAction> rewards;
	private ArrayList<QuestStage> stages;
	private Questline ql;
	private boolean canRepeat = false, canRetry = false;
	private int stage;
	
	public Quest(String key, ConfigurationSection cfg) throws NeoIOException {
		this.key = key;
		
		this.name = cfg.getString("name");
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		this.rewards = ActionManager.parseRewards(cfg.getStringList("rewards"));
		this.startConv = cfg.getString("start-conversation");
		
		this.stages = QuestStage.parseQuestStages(cfg.getConfigurationSection("stages"), this);
		if (cfg.getBoolean("repeatable", false)) {
			canRepeat = true;
			this.conditions.add(new QuestNotCompletedCondition());
		}
		canRetry = cfg.getBoolean("retryable", false);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	
	public void completeStage(Player p) {
		stages.get(stage).complete(p);
	}
	
	public ArrayList<QuestStage> getStages() {
		return stages;
	}
	
	public ArrayList<RewardAction> getRewards() {
		return rewards;
	}
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}
	
	public boolean canRepeat() {
		return canRepeat;
	}
	
	public boolean canRetry() {
		return canRepeat;
	}
	
	public void setQuestline(Questline ql) throws NeoIOException {
		if (this.ql != null) {
			throw new NeoIOException("Quest " + this.key + " is already in questline " + this.ql.getKey() + ", can't set it in other questline " + ql.getKey());
		}
		this.ql = ql;
	}
	
	public Questline getQuestline() {
		return ql;
	}
}
