package me.neoblade298.neoquests.actions;

import org.bukkit.entity.Player;

import me.neoblade298.neoquests.io.LineConfig;
import net.md_5.bungee.api.ChatColor;

public class DescriptionDialogueAction implements Action, DialogueAction {
	private static final String key;
	private String dialogue;
	
	static { 
		key = "player";
		Action.register(key, new DescriptionDialogueAction());
	}
	
	public DescriptionDialogueAction() {}
	
	public DescriptionDialogueAction(LineConfig cfg) {
		this.dialogue = parseDialogue(cfg);
	}

	@Override
	public void run(Player p) {
		p.sendMessage("§e" + p.getName() + this.dialogue);
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new DescriptionDialogueAction(cfg);
	}
	
	@Override
	public String parseDialogue(LineConfig cfg) {
		return "§7: " + cfg.getLine();
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
