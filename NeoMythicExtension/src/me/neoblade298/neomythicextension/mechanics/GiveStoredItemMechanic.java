package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;

public class GiveStoredItemMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected final int id;

	public GiveStoredItemMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);

        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.amount = config.getInteger(new String[] {"amount", "a"}, 1);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			StorageManager.givePlayer((Player) target, this.id, this.amount);
			return true;
		}
		return false;
    }
}
