package me.Neoblade298.NeoProfessions.Augments.Types;

import java.text.DecimalFormat;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModDamageDealtAugment extends Augment{
	protected static DecimalFormat df = new DecimalFormat("##.#");
	public ModDamageDealtAugment() {
		super();
	}
	
	public ModDamageDealtAugment(int level) {
		super(level);
	}
	
	public void applyEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public boolean isPermanent() {
		return false;
	}
	
	public String formatMultiplierBonus(LivingEntity user, double bonus) {
		return df.format(getMultiplierBonus(user) * 100);
	}
	
	public abstract double getFlatBonus(LivingEntity user);
	public abstract double getMultiplierBonus(LivingEntity user);
	public abstract boolean canUse(Player user, LivingEntity target);
}
