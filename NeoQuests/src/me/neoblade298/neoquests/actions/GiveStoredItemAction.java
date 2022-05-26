package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;

public class GiveStoredItemAction implements RewardAction {
	private static String key = "give-storeditem";
	private String display;
	private int id;
	private int amount;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveStoredItemAction());
	}
	
	public GiveStoredItemAction() {}
	
	public GiveStoredItemAction(LineConfig cfg) {
		this.id = cfg.getInt("id", 0);
		this.amount = cfg.getInt("amount", 1);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveStoredItemAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		if (display == null) {
			display = "§f" + amount + " " + StorageManager.getItem(id).getDisplay();
		}
		return display;
	}

	@Override
	public void run(Player p) {
		StorageManager.givePlayer(p, id, amount);
	}

}
