package me.neoblade298.neoquests.actions;

import me.neoblade298.neoquests.util.LineConfig;

public interface DialogueAction extends Action, DelayableAction {
	public String parseDialogue(LineConfig cfg);
}
