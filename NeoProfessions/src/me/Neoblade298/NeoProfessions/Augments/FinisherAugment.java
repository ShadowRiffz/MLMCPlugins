package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

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

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §c" + name + " Augment");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " " + name + " Augment");
		lore.add("§7Increases damage by §f" + getMultiplierBonus() + "% §7when dealing");
		lore.add("§7damage to an enemy below 10% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", level);
		nbti.setString("augment", name);
		return nbti.getItem();
	}

}
