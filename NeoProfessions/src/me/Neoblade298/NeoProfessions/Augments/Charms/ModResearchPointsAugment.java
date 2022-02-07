package me.Neoblade298.NeoProfessions.Augments.Charms;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerExperienceGainEvent;

import me.neoblade298.neomythicextension.events.ChestDropEvent;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;

public interface ModResearchPointsAugment {
	
	public default void applyExpEffects(Player user) {
		
	}
	
	public default double getRPChanceFlat(Player user) {
		return 0;
	}
	
	public default double getRPChanceMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, MythicResearchPointsChanceEvent e);
}
