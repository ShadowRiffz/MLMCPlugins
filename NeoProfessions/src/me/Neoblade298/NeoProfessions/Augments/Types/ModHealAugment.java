package me.Neoblade298.NeoProfessions.Augments.Types;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModHealAugment extends Augment{
	protected static DecimalFormat df = new DecimalFormat("##.#");
	public ModHealAugment() {
		super();
	}
	
	public ModHealAugment(int level) {
		super(level);
	}
	
	public void applyEffects(PlayerData user, double healing) {
		// Empty unless overridden
	}
	
	public String formatMultiplierBonus(Player user, double bonus) {
		return df.format(getMultiplierBonus(user) * 100);
	}
	
	public abstract double getFlatBonus(PlayerData user);
	public abstract double getMultiplierBonus(Player user);
	public abstract boolean canUse(PlayerData user);
}
