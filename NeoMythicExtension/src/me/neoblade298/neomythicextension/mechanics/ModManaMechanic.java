package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;

public class ModManaMechanic implements ITargetedEntitySkill {

	protected final int amount;

	public ModManaMechanic(MythicLineConfig config) {
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			Player p = (Player) target.getBukkitEntity();
			SkillAPI.getPlayerData(p).giveMana(amount);
			return SkillResult.SUCCESS;
		}
		return SkillResult.INVALID_TARGET;
    }
}
