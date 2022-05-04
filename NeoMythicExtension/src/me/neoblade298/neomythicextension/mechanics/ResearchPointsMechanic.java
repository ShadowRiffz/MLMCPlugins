package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected int level;
	protected final String alias;
	protected final Research nr;
	protected String display;

	public ResearchPointsMechanic(MythicLineConfig config) {
        this.level = config.getInteger("l", 0);
        this.amount = config.getInteger("a");
        this.alias = config.getString("alias", "default");
        this.display = config.getString("d", "default").replaceAll("_", " ").replaceAll("&", "§").replaceAll("@", "&");
        
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
				Player p = (Player) target.getBukkitEntity();
				if (this.alias.equals("default")) {
					ActiveMob amob = (ActiveMob) data.getCaster();
					mob = amob.getType().getInternalName();
					level = (int) amob.getLevel();
					nr.giveResearchPoints(p, this.amount, mob, level, false, null);
				}
				else {
					MythicMob mm = MythicBukkit.inst().getMobManager().getMythicMob(mob).get();
					if (mm != null) {
						display = mm.getDisplayName().get();
					}
					nr.giveResearchPointsAlias(p, this.amount, mob, level, display, false);
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
