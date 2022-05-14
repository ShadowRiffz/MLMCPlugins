package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;
import me.neoblade298.neoquests.io.QuestsConfigException;

public interface Action {
	static HashMap<String, Action> actions = new HashMap<String, Action>();
	static HashMap<String, DialogueAction> dialogueActions = new HashMap<String, DialogueAction>();
	public default void run(Player p) {};
	public Action newInstance(LineConfig cfg);
	public String getKey();
	
	public static void clear() {
		actions.clear();
		dialogueActions.clear();
	}
	
	public static Action getNew(LineConfig cfg) throws QuestsConfigException {
		if (!actions.containsKey(cfg.getKey())) {
			throw new QuestsConfigException("Invalid action: " + cfg.getKey());
		}
		try {
			return actions.get(cfg.getKey()).newInstance(cfg);
		}
		catch (Exception e) {
			throw new QuestsConfigException("Failed to load action: " + cfg.getKey());
		}
	}
	
	public static String parseDialogue(LineConfig cfg) {
		return dialogueActions.get(cfg.getKey()).parseDialogue(cfg);
	}
}
