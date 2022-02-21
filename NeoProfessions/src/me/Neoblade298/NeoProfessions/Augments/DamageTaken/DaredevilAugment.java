package me.Neoblade298.NeoProfessions.Augments.DamageTaken;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class DaredevilAugment extends Augment implements ModDamageTakenAugment {
	
	public DaredevilAugment() {
		super();
		this.name = "Daredevil";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN});
	}

	public DaredevilAugment(int level) {
		super(level);
		this.name = "Daredevil";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN});
	}

	@Override
	public double getDamageTakenMult(LivingEntity user) {
		return 0.01 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new DaredevilAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = (user.getPlayer().getHealth() / user.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		return percentage < 0.3;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Decreases damage taken by §f" + formatPercentage(getDamageTakenMult(user)) + "%");
		lore.add("§7when below 30% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
