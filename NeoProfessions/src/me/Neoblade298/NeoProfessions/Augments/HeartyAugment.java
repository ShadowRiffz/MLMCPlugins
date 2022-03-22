package me.Neoblade298.NeoProfessions.Augments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeartyAugment extends Augment implements ModDamageDealtAugment {
	
	public HeartyAugment() {
		super();
		this.name = "Hearty";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	public HeartyAugment(int level) {
		super(level);
		this.name = "Hearty";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	@Override
	public double getDamageDealtMult(LivingEntity user) {
		return 0.02 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new HeartyAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = user.getHealth() / user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage > 0.95;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + formatPercentage(getDamageDealtMult(user)) + "% §7when dealing");
		lore.add("§7damage while above 95% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
