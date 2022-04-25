package me.neoblade298.neomythicextension.triggers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sucy.skill.api.event.FlagApplyEvent;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import me.neoblade298.neomythicextension.Main;

public class StatusTrigger implements Listener {
	Main main;
	MobExecutor mm;
	
	public StatusTrigger(Main main) {
		this.main = main;
		mm = MythicBukkit.inst().getMobManager();
	}
	
	@EventHandler
	public void onFlagApply(FlagApplyEvent e) {
		try {
			ActiveMob mob = mm.getActiveMob(e.getEntity().getUniqueId()).get();
			mob.signalMob(mob.getEntity(), e.getFlag());
		}
		catch (Exception ex) {
			
		}
	}
}
