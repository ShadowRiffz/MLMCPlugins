package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;

public class StartMinigameMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int cd;
	protected final int id;

	public StartMinigameMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);

        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.cd = config.getInteger(new String[] {"cooldown", "cd"}, 600);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			MinigameManager.startMinigame((Player) target, id, data.getCaster().getEntity().getUniqueId(), this.cd);
			return true;
		}
		return false;
    }
}
