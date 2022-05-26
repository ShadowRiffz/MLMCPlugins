package me.neoblade298.neoquests.objectives;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;
import net.citizensnpcs.api.CitizensAPI;

public class GetStoredItemObjective extends Objective {
	private int id;
	
	public GetStoredItemObjective() {
		super();
	}

	public GetStoredItemObjective(LineConfig cfg) {
		super(ObjectiveEvent.RECEIVE_STORED_ITEM, cfg);
		this.needed = cfg.getInt("amount", 1);
		
		id = cfg.getInt("id", 0);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new GetStoredItemObjective(cfg);
	}

	@Override
	public String getKey() {
		return "get-storeditem";
	}

	public boolean checkEvent(ReceiveStoredItemEvent e, ObjectiveInstance o) {
		if (id == e.getId()) {
			o.setCount(StorageManager.getAmount(e.getPlayer(), id));
			return true;
		}
		return false;
	}

}
