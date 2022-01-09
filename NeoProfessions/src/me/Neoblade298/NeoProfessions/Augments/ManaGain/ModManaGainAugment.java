package me.Neoblade298.NeoProfessions.Augments.ManaGain;

import org.bukkit.entity.Player;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModManaGainAugment extends Augment{
	public ModManaGainAugment() {
		super();
	}
	
	public ModManaGainAugment(int level) {
		super(level);
	}
	
	public void applyEffects(PlayerData user, double mana) {
		// Empty unless overridden
	}
	
	public abstract double getFlatBonus(PlayerData user);
	public abstract double getMultiplierBonus(Player user);
	public abstract boolean canUse(PlayerData user, ManaSource src);
}
