package me.neoblade298.neomythicextension.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import me.neoblade298.neomythicextension.MythicExt;

public class ModGlobalScore implements INoTargetSkill {
	protected final String objective;
	protected final String operation;
	protected final int value;
	MythicExt nme;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ModGlobalScore(MythicLineConfig config, MythicExt nme) {
        this.nme = nme;
        
        this.objective = config.getString(new String[] {"objective", "obj", "o"}, "default");
        this.operation = config.getString(new String[] {"operation", "op"}, "=");
        this.value = config.getInteger(new String[] {"value", "v"}, 1);
	}
	
	@Override
    public SkillResult cast(SkillMetadata data) {
		try {
			// Get current score
			int score = 0;
			if (nme.globalscores.containsKey(objective)) {
				score = nme.globalscores.get(objective);
			}
			
			// Perform operation
			switch (operation) {
			case "=":
				nme.globalscores.put(objective, value); break;
			case "+":
				nme.globalscores.put(objective, score + value); break;
			case "-":
				nme.globalscores.put(objective, score - value); break;
			case "*":
				nme.globalscores.put(objective, score * value); break;
			case "/":
				nme.globalscores.put(objective, score / value); break;
			}
			return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }

}
