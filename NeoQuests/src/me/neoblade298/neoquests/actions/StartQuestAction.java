package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class StartQuestAction implements Action {
	private static String key = "start-quest";
	private String quest;
	private Quest q;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new StartQuestAction());
	}
	
	public StartQuestAction() {}
	
	public StartQuestAction(LineConfig cfg) {
		this.quest = cfg.getString("name", "N/A");
	}

	@Override
	public Action create(LineConfig cfg) {
		return new StartQuestAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		QuestsManager.startQuest(p, quest);
	}
	
	public Quest getQuest() {
		if (q == null) {
			q = QuestsManager.getQuest(quest);
		}
		return q;
	}

}
