package me.neoblade298.neoquests.conversations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.events.ConversationEvent;

public class ConversationInstance {
	private Player p;
	private Conversation conv;
	private int stage = 0;
	
	public ConversationInstance(Player p, Conversation conv) {
		this.p = p;
		this.conv = conv;
		
		ActionSequence start = conv.getStartActions();
		int runtime = 0;
		if (start != null) {
			start.run(p);
			runtime = start.getRuntime();
		}
		conv.getStage(0).run(p, runtime);
	}
	
	public void show() {
		this.conv.getStage(stage).show(p);
	}
	
	public void endConversation(boolean runActions) {
		if (runActions) {
			conv.getEndActions().run(p);
		}
	}
	
	public boolean chooseResponse(int num) {
		ConversationResponse resp = conv.getStage(stage).getResponse(num);
		if (resp != null) {
			if (resp.tryResponse(p)) {
				// Successfully choose response
				Bukkit.getPluginManager().callEvent(new ConversationEvent(conv, conv.getStage(stage), resp));
				int runtime = resp.getActions().run(p);
				
				if (resp.getNext() != -1) {
					stage = resp.getNext();
				}
				else if (stage + 1 < conv.getTotalStages()) {
					stage++;
				}
				conv.getStage(stage).run(p, runtime);
			}
			return true;
		}
		else {
			return false;
		}
	}
}
