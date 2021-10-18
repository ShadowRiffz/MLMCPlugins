package me.Neoblade298.NeoConsumables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AttributeRunnable extends BukkitRunnable {
	Attributes attr;
	Player p;
	public AttributeRunnable(Player p, Attributes attr) {
		this.p = p;
		this.attr = attr;
	}

	@Override
	public void run() {
		if (p != null) {
			attr.removeAttributes(p);
		}
	}
}
