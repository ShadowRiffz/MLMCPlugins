package me.neoblade298.neoquests.objectives;

import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;

public class GetStoredItemObjective extends Objective {
	private int id;
	private String itemname;
	
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

	@Override
	public String getDisplay() {
		if (itemname == null) {
			itemname = StorageManager.getItem(id).getDisplay();
		}
		return "Get " + itemname;
	}
	
	@Override
	public void initialize(ObjectiveInstance oi) {
		oi.setCount(StorageManager.getAmount(oi.getPlayer(), id));
	}

}
