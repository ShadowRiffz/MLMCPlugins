package me.neoblade298.neoquests.quests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.objectives.ObjectiveSet;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;

public class QuestInstance {
	private Quester q;
	private Quest quest;
	private int stage;
	private LinkedHashMap<String, ObjectiveSetInstance> sets;
	
	public QuestInstance(Quester quester, Quest quest) {
		this(quester, quest, 0);
	}
	
	public QuestInstance(Quester quester, Quest quest, int stage) {
		this.q = quester;
		this.quest = quest;
		this.stage = stage;
		this.sets = new LinkedHashMap<String, ObjectiveSetInstance>();
	}
	
	// Used anytime new objectives show up
	public void cleanupInstances() {
		for (ObjectiveSetInstance i : sets.values()) {
			i.stopListening();
		}
		sets.clear();
	}
	
	public void setupInstances() {
		cleanupInstances();
		for (ObjectiveSet set : quest.getStages().get(stage).getObjectives()) {
			ObjectiveSetInstance osi = new ObjectiveSetInstance(q.getPlayer(), this, set);
			sets.put(set.getKey(), osi);
			osi.startListening();
		}
	}
	
	public void completeObjectiveSet(ObjectiveSetInstance set) {
		set.finalizeObjectives();
		if (set.getNext() == -1 || set.getNext() == -2) {
			endQuest(set, set.getNext() == -1, stage);
			return;
		}
		else if (set.getNext() == -3) {
			stage = stage + 1 >= quest.getStages().size() ? stage : stage + 1; // Next stage if one exists
		}
		else {
			stage = set.getNext();
		}
		
		// Setup new stage
		setupInstances();
		q.displayObjectives(q.getPlayer());
	}
	
	public void endQuest(ObjectiveSetInstance si, boolean success, int stage) {
		Player p = q.getPlayer();
		if (success) {
			q.completeQuest(this, stage, success);
			ArrayList<RewardAction> rewards = quest.getRewards();
			if (si.getSet().hasAlternateRewards()) {
				rewards = si.getSet().getAlternateRewards();
			}
			if (rewards.size() > 0) {
				p.sendMessage("�6Rewards:");
				for (RewardAction r : rewards) {
					if (r.getDisplay() != null) {
						p.sendMessage("�7- " + r.getDisplay());
					}
				}
				for (RewardAction r : rewards) {
					r.run(p);
				}
			}
		}
		else {
			q.cancelQuest(quest.getKey());
			p.sendMessage("�c[�4�lMLMC�4] �cYou failed �6" + quest.getDisplay() + "�c!");
		}
	}
	
	public Quest getQuest() {
		return quest;
	}
	
	public void stopListening() {
		for (ObjectiveSetInstance si : sets.values()) {
			si.stopListening();
		}
	}
	
	public ObjectiveSetInstance getObjectiveSetInstance(String key) {
		return sets.get(key);
	}
	
	public Collection<ObjectiveSetInstance> getObjectiveSetInstances() {
		return sets.values();
	}
	
	public void initialize() {
		for (ObjectiveSetInstance osi : sets.values()) {
			osi.initialize();
		}
	}
	
	public int getStage() {
		return stage;
	}
}
