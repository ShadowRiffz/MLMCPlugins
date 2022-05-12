package me.neoblade298.neoquests.actions;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.util.LineConfig;
import net.citizensnpcs.api.CitizensAPI;

public class NPCDialogueAction implements Action, DialogueAction {
	private String dialogue;
	
	static { 
		Action.register("npc", new NPCDialogueAction(), true);
	}
	
	public NPCDialogueAction() {}
	
	public NPCDialogueAction(String dialogue) {
		this.dialogue = dialogue;
	}

	@Override
	public void run(Player p) {
		p.sendMessage(this.dialogue);
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new NPCDialogueAction(parseDialogue(cfg));
	}
	
	@Override
	public String parseDialogue(LineConfig cfg) {
		String name = CitizensAPI.getNPCRegistry().getById(cfg.getInt("id")).getFullName();
		String text = cfg.getLine().replaceAll("&", "§");
		return name + "§7: " + text;
	}
	
	@Override
	public int getDelay() {
		return this.dialogue.length() / 20;
	}
	
}
