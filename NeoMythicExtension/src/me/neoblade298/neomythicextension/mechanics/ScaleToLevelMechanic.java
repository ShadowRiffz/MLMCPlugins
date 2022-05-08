package me.neoblade298.neomythicextension.mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neobossinstances.BossInstances;

public class ScaleToLevelMechanic implements ITargetedEntitySkill {
	protected final String boss;
	protected final double exlevel, exhealth, xlevel, xhealth;
	protected BossInstances nbi;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ScaleToLevelMechanic(MythicLineConfig config, BossInstances nbi) {
        this.nbi = nbi;
        
        this.boss = config.getString("boss", "Ratface");
        this.xlevel = config.getDouble("xlevel", -1);
        this.xhealth = config.getDouble("xhealth", -1);
        this.exlevel = config.getDouble("exlevel", -1);
        this.exhealth = config.getDouble("exhealth", -1);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (this.nbi.getActiveFights().containsKey(this.boss)) { 
				int numPlayers = this.nbi.getActiveFights().get(this.boss).size();
		
		    	// Make sure target is a MythicMob
		    	if (MythicBukkit.inst().getAPIHelper().isMythicMob(target.getBukkitEntity())) {
		    		ActiveMob am = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(target.getBukkitEntity());
		    		double level = am.getLevel();
		    		AbstractEntity ent = am.getEntity();
		    		
		    		// Set level
		    		if (level < numPlayers && level >= 1) {
		    			if (ent.isValid()) {
			    			am.setLevel(numPlayers);
			    			level = numPlayers;
		    			}
		    			else {
		    				return SkillResult.CONDITION_FAILED;
		    			}
		    		}
		    		
		    		// Check if the boss is x or ex
		    		double oldHealth = ent.getMaxHealth();
		    		double newHealth = oldHealth;
		    		if (exlevel != -1 && level >= exlevel) {
		    			
		    		}
		    		else if (xlevel != -1 && level >= xlevel) {
		    			
		    		}
		    		else if (level >= 1) {
		    			newHealth *= 0.5 + (Math.min(6, level) * 0.5);
		    			if (level > 6) {
		    				newHealth += (level - 6) * 0.2 * oldHealth;
		    			}
		    		}
		    		else if (level >= -99) {
		    			newHealth *= 1 + (0.01 * level);
		    		}
		    		
		    		if (ent.isValid()) {
			    		ent.setMaxHealth(newHealth);
			    		ent.setHealth(newHealth);
		    		}
		    	}
			}
	    	return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }

}
