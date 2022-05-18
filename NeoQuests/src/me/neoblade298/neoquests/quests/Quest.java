package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.RewardAction;

public class Quest {
	private ArrayList<RewardAction> rewards;
	private ArrayList<QuestStage> stages;
	private int stage;
	
	public void completeStage(Player p) {
		stages.get(stage).complete(p);
	}
}
