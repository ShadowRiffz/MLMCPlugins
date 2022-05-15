package me.neoblade298.neoquests.actions;

import java.util.HashSet;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neoquests.NeoQuests;

public class ActionManager {
	private static LineConfigManager<Action> mngr;
	private static HashSet<String> dialogueActions;
	
	public ActionManager() {
		mngr = new LineConfigManager<Action>(NeoQuests.inst(), "actions");
		
		mngr.register(new DelayAction());
		mngr.register(new DescriptionDialogueAction());
		mngr.register(new MessageDialogueAction());
		mngr.register(new NPCDialogueAction());
		mngr.register(new PlayerDialogueAction());
	}
	
	public static Action get(LineConfig cfg) throws NeoIOException {
		return mngr.get(cfg);
	}
	
	public static boolean isDialogueAction(String key) {
		return dialogueActions.contains(key);
	}
}
