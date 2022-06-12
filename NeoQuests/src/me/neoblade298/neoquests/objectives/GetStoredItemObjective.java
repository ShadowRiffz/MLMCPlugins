package me.neoblade298.neoquests.objectives;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;

public class GetStoredItemObjective extends Objective {
	private int id;
	private String itemname;
	private boolean keep;
	
	public GetStoredItemObjective() {
		super();
	}

	public GetStoredItemObjective(LineConfig cfg) {
		super(ObjectiveEvent.RECEIVE_STORED_ITEM, cfg);
		this.keep = cfg.getBool("keep", false);
		
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
		new BukkitRunnable() {
			public void run() {
				oi.setCount(StorageManager.getAmount(oi.getPlayer(), id));
			}
		}.runTaskLater(NeoQuests.inst(), 20L);
	}

	@Override
	public void finalize(Player p) {
		if (!keep) {
			StorageManager.takePlayer(p, id, needed);
		}
	}
}
