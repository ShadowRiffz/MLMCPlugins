package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.io.QuestsConfigException;
import me.neoblade298.neoquests.io.LineConfig;

public class ConversationStage {
	private int num;
	private String text;
	private ActionSequence actions = new ActionSequence();
	private ArrayList<ConversationResponse> responses = new ArrayList<ConversationResponse>();
	
	public ConversationStage(ConfigurationSection cfg, int num) throws QuestsConfigException {
		this.num = num;
		String text = cfg.getString("text");
		LineConfig tcfg = new LineConfig(text);
		if (Action.dialogueActions.containsKey(tcfg.getKey())) {
			this.text = Action.parseDialogue(tcfg);
		}
		else {
			this.text = text;
		}
		
		actions.load(cfg.getStringList("actions"));

		ConfigurationSection rcfg = cfg.getConfigurationSection("responses");
		for (String key : rcfg.getKeys(false)) {
			responses.add(new ConversationResponse(rcfg.getConfigurationSection(key)));
		}
		responses.add(new ConversationResponse());
	}
	
	public int getNumber() {
		return num;
	}
	
	public ConversationResponse getResponse(int num) {
		return responses.size() > num ? responses.get(num) : null;
	}
	
	public ActionSequence getActions() {
		return actions;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void run(Player p) {
		run(p, 0);
	}
	
	public void run(Player p, int delay) {
		int runtime = actions.run(p, delay);
		new BukkitRunnable() {
			public void run() {
				show(p);
			}
		}.runTaskLater(NeoQuests.inst(), delay + runtime);
	}
	
	public void show(Player p) {
		p.sendMessage(text);
		int num = 1;
		for (ConversationResponse resp : responses) {
			if (resp.showResponse(p, num)) {
				num++;
			}
		}
	}
}
