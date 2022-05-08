package me.neoblade298.neomythicextension.mechanics;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsChanceMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected int level;
	protected final double basechance;
	protected final Random rand;
	protected final Research nr;
	protected final String alias;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ResearchPointsChanceMechanic(MythicLineConfig config) {
		this.level = config.getInteger("l", 0);
		this.amount = config.getInteger("a");
		this.basechance = config.getDouble(new String[] { "basechance", "bc" }, 1);
		this.rand = new Random();
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
				double rand = this.rand.nextDouble();
				double chance = this.basechance;
				String mob = this.alias;
				if (this.alias.equals("default")) {
					ActiveMob amob = (ActiveMob) data.getCaster();
					mob = amob.getType().getInternalName();
					level = (int) amob.getLevel();
				}
	
				// Check if player is holding a drop charm
				Player p = (Player) target.getBukkitEntity();
				int dropType = 0;
				ResearchPointsChanceMechanic cfg = this;
				MythicResearchPointsChanceEvent e = new MythicResearchPointsChanceEvent(p, chance, dropType);
				Bukkit.getPluginManager().callEvent(e);
				double moddedChance = e.getChance();
				int moddedDrop = e.getDropType();
	
				// Check for successful drop
				if (rand <= moddedChance) {
					if (moddedDrop == 1 && rand >= cfg.basechance) {
						nr.giveResearchPoints(p, cfg.amount, mob, level, true, "Research Augment");
					}
					else {
						nr.giveResearchPoints(p, cfg.amount, mob, level, true, null);
					}
				}
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
	}
}
