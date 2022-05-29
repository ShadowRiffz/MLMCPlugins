package me.neoblade298.neoquests.objectives;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.neoblade298.neocore.io.LineConfig;
import net.citizensnpcs.api.CitizensAPI;

public class InteractNpcObjective extends Objective {
	private int npcid;
	private String npcname;
	
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

	public boolean checkEvent(PlayerInteractEntityEvent e, ObjectiveInstance o) {
		int id = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked()).getId();
		if (id == npcid && o.getCount() == 0) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		if (npcname == null) {
			npcname = CitizensAPI.getNPCRegistry().getById(npcid).getFullName();
		}
		return "Talk to NPC " + npcname;
	}

}