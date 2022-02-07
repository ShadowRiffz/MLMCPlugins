package me.neoblade298.neomythicextension.mechanics;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsChanceMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected int level;
	protected final double basechance;
	protected final Random rand;
	protected final Research nr;
	protected final String alias;

	public ResearchPointsChanceMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
		this.setAsyncSafe(false);
		this.setTargetsCreativePlayers(false);

		this.level = config.getInteger("l", 0);
		this.amount = config.getInteger("a");
		this.basechance = config.getDouble(new String[] { "basechance", "bc" }, 1);
		this.rand = new Random();
		this.alias = config.getString("alias", "default");

		nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player && data.getCaster() instanceof ActiveMob) {
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
			MythicResearchPointsChanceEvent e = new MythicResearchPointsChanceEvent(p, chance, dropType);
			Bukkit.getPluginManager().callEvent(e);
			chance = e.getChance();
			dropType = e.getDropType();

			// Check for successful drop
			if (rand <= chance) {
				if (dropType == 1 && rand >= this.basechance) {
					nr.giveResearchPoints(p, this.amount, mob, level, true, "Research Augment");
				}
				else {
					nr.giveResearchPoints(p, this.amount, mob, level, true, null);
				}
			}
			return true;
		}
		return false;
	}
}
