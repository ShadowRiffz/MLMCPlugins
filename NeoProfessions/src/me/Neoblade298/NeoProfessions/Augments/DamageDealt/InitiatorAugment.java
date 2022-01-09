package me.Neoblade298.NeoProfessions.Augments.DamageDealt;

import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class InitiatorAugment extends ModDamageDealtAugment {
	
	public InitiatorAugment() {
		super();
		this.name = "Initiator";
		this.etype = EventType.DAMAGE;
	}

	public InitiatorAugment(int level) {
		super(level);
		this.name = "Initiator";
		this.etype = EventType.DAMAGE;
	}

	@Override
	public double getFlatBonus(LivingEntity user) {
		return 0;
	}

	@Override
	public double getMultiplierBonus(LivingEntity user) {
		return 0.05 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new InitiatorAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = target.getHealth() / target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage > 0.95;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + formatMultiplierBonus(user, getMultiplierBonus(user)) + "% §7when dealing");
		lore.add("§7damage to an enemy above 95% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
