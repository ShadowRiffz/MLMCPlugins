package me.neoblade298.neomythicextension.mechanics;

import java.util.HashMap;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neobossinstances.Main;

public class ScaleHealthMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final String boss;
	protected HashMap<Integer, Double> scale;
	protected Main nbi;

	public ScaleHealthMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        this.nbi = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoBossInstances");
        
        this.boss = config.getString("boss", "Ratface");
        this.scale = new HashMap<Integer, Double>();
        this.scale.put(0, 1.0);
        this.scale.put(1, config.getDouble("1", 0.8));
        this.scale.put(2, config.getDouble("2", 1.2));
        this.scale.put(3, config.getDouble("3", 1.4));
        this.scale.put(4, config.getDouble("4", 1.55));
        this.scale.put(5, config.getDouble("5", 1.7));
        this.scale.put(6, config.getDouble("6", 1.8));
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		int numPlayers = this.nbi.getActiveFights().get(this.boss).size();

    	// Make sure target is a MythicMob
    	if (MythicMobs.inst().getAPIHelper().isMythicMob(target.getBukkitEntity())) {
    		ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(target.getBukkitEntity());
    		AbstractEntity ent = am.getEntity();
    		ent.setMaxHealth(ent.getMaxHealth() * this.scale.get(numPlayers));
    		ent.setHealth(ent.getMaxHealth());
    	}
    	return true;
    }

}
