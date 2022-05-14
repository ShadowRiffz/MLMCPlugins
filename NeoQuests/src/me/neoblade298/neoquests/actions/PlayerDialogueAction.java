package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;

public class PlayerDialogueAction implements Action, DialogueAction {
	private static final String key = "player";
	private String dialogue;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new PlayerDialogueAction());
		dialogueActions.put(key, new NPCDialogueAction());
	}
	
	public PlayerDialogueAction() {}
	
	public PlayerDialogueAction(LineConfig cfg) {
		this.dialogue = parseDialogue(cfg);
	}

	@Override
	public void run(Player p) {
		p.sendMessage(this.dialogue);
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new PlayerDialogueAction(cfg);
	}
	
	@Override
	public String parseDialogue(LineConfig cfg) {
		return "§7§o" + cfg.getLine();
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
