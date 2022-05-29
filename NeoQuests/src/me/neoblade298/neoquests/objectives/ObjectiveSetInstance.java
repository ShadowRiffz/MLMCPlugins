package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.quests.QuestInstance;

public class ObjectiveSetInstance {
	private Player p;
	private QuestInstance quest;
	private ObjectiveSet set;
	private ArrayList<ObjectiveInstance> objs;
	private String key;
	
	public ObjectiveSetInstance(Player p, QuestInstance quest, ObjectiveSet set) {
		this.p = p;
		this.quest = quest;
		this.set = set;
		this.key = set.getKey();
		
		objs = new ArrayList<ObjectiveInstance>();
		for (Objective o : set.getObjectives()) {
			objs.add(new ObjectiveInstance(p, o, this));
		}
	}
	
	public ObjectiveSetInstance(Player p, QuestInstance quest, ObjectiveSet set, ObjectiveInstance insts) {
		this.p = p;
		this.quest = quest;
		this.set = set;
		this.key = set.getKey();
		
		objs = new ArrayList<ObjectiveInstance>();
		for (Objective o : set.getObjectives()) {
			objs.add(new ObjectiveInstance(p, o, this));
		}
	}
	
	public String getKey() {
		return key;
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
	
	public void setObjectiveCounts(int[] counts) {
		if (counts.length != objs.size()) {
			Bukkit.getLogger().warning("[NeoQuests] Player " + p.getName() + " failed to load objective set " + key + " for quest " + quest.getQuest().getKey() + ", " +
					"counts.length " + counts.length + " != objs.size " + objs.size());
			return;
		}
		
		int i = 0;
		for (ObjectiveInstance oi : objs) {
			if (oi.getObjective().getNeeded() < counts[i++]) {
				Bukkit.getLogger().warning("[NeoQuests] Player " + p.getName() + " failed to load objective set " + key + " for quest " + quest.getQuest().getKey() + ", " +
						"objective " + oi.getObjective().getKey() + " needed " + oi.getObjective().getNeeded() + " < counts[i] " + counts[i]);
				return;
			}
		}
		
		for (ObjectiveInstance oi : objs) {
			oi.setCount(i++);
		}
	}
	
	public ArrayList<ObjectiveInstance> getObjectives() {
		return objs;
	}
}