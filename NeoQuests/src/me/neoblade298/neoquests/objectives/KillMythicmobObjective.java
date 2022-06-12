package me.neoblade298.neoquests.objectives;

import org.bukkit.Bukkit;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.neoblade298.neocore.io.LineConfig;

public class KillMythicmobObjective extends Objective {
	private String type;
	private String display;
	
	public KillMythicmobObjective() {
		super();
	}

	public KillMythicmobObjective(LineConfig cfg) {
		super(ObjectiveEvent.KILL_MYTHICMOB, cfg);

		type = cfg.getString("type", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new KillMythicmobObjective(cfg);
	}

	@Override
	public String getKey() {
		return "kill-mythicmob";
	}

	public boolean checkEvent(MythicMobDeathEvent e, ObjectiveInstance o) {
		if (e.getMobType().getInternalName().equals(type)) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		if (display == null) {
			try {
				display = MythicBukkit.inst().getMobManager().getMythicMob(type).get().getDisplayName().get();
			}
			catch (Exception e) {
				Bukkit.getLogger().warning("[NeoQuests] Failed to retrieve mob display for Mythic Mob " + type + ".");
				e.printStackTrace();
				display = "Error";
			}
		}
		return "Kill " + display;
	}

}
