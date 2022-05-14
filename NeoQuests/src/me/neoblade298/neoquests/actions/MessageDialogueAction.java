package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;

public class MessageDialogueAction implements Action, DialogueAction {
	private static final String key = "message";
	private String dialogue;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new MessageDialogueAction());
		dialogueActions.put(key, new MessageDialogueAction());
	}
	
	public MessageDialogueAction() {}
	
	public MessageDialogueAction(LineConfig cfg) {
		this.dialogue = parseDialogue(cfg);
	}

	@Override
	public void run(Player p) {
		p.sendMessage(this.dialogue);
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new MessageDialogueAction(cfg);
	}
	
	@Override
	public String parseDialogue(LineConfig cfg) {
		return cfg.getLine();
	}
	
	@Override
	public int getDelay() {
		return DialogueAction.getDelay(this.dialogue);
	}
	
	@Override
	public String getKey() {
		return key;
	}
}
