package me.Neoblade298.NeoConsumables;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthRunnable extends BukkitRunnable {
	Player p;
	double healing;
	int reps;
	
	public HealthRunnable(Player p, double healing, int reps) {
		this.p = p;
		this.healing = healing;
		this.reps = reps;
	}
	
	@Override
	public void run() {
		if (!p.isDead()) {
			this.reps -= 1;
			p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + healing));
			if (this.reps <= 0) {
				this.cancel();
			}
		}
		else {
			this.cancel();
		}
	}
}
