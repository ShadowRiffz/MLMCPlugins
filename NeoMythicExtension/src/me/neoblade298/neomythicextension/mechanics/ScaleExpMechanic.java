package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ScaleExpMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;

	public ScaleExpMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			Player p = (Player) target.getBukkitEntity();
			double scale = Math.min(2, 1 + (0.05 * (data.getCaster().getLevel() - 1)));
			double exp = Math.round(this.amount * scale);
			SkillAPI.getPlayerData(p).giveExp(exp, ExpSource.MOB);
			return true;
		}
		return false;
    }
}
