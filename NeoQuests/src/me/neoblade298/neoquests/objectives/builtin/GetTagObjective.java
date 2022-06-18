package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class GetTagObjective extends Objective {
	private int id;
	private String itemname;
	private boolean keep;
	
	public GetTagObjective() {
		super();
	}

	public GetTagObjective(LineConfig cfg) {
		super(ObjectiveEvent.RECEIVE_STORED_ITEM, cfg);
		this.keep = cfg.getBool("keep", false);
		
		id = cfg.getInt("id", 0);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new GetTagObjective(cfg);
	}

	@Override
	public String getKey() {
		return "get-tag";
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

	@Override
	public void finalize(Player p) {
		if (!keep) {
			StorageManager.takePlayer(p, id, needed);
		}
	}
}
