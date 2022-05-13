package me.neoblade298.neoquests.actions;

public class ActionManager {
	static {
		DelayAction.register(Action.actions);
		DescriptionDialogueAction.register(Action.actions, Action.dialogueActions);
		EndConversationAction.register(Action.actions);
		MessageDialogueAction.register(Action.actions, Action.dialogueActions);
		NPCDialogueAction.register(Action.actions, Action.dialogueActions);
		PlayerDialogueAction.register(Action.actions, Action.dialogueActions);
	}
}
