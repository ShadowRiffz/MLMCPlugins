package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.objectives.ObjectiveSet;

public class QuestStage {
	private Quest quest;
	private ArrayList<ObjectiveSet> objectives;
	private int next;
	private ActionSequence actions;
	
	public int complete(Player p) {
		actions.run(p);
		return actions.getRuntime();
	}
	
	public int getNext() {
		return next;
	}
	
	public ArrayList<ObjectiveSet> getObjectives() {
		return objectives;
	}
	
	public static ArrayList<QuestStage> parseQuestStages(ConfigurationSection cfg) {
		ArrayList<QuestStage> stages = new ArrayList<QuestStage>();
		for (String key : cfg.getKeys(false)) {
			ConfigurationSection sec = cfg.getConfigurationSection(key);
			
		}
	}
}
