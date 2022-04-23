package me.neoblade298.neomythicextension.triggers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sucy.skill.api.event.FlagApplyEvent;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.neoblade298.neomythicextension.Main;

public class StatusTrigger implements Listener {
	Main main;
	MythicBukkit mm;
	
	public StatusTrigger(Main main) {
		this.main = main;
		mm = (MythicBukkit) Bukkit.getPluginManager().getPlugin("MythicMobs");
	}
	
	@EventHandler
	public void onFlagApply(FlagApplyEvent e) {
		if (mm.getAPIHelper().isMythicMob(e.getEntity())) {
			io.lumine.mythic.core.mobs.ActiveMob mob = mm.getAPIHelper().getMythicMobInstance(e.getEntity());
			mob.signalMob(mob.getEntity(), e.getFlag());
		}
	}
}
