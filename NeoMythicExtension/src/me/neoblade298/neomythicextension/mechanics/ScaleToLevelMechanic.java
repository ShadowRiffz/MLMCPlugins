package me.neoblade298.neomythicextension.mechanics;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neobossinstances.Main;

public class ScaleToLevelMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final String boss;
	protected final double exlevel, exhealth, xlevel, xhealth;
	protected Main nbi;

	public ScaleToLevelMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        this.nbi = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoBossInstances");
        
        this.boss = config.getString("boss", "Ratface");
        this.xlevel = config.getDouble("xlevel", -1);
        this.xhealth = config.getDouble("xhealth", -1);
        this.exlevel = config.getDouble("exlevel", -1);
        this.exhealth = config.getDouble("exhealth", -1);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (this.nbi.getActiveFights().containsKey(this.boss)) { 
			int numPlayers = this.nbi.getActiveFights().get(this.boss).size();
	
	    	// Make sure target is a MythicMob
	    	if (MythicMobs.inst().getAPIHelper().isMythicMob(target.getBukkitEntity())) {
	    		ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(target.getBukkitEntity());
	    		double level = am.getLevel();
	    		if (level < numPlayers) {
	    			am.setLevel(numPlayers);
	    			level = numPlayers;
	    		}
	    		
	    		// Check if the boss is x or ex
	    		AbstractEntity ent = am.getEntity();
	    		double oldHealth = ent.getMaxHealth();
	    		double newHealth = oldHealth;
	    		if (exlevel != -1 && level >= exlevel) {
	    			
	    		}
	    		else if (xlevel != -1 && level >= xlevel) {
	    			
	    		}
	    		else {
	    			newHealth *= 0.5 + (Math.min(6, level) * 0.5);
	    			if (level > 6) {
	    				newHealth += (level - 6) * 0.2 * oldHealth;
	    			}
	    		}
	    		
	    		if (ent.isDead()) {
		    		ent.setMaxHealth(newHealth);
		    		ent.setHealth(newHealth);
	    		}
	    	}
		}
    	return true;
    }

}
