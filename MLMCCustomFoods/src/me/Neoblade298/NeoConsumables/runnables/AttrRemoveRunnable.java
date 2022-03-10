package me.Neoblade298.NeoConsumables.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.Neoblade298.NeoConsumables.objects.StoredAttributes;

public class AttrRemoveRunnable extends BukkitRunnable {
	Player p;
	StoredAttributes attrs;
	
	public AttrRemoveRunnable(Player p, StoredAttributes attrs) {
		this.p = p;
		this.attrs = attrs;
	}
	
	@Override
	public void run() {
		System.out.println("Attributes removed");
		attrs.removeAttributes(p);
	}
}
