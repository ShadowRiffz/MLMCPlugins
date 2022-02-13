package me.Neoblade298.NeoProfessions.Utilities;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CulinarianUtils {

	static final int CRAFT_COST = 1;
	static final int NUM_PER_CRAFT = 3;
	static Random gen = new Random();
	Util util;

	public CulinarianUtils() {
		util = new Util();
	}

	public void checkAlcoholUp(Player p, int amount, HashMap<Player, Integer> drunkness) {
		if (amount >= 30 && amount < 50) {
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30 * 20, 0));
			Util.sendMessage(p, "&7You are now &esomewhat drunk&7 (" + amount + "/100).");
		}
		else if (amount >= 50 && amount < 70) {
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60 * 20, 0));
			Util.sendMessage(p, "&7You are now &6very drunk&7 (" + amount + "/100).");
		}
		else if (amount >= 70 && amount < 100) {
			p.removePotionEffect(PotionEffectType.WITHER);
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60 * 20, 0));
			Util.sendMessage(p, "&7You are now &4dangerously drunk&7. (" + amount + "/100).");
		}
		else if (amount > 100) {
			double chance = gen.nextDouble();
			if (chance >= 0.5) {
				Util.sendMessage(p,
						"&7You throw up from being too drunk. Your drunkness is reduced to &6very drunk&7 (50/100).");
				drunkness.put(p, 50);
			}
			else {
				Util.sendMessage(p, "&7You become too drunk and lose 80% of your current health.");
				p.damage(1);
				p.setHealth(p.getHealth() * 0.2);
				drunkness.put(p, 100);
			}
		}
		else {
			Util.sendMessage(p, "&7You drink&7 (" + amount + "/100).");
		}
	}

	public void checkAlcoholDown(Player p, int amount) {
		if (amount == 0) {
			Util.sendMessage(p, "&7You are completely sober&7 (0/100).");
		}
		else if (amount == 29) {
			Util.sendMessage(p, "&7Your drunkness is reduced to &fsober&7 (29/100).");
		}
		else if (amount == 49) {
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0));
			Util.sendMessage(p, "&7Your drunkness is reduced to &esomewhat drunk&7 (49/100).");
		}
		else if (amount == 69) {
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 0));
			Util.sendMessage(p, "&7Your drunkness is reduced to &6very drunk&7 (69/100).");
		}
	}
}
