package me.Neoblade298.NeoProfessions.Augments.Crits;

import com.sucy.skill.api.event.PlayerCriticalSuccessEvent;
import com.sucy.skill.api.player.PlayerData;

public interface ModCritSuccessAugment {
	
	public default void applyCritSuccessEffects(PlayerData user, double chance) {
		// Empty unless overridden
	}
	
	public abstract boolean canUse(PlayerData user, PlayerCriticalSuccessEvent e);
}
