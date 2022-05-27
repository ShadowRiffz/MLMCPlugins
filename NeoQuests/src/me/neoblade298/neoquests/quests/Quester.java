package me.neoblade298.neoquests.quests;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Quester {
	private UUID uuid;
	private HashMap<String, CompletedQuest> completedQuests = new HashMap<String, CompletedQuest>();
	private HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();;
	
	public Quester(UUID uuid) {
		this.uuid = uuid;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
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
