package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.io.LineConfig;

public class EndConversationAction implements Action {
	private static final String key = "end";
	private boolean runEndActions;
	
	public static void register(HashMap<String, Action> actions) {
		actions.put(key, new EndConversationAction());
	}
	
	public EndConversationAction() {}
	
	public EndConversationAction(LineConfig cfg) {
		runEndActions = cfg.getBool(key, false);
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new EndConversationAction();
	}
	
	@Override
	public void run(Player p) {
		ConversationManager.endConversation(p, runEndActions);
	}

	@Override
	public String getKey() {
		return key;
	}
	
}
