package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neoresearch.Research;

public class ResearchKillsMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected final Research nr;
	protected final String alias;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ResearchKillsMechanic(MythicLineConfig config) {
        this.amount = config.getInteger("a");
        this.alias = config.getString("alias", "default");
        
        nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player && data.getCaster() instanceof ActiveMob) {
				if (data.getCaster().getLevel() <= 0) {
					return SkillResult.CONDITION_FAILED;
				}
				String mob = this.alias;
				if (this.alias.equals("default")) {
					ActiveMob amob = (ActiveMob) data.getCaster();
					mob = amob.getType().getInternalName();
				}
				Player p = (Player) target.getBukkitEntity();
				nr.giveResearchKills(p, this.amount, mob);
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
