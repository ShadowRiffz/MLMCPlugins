package me.Neoblade298.NeoProfessions.Augments.DamageDealt;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class IntimidatingAugment extends Augment implements ModDamageDealtAugment {
	private double threatMult;
	
	public IntimidatingAugment() {
		super();
		this.name = "Intimidating";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
		this.threatMult = 0.01 * (level / 5);
	}

	public IntimidatingAugment(int level) {
		super(level);
		this.name = "Intimidating";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
		this.threatMult = 0.01 * (level / 5);
	}

	@Override
	public void applyEffects(Player user, LivingEntity target, double damage) {
		MythicMobs.inst().getAPIHelper().addThreat(user, target, damage * threatMult);
	}

	@Override
	public Augment createNew(int level) {
		return new IntimidatingAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		return MythicMobs.inst().getAPIHelper().isMythicMob(target);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases threat from damaging an");
		lore.add("§7enemy by §f" + formatPercentage(this.threatMult) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
