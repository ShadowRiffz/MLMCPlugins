package me.neoblade298.neomythicextension.mechanics;

import java.util.concurrent.ConcurrentHashMap;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import me.neoblade298.neomythicextension.MythicExt;

public class ModScore implements ITargetedEntitySkill {
	protected final String objective;
	protected final String operation;
	protected final int value;
	MythicExt nme;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ModScore(MythicLineConfig config, MythicExt nme) {
        this.nme = nme;
        
        this.objective = config.getString(new String[] {"objective", "obj", "o"}, "default");
        this.operation = config.getString(new String[] {"operation", "op"}, "+");
        this.value = config.getInteger(new String[] {"value", "v"}, 1);
	}

	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			String uuid = target.getBukkitEntity().getUniqueId().toString();
			
			// Check if this objective already exists
			ConcurrentHashMap<String, Integer> objScores = null;
			if (nme.scores.containsKey(objective)) {
				objScores = nme.scores.get(objective);
			}
			else {
				objScores = new ConcurrentHashMap<String, Integer>();
				nme.scores.put(objective, objScores);
			}
	
			// Get current score
			int score = 0;
			if (objScores.containsKey(uuid)) {
				score = objScores.get(uuid);
			}
			
			// Perform operation
			switch (operation) {
			case "+":
				nme.globalscores.put(objective, score + value);
			case "-":
				nme.globalscores.put(objective, score - value);
			case "*":
				nme.globalscores.put(objective, score * value);
			case "/":
				nme.globalscores.put(objective, score / value);
			}
			return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }

}
