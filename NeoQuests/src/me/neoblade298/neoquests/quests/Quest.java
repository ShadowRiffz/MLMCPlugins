package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Quest {
	private String key, name;
	private ArrayList<Condition> conditions;
	private ArrayList<RewardAction> rewards;
	private ArrayList<QuestStage> stages;
	private int stage;
	
	public Quest(String key, ConfigurationSection cfg) throws NeoIOException {
		this.key = key;
		
		this.name = cfg.getString("name");
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		this.rewards = ActionManager.parseRewards(cfg.getStringList("rewards"));
		
		this.stages = QuestStage.parseQuestStages(cfg.getConfigurationSection("stages"));
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
	
}
