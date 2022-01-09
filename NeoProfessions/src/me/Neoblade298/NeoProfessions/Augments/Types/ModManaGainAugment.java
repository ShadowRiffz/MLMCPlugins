package me.Neoblade298.NeoProfessions.Augments.Types;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModManaGainAugment extends Augment{
	protected static DecimalFormat df = new DecimalFormat("##.#");
	public ModManaGainAugment() {
		super();
	}
	
	public ModManaGainAugment(int level) {
		super(level);
	}
	
	public void applyEffects(PlayerData user, double mana) {
		// Empty unless overridden
	}
	
	public String formatMultiplierBonus(Player user, double bonus) {
		return df.format(getMultiplierBonus(user) * 100);
	}
	
	public abstract double getFlatBonus(PlayerData user);
	public abstract double getMultiplierBonus(Player user);
	public abstract boolean canUse(PlayerData user, ManaSource src);
}
