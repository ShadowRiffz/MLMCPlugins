package me.neoblade298.neoquests.objectives;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.neoblade298.neocore.io.LineConfig;
import net.citizensnpcs.api.CitizensAPI;

public class InteractNpcObjective extends Objective {
	private int npcid;
	
	public InteractNpcObjective() {
		super();
	}

	public InteractNpcObjective(LineConfig cfg) {
		super(ObjectiveEvent.INTERACT_NPC, cfg);
		this.needed = 1;
		
		npcid = cfg.getInt("id", -1);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new InteractNpcObjective(cfg);
	}

	@Override
	public String getKey() {
		return "interact-npc";
	}

	public boolean checkEvent(PlayerInteractEntityEvent e) {
		int id = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked()).getId();
		if (id == npcid && this.needed > this.count) {
			this.count++;
			return true;
		}
		return false;
	}

}
