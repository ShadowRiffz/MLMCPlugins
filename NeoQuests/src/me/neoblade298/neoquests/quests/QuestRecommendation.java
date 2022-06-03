package me.neoblade298.neoquests.quests;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;

public class QuestRecommendation {
	private int min, max;
	private Quest quest;
	
	public QuestRecommendation(LineConfig cfg) throws NeoIOException {
		this.min = cfg.getInt("min", 1);
		this.max = cfg.getInt("max", 60);
		this.quest = QuestsManager.getQuest(cfg.getString("quest", "N/A").toUpperCase());
		
		if (this.quest == null) {
			throw new NeoIOException("Failed to load quest " + cfg.getString("quest", "N/A") + " for recommendation " + cfg.getFullLine());
		}
	}
	
	public boolean isRelevant(Player p) {
		int level = SkillAPI.getPlayerData(p).getClass("class").getLevel();
		return level >= min && level <= max;
	}
	
	public Quest getQuest() {
		return quest;
	}
}
