package me.neoblade298.neomythicextension.drops;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IIntangibleDrop;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neobossinstances.BossInstances;

public class StoredItemDrop implements IIntangibleDrop {

	protected final int id;
	protected final String mob;
	protected final String boss;

	public StoredItemDrop(MythicLineConfig config) {
        this.mob = config.getString(new String[] {"mob", "m"});
        this.id = config.getInteger(new String[] {"id", "i"}, 0);
        this.boss = config.getString(new String[] {"boss", "b"});
        
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
	public void giveDrop(AbstractPlayer p, DropMetadata meta, double amount) {
		if (this.boss != null) {
			for (Player fighter : BossInstances.inst().getActiveFights().get(this.boss)) {
				StorageManager.givePlayer(fighter, this.id, (int) amount);
			}
		}
		else {
			StorageManager.givePlayer((Player) p.getBukkitEntity(), this.id, (int) amount);
		}
	}
}
