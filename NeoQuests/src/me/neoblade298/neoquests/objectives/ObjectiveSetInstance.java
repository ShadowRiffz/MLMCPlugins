package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.quests.QuestInstance;

public class ObjectiveSetInstance {
	private Player p;
	private QuestInstance quest;
	private ObjectiveSet set;
	private ArrayList<ObjectiveInstance> objs;
	
	public ObjectiveSetInstance(Player p, QuestInstance quest, ObjectiveSet set) {
		this.p = p;
		this.quest = quest;
		this.set = set;
		
		objs = new ArrayList<ObjectiveInstance>();
		for (Objective o : set.getObjectives()) {
			objs.add(new ObjectiveInstance(p, o, this));
		}
	}
	
	public void checkCompletion() {
		for (ObjectiveInstance o : objs) {
			if (!o.isComplete()) {
				return;
			}
		}
		quest.completeObjectiveSet(this);
	}
	
	public int getNext() {
		return set.getNext();
	}
	
	public void cleanup() {
		for (ObjectiveInstance o : objs) {
			o.cleanup();
		}
	}
	
	public ObjectiveSet getSet() {
		return set;
	}
}
