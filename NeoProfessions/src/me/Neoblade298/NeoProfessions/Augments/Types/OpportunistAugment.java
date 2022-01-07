package me.Neoblade298.NeoProfessions.Augments.Types;

import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class OpportunistAugment extends ModDamageDealtAugment {
	
	public OpportunistAugment() {
		super();
		this.name = "Opportunist";
		this.etype = EventType.DAMAGE;
	}

	public OpportunistAugment(int level) {
		super(level);
		this.name = "Opportunist";
		this.etype = EventType.DAMAGE;
	}

	@Override
	public double getFlatBonus(LivingEntity user) {
		return 0;
	}

	@Override
	public double getMultiplierBonus(LivingEntity user) {
		return 0.01 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new OpportunistAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = target.getHealth() / target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage < 0.1;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + getMultiplierBonus(user) + "% §7when dealing");
		lore.add("§7damage while below 30% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
