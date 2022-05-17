package me.neoblade298.neomythicextension.mechanics;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;

public class GiveStoredItemMechanic implements ITargetedEntitySkill {

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	protected final int amount;
	protected final int id;
	protected final String mob;

	public GiveStoredItemMechanic(MythicLineConfig config) {
        this.mob = config.getString(new String[] {"mob", "m"}, null);
        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.amount = config.getInteger(new String[] {"amount", "a"}, 1);
        
        try {
            if (this.mob != null) {
                if (MythicBukkit.inst().getMobManager().getMythicMob(this.mob).isEmpty()) {
                	Bukkit.getLogger().log(Level.WARNING, "[NeoMythicExtension] Failed to load mob " + this.mob + " for GiveStoredItem " + this.id);
                }
                else {
                    StorageManager.addSource(this.id, this.mob, true);
                }
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				StorageManager.givePlayer((Player) target.getBukkitEntity(), this.id, this.amount);
				return SkillResult.SUCCESS;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SkillResult.INVALID_TARGET;
    }
}
