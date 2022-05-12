package me.neoblade298.neoquests.actions;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.util.LineConfig;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatColor;

public class NPCDialogueAction implements Action, DialogueAction {
	private static final String key;
	private String dialogue;
	
	static { 
		key = "npc";
		Action.register(key, new NPCDialogueAction());
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
		String name = CitizensAPI.getNPCRegistry().getById(cfg.getInt("id", -1)).getFullName();
		String text = cfg.getLine().replaceAll("&", "§");
		return name + "§7: " + text;
	}
	
	@Override
	public int getDelay() {
		return ChatColor.stripColor(this.dialogue).length() / 20;
	}
	
	@Override
	public String getKey() {
		return key;
	}
}
