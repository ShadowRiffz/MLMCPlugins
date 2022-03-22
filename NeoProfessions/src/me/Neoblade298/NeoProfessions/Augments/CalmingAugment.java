package me.Neoblade298.NeoProfessions.Augments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class CalmingAugment extends Augment implements ModDamageDealtAugment {
	double threatReduction;
	
	public CalmingAugment() {
		super();
		this.name = "Calming";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
		this.threatReduction = (this.level / 5) * 0.02;
	}

	public CalmingAugment(int level) {
		super(level);
		this.name = "Calming";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
		this.threatReduction = (this.level / 5) * 0.02;
	}
	
	@Override
	public void applyDamageDealtEffects(Player user, LivingEntity target, double damage) {
		MythicMobs.inst().getAPIHelper().reduceThreat(target, user, damage * this.threatReduction);
	}

	@Override
	public Augment createNew(int level) {
		return new CalmingAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		return true;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Reduces threat generated from dealing");
		lore.add("§7damage by §f" + formatPercentage(this.threatReduction) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
