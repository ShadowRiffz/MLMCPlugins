package me.neoblade298.neomythicextension.triggers;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sucy.skill.api.event.FlagApplyEvent;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import me.neoblade298.neomythicextension.MythicExt;

public class StatusTrigger implements Listener {
	MythicExt main;
	MobExecutor mm;
	
	public StatusTrigger(MythicExt main) {
		this.main = main;
		mm = MythicBukkit.inst().getMobManager();
	}
	
	@EventHandler
	public void onFlagApply(FlagApplyEvent e) {
		try {
			Optional<ActiveMob> mob = mm.getActiveMob(e.getEntity().getUniqueId());
			if (mob.isPresent()) {
				mob.get().signalMob(mob.get().getEntity(), e.getFlag());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
