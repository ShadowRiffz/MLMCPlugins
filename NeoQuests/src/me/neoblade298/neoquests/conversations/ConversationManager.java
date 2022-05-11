package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.conversations.Conversation;

import me.neoblade298.neoquests.Reloadable;

public class ConversationManager implements Reloadable {
	private static HashMap<Integer, ArrayList<Conversation>> npcConvs;
	private static HashMap<String, Conversation> convs;
	
	public ConversationManager() {
		// Load convs folder
		reload();
	}
	
	public void reload() {
		
	}
}
