package me.Neoblade298.NeoProfessions.Augments.Healing;

import org.bukkit.entity.Player;

import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModHealAugment extends Augment{
	public ModHealAugment() {
		super();
	}
	
	public ModHealAugment(int level) {
		super(level);
	}
	
	public void applyEffects(PlayerData user, double healing) {
		// Empty unless overridden
	}
	
	public abstract double getFlatBonus(PlayerData user);
	public abstract double getMultiplierBonus(Player user);
	public abstract boolean canUse(PlayerData user);
}
