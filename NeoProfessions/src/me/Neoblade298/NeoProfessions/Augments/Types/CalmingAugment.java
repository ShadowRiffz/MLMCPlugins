package me.Neoblade298.NeoProfessions.Augments.Types;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class CalmingAugment extends ModDamageDealtAugment {
	double threatReduction;
	
	public CalmingAugment() {
		super();
		this.name = "Calming";
		this.etype = EventType.DAMAGE;
		this.threatReduction = (this.level / 5) * 0.01;
	}

	public CalmingAugment(int level) {
		super(level);
		this.name = "Calming";
		this.etype = EventType.DAMAGE;
		this.threatReduction = (this.level / 5) * 0.01;
	}
	
	@Override
	public void applyEffects(Player user, LivingEntity target, double damage) {
		MythicMobs.inst().getAPIHelper().reduceThreat(user, target, damage * this.threatReduction);
	}

	@Override
	public double getFlatBonus(LivingEntity user) {
		return 0;
	}

	@Override
	public double getMultiplierBonus(LivingEntity user) {
		return 0;
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
		lore.add("§7Reduces threat generated from dealing damage by");
		lore.add("§f" + (int) (this.threatReduction * 100) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
