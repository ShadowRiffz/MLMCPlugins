package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;

public interface Action {
	static HashMap<String, Action> actions = new HashMap<String, Action>();
	static HashMap<String, DialogueAction> dialogueActions = new HashMap<String, DialogueAction>();
	public default void run(Player p) {};
	public Action newInstance(LineConfig cfg);
	public String getKey();
	
	public static void register(String key, Action action) {
		actions.put(key, action);
		if (action instanceof DialogueAction) dialogueActions.put(key, (DialogueAction) action);
	}
	
	public static void clear() {
		actions.clear();
		dialogueActions.clear();
	}
	
	public static Action getNew(LineConfig cfg) {
		return actions.get(cfg.getKey()).newInstance(cfg);
	}
	
	public static String parseDialogue(LineConfig cfg) {
		return dialogueActions.get(cfg.getKey()).parseDialogue(cfg);
	}
}
