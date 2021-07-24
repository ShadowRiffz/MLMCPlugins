package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neoresearch.Research;

public class ResearchKillsMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected final Research nr;
	protected final String alias;

	public ResearchKillsMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger("a");
        this.alias = config.getString("alias", "default");
        
        nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player && data.getCaster() instanceof ActiveMob) {
			String mob = this.alias;
			if (this.alias.equals("default")) {
				ActiveMob amob = (ActiveMob) data.getCaster();
				mob = amob.getType().getInternalName();
			}
			Player p = (Player) target.getBukkitEntity();
			nr.giveResearchKills(p, this.amount, mob);
			return true;
		}
		return false;
    }
}
