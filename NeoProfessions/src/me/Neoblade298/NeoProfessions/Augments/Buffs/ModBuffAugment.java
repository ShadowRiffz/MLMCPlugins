package me.Neoblade298.NeoProfessions.Augments.Buffs;

import java.text.DecimalFormat;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModBuffAugment extends Augment{
	protected static DecimalFormat df = new DecimalFormat("##.#");
	public ModBuffAugment() {
		super();
	}
	
	public ModBuffAugment(int level) {
		super(level);
	}
	
	public void applyEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public String formatTimeMultiplier(LivingEntity user) {
		return df.format(getTimeMultiplier(user) * 100);
	}
	
	public String formatMultiplierBonus(LivingEntity user) {
		return df.format(getMultiplierBonus(user) * 100);
	}
	
	public abstract double getTimeMultiplier(LivingEntity user);
	public abstract double getFlatBonus(LivingEntity user);
	public abstract double getMultiplierBonus(LivingEntity user);
	
	public abstract boolean canUse(Player user, LivingEntity target);
}
