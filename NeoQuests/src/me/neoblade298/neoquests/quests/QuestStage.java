package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.objectives.Objective;

public class QuestStage {
	private Quest quest;
	private ArrayList<Objective> objectives;
	private int next;
	private ActionSequence actions;
	
	public int complete(Player p) {
		actions.run(p);
		return actions.getRuntime();
	}
	
	public int getNext() {
		return next;
	}
}
