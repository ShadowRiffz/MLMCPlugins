package me.Neoblade298.NeoProfessions.Augments.DamageTaken;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Objects.FlagSettings;

public interface ModDamageTakenAugment {
	public default void applyDamageTakenEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public default double getDamageTakenFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getDamageReturnedFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getDamageTakenMult(LivingEntity user) {
		return 0;
	}
	
	public default FlagSettings setFlagAfter() {
		return null;
	}
	
	public abstract boolean canUse(Player user, LivingEntity target);
}
