package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.util.LineConfig;

public class ConversationStage {
	private int num;
	private String text;
	private ActionSequence actions;
	private ArrayList<ConversationResponse> responses;
	
	public ConversationStage(ConfigurationSection cfg, int num) {
		this.num = num;
		String text = cfg.getString("text");
		LineConfig tcfg = new LineConfig(text);
		if (Action.dialogueActions.containsKey(tcfg.getKey())) {
			this.text = Action.parseDialogue(tcfg);
		}
		else {
			this.text = text;
		}
		
		actions = new ActionSequence(cfg.getStringList("actions"));
	}
	
	public int getNumber() {
		return num;
	}
	
	public ConversationResponse getResponse(int num) {
		return responses.size() < num ? responses.get(num) : null;
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
		}.runTaskLater(NeoQuests.inst(), (delay + runtime) * 20);
	}
	
	public void show(Player p) {
		p.sendMessage(text);
		int num = 1;
		for (ConversationResponse resp : responses) {
			if (resp.showResponse(p, num)) {
				num++;
			}
		}
		// Add an exit conversation response
	}
}
