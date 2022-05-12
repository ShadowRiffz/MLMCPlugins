package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.conditions.Condition;

public class Conversation {
	private String key;
	private ArrayList<Condition> conditions;
	private ArrayList<ConversationStage> stages;
	private ActionSequence startActions, endActions;
	
	public Conversation(String key, ConfigurationSection cfg) {
		this.key = key;
		this.conditions = Condition.parseConditions(cfg.getStringList("conditions"));
		stages = new ArrayList<ConversationStage>();
		ConfigurationSection scfg = cfg.getConfigurationSection("stages");
		for (String stageNum : scfg.getKeys(false)) {
			int num = Integer.parseInt(stageNum);
			stages.add(new ConversationStage(scfg, num));
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public ConversationStage getStage(int num) {
		return stages.get(num);
	}
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}
	
	public ActionSequence getStartActions() {
		return startActions;
	}
	
	public ActionSequence getEndActions() {
		return endActions;
	}
}
