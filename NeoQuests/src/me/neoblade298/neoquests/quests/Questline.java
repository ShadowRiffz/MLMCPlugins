package me.neoblade298.neoquests.quests;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;

public class Questline implements Comparator<Questline> {
	private String key, display, fileLocation;
	private ArrayList<Quest> quests;

	public Questline(ConfigurationSection cfg, File file) throws NeoIOException {
		key = cfg.getName().toUpperCase();
		fileLocation = file.getAbsolutePath();
		display = cfg.getString("display");
		
		List<String> recs = cfg.getStringList("recommendations");
		quests = new ArrayList<Quest>(recs.size());
		for (String line : recs) {
			Quest q = QuestsManager.getQuest(line);
			q.setQuestline(this);
			quests.add(q);
		}
	}

	public boolean isCompleted(Player p) {
		Quester q = QuestsManager.getQuester(p);
		CompletedQuest cq = q.getCompletedQuest(quests.get(quests.size() - 1).getKey());
		if (cq == null) {
			return false;
		}
		return canTakeQuest(cq);
	}
	
	public Quest getFirstQuest() {
		return quests.get(0);
	}

	public Quest getNextQuest(Player p) {
		Quester q = QuestsManager.getQuester(p);
		for (Quest quest : quests) {
			CompletedQuest cq = q.getCompletedQuest(quest.getKey());
			if (cq == null) continue;
			if (canTakeQuest(cq)) return quest;
		}
		return null;
	}

	private boolean canTakeQuest(CompletedQuest cq) {
		if (cq.isSuccess()) {
			return true;
		}
		Quest q = cq.getQuest();
		return q.canRepeat() || q.canRetry();
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getLastQuest() {
		return quests.get(quests.size() - 1).getKey();
	}

	@Override
	public int compare(Questline q1, Questline q2) {
		return q1.getKey().compareTo(q2.getKey());
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
}
