package me.neoblade298.neomythicextension.mechanics;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neomythicextension.Main;

public class ModGlobalScore extends SkillMechanic implements ITargetedEntitySkill {
	protected final String objective;
	protected final String operation;
	protected final int value;
	Main nme;

	public ModGlobalScore(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        this.nme = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoMythicExtension");
        
        this.objective = config.getString(new String[] {"objective", "obj", "o"}, "default");
        this.operation = config.getString(new String[] {"operation", "op"}, "+");
        this.value = config.getInteger(new String[] {"value", "v"}, 1);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		// Get current score
		int score = 0;
		if (nme.globalscores.containsKey(objective)) {
			score = nme.globalscores.get(objective);
		}
		
		// Perform operation
		switch (operation) {
		case "+":
			nme.globalscores.put(objective, score + value); break;
		case "-":
			nme.globalscores.put(objective, score - value); break;
		case "*":
			nme.globalscores.put(objective, score * value); break;
		case "/":
			nme.globalscores.put(objective, score / value); break;
		}
		return true;
    }

}
