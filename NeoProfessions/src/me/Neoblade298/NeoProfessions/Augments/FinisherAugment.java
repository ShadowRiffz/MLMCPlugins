package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FinisherAugment extends ModDamageDealtAugment {

	public FinisherAugment(int level) {
		this.level = level;
	}

	@Override
	public double getFlatBonus() {
		return 0;
	}

	@Override
	public double getMultiplierBonus() {
		return 0.02 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new FinisherAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = target.getHealth() / target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage > 0.95;
	}

}
