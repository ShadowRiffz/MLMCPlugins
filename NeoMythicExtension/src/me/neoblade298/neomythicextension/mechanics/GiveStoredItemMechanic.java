package me.neoblade298.neomythicextension.mechanics;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;

public class GiveStoredItemMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected final int id;
	protected final String mob;

	public GiveStoredItemMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);

        this.mob = config.getString(new String[] {"mob", "m"}, "Ratface");
        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.amount = config.getInteger(new String[] {"amount", "a"}, 1);
        
        try {
            if (MythicMobs.inst().getMobManager().getMythicMob(this.mob) == null) {
            	Bukkit.getLogger().log(Level.WARNING, "[NeoMythicExtension] Failed to load mob " + this.mob + " for GiveStoredItem " + this.id);
            	return;
            }
            StorageManager.addSource(this.id, this.mob, true);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				StorageManager.givePlayer((Player) target.getBukkitEntity(), this.id, this.amount);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
}
