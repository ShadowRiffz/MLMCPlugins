package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

public class ScaleExpMechanic implements ITargetedEntitySkill {

	protected final int amount;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ScaleExpMechanic(MythicLineConfig config) {
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				if (data.getCaster().getLevel() < 1) {
					return SkillResult.SUCCESS;
				}
				
				Player p = (Player) target.getBukkitEntity();
				double scale = Math.min(2, 1 + (0.1 * (data.getCaster().getLevel() - 1)));
				double exp = Math.round(this.amount * scale);
				SkillAPI.getPlayerData(p).giveExp(exp, ExpSource.MOB);
				p.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + exp + " §7exp!");
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
