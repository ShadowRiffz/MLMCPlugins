package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;
import java.util.List;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.QuestStage;

public class ObjectiveSet {
	private QuestStage stage;
	private ArrayList<Objective> objs;
	
	public ObjectiveSet(List<String> list) {
		for (String line : list) {
			ObjectiveManager.parseObjective(new LineConfig(line));
		}
	}
	
	public boolean check() {
		for (Objective o : objs) {
			if (!o.isComplete()) {
				return false;
			}
		}
		return true;
	}
}
