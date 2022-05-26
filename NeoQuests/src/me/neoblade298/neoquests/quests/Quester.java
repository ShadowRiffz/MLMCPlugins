package me.neoblade298.neoquests.quests;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Quester {
	private Player p;
	private HashMap<String, CompletedQuest> completedQuests;
	private HashMap<String, QuestInstance> activeQuests;
	
	public Player getPlayer() {
		return p;
	}
	
	public void completeQuest(QuestInstance qi, int stage, boolean success) {
		qi.cleanup();
		activeQuests.remove(qi.getQuest().getName());
		completedQuests.put(qi.getQuest().getName(), new CompletedQuest(qi.getQuest(), stage, success));
	}
	
	public void cancelQuest(String name) {
		if (activeQuests.containsKey(name.toUpperCase())) {
			QuestInstance qi = activeQuests.remove(name.toUpperCase());
			qi.cleanup();
		}
	}
	
	public void startQuest(Quest q) {
		activeQuests.put(q.getKey(), new QuestInstance(this, q));
	}
}
