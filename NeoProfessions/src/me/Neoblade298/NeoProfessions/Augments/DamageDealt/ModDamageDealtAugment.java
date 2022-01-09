package me.Neoblade298.NeoProfessions.Augments.DamageDealt;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface ModDamageDealtAugment {
	
	public default void applyEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public default double getDamageDealtFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getDamageDealtMult(LivingEntity user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, LivingEntity target);
}
