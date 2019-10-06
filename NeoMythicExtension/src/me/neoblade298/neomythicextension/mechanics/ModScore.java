package me.neoblade298.neomythicextension.mechanics;

import java.util.HashMap;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neomythicextension.Main;

public class ModScore extends SkillMechanic implements ITargetedEntitySkill {
	protected final String objective;
	protected final String operation;
	protected final int value;
	Main nme;

	public ModScore(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        this.nme = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoMythicExtension");
        
        this.objective = config.getString(new String[] {"objective", "obj", "o"}, "default");
        this.operation = config.getString(new String[] {"operation", "op"}, "+");
        this.value = config.getInteger(new String[] {"value", "v"}, 1);
	}

    // CURRENTLY UNUSED MECHANIC
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		String uuid = target.getBukkitEntity().getUniqueId().toString();
		
		// Check if this objective already exists
		HashMap<String, Integer> objScores = null;
		if (nme.scores.containsKey(objective)) {
			objScores = nme.scores.get(objective);
		}
		else {
			objScores = new HashMap<String, Integer>();
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
		return true;
    }

}
