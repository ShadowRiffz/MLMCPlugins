package me.neoblade298.neosapiaddons;

import java.util.HashMap;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class ShieldManager {
	private static HashMap<Player, PlayerShields> shields = new HashMap<Player, PlayerShields>();
	
	public static void addShields(Player p, Shield shield) {
		PlayerShields pshields = shields.getOrDefault(p, new PlayerShields(p));
		pshields.addShield(shield);
		shields.put(p, pshields);
	}
	
	public static double useShields(Player p, double damage) {
		if (shields.containsKey(p)) {
			damage = shields.get(p).useShields(damage);
		}
		return damage;
	}
	
	public static void updatePlayerShields(Player p) {
		if (shields.containsKey(p)) {
			double pct = 0;
			double max = shields.get(p).getMax();
			double amount = shields.get(p).getAmount();
			if (max > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				pct = amount / max;
			}
			else {
				pct = amount / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			pct = Math.max(0, pct);
			double absorb = Math.round(pct * 20);
			p.setAbsorptionAmount(absorb);
		}
	}
	
	public static PlayerShields getPlayerShields(Player p) {
		if (!shields.containsKey(p)) {
			shields.put(p, new PlayerShields(p));
		}
		return shields.get(p);
	}
}
