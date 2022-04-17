package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected int level;
	protected final String alias;
	protected final Research nr;
	protected String display;

	public ResearchPointsMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);

        this.level = config.getInteger("l", 0);
        this.amount = config.getInteger("a");
        this.alias = config.getString("alias", "default");
        this.display = config.getString("d", "default").replaceAll("_", " ").replaceAll("&", "§").replaceAll("@", "&");
        
        nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player && data.getCaster() instanceof ActiveMob) {
			if (data.getCaster().getLevel() <= 0) {
				return true;
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
				MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(mob);
				if (mm != null) {
					display = mm.getDisplayName().get();
				}
				nr.giveResearchPointsAlias(p, this.amount, mob, level, display, false);
			}
			return true;
		}
		return false;
    }
}
