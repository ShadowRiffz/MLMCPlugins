package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.conditions.Condition;

public class Quest {
	private String key, name;
	private ArrayList<Condition> conditions;
	private ArrayList<RewardAction> rewards;
	private ArrayList<QuestStage> stages;
	private int stage;
	
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
