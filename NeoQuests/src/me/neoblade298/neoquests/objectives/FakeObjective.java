package me.neoblade298.neoquests.objectives;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;

public class FakeObjective extends Objective {
	private String line;
	
	public FakeObjective() {
		super();
	}

	public FakeObjective(LineConfig cfg) {
		super(null, cfg);
		line = cfg.getLine();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new FakeObjective(cfg);
	}

	@Override
	public String getKey() {
		return "fake";
	}

	@Override
	public String getDisplay() {
		return line;
	}
	
	@Override
	public void initialize(ObjectiveInstance oi) {
		new BukkitRunnable() {
			public void run() {
				// This objective counts as completed by default
				oi.setCount(1);
			}
		}.runTaskLater(NeoQuests.inst(), 20L);
	}
}
