package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AugmentListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) e.getDamager();
			double multiplier = 1;
			double flat = 0;
			if (AugmentManager.playerAugments.containsKey(p)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments()) {
					if (augment instanceof ModDamageDealtAugment) {
						ModDamageDealtAugment aug = (ModDamageDealtAugment) augment;
						if (aug.canUse(p, (LivingEntity) e.getEntity())) {
							multiplier += aug.getMultiplierBonus();
							flat += aug.getFlatBonus();
						}
					}
				}
			}
			e.setDamage(e.getDamage() * multiplier + flat);
		}
	}
}
