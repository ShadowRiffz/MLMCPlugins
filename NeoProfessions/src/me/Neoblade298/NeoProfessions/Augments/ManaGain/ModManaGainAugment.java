package me.Neoblade298.NeoProfessions.Augments.ManaGain;

import org.bukkit.entity.Player;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.player.PlayerData;

public interface ModManaGainAugment {
	
	public default void applyEffects(PlayerData user, double mana) {
		// Empty unless overridden
	}
	
	public default double getManaGainFlat(Player user) {
		return 0;
	}
	
	public default double getManaGainMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(PlayerData user, ManaSource src);
}
