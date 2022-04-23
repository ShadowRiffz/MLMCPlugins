package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;

public class StartMinigameMechanic implements ITargetedEntitySkill {

	protected final int cd;
	protected final int id;

	public StartMinigameMechanic(MythicLineConfig config) {
        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.cd = config.getInteger(new String[] {"mgcd", "minigamecd"}, 600);
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				MinigameManager.startMinigame((Player) target.getBukkitEntity(), id, data.getCaster().getEntity().getUniqueId(), this.cd);
				return SkillResult.SUCCESS;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SkillResult.INVALID_TARGET;
    }
}
