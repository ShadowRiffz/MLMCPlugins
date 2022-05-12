package me.neoblade298.neoquests.actions;

import me.neoblade298.neoquests.util.LineConfig;

public interface DialogueAction {
	public String parseDialogue(LineConfig cfg);
	public int getDelay();
}
