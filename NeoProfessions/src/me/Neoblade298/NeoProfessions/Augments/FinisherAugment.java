package me.Neoblade298.NeoProfessions.Augments;

import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FinisherAugment extends ModDamageDealtAugment {
	
	public FinisherAugment() {
		super();
		this.name = "Finisher";
		this.etype = EventType.DAMAGE;
	}

	public FinisherAugment(int level) {
		super(level);
		this.name = "Finisher";
		this.etype = EventType.DAMAGE;
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

	public ItemStack getItem() {
		ItemStack item = super.getItem();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + getMultiplierBonus() + "% §7when dealing");
		lore.add("§7damage to an enemy below 10% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

}
