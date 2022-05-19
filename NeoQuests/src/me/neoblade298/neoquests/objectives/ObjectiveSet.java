package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.quests.QuestStage;

public class ObjectiveSet {
	private ArrayList<Objective> objs = new ArrayList<Objective>();
	private ArrayList<RewardAction> alternateRewards;
	private int next;
	
	public ObjectiveSet(ConfigurationSection cfg) throws NeoIOException {
		next = cfg.getInt("next", -3);
		for (String line : cfg.getStringList("objectives")) {
			ObjectiveManager.get(new LineConfig(line));
		}
		if (cfg.getKeys(false).contains("random-objectives")) {
			int min = cfg.getInt("random-min");
			int max = cfg.getInt("random-max");
		}
	}
	
	public int getNext() {
		return next;
	}
	
	public ArrayList<RewardAction> getAlternateRewards() {
		return alternateRewards;
	}
	
	public boolean hasAlternateRewards() {
		return alternateRewards != null;
	}
	
	public ArrayList<Objective> getObjectives() {
		return objs;
	}
}
