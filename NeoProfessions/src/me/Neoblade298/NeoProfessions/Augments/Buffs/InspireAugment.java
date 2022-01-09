package me.Neoblade298.NeoProfessions.Augments.Buffs;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.util.BuffType;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class InspireAugment extends ModBuffAugment {
	
	public InspireAugment() {
		super();
		this.name = "Inspire";
		this.etype = EventType.BUFF;
	}

	public InspireAugment(int level) {
		super(level);
		this.name = "Inspire";
		this.etype = EventType.BUFF;
	}

	@Override
	public double getTimeMultiplier(LivingEntity user) {
		return 0.02 * (level / 5);
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
		return new InspireAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target, SkillBuffEvent e) {
		return e.getType().equals(BuffType.SKILL_DAMAGE) || e.getType().equals(BuffType.DAMAGE);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases duration of damage");
		lore.add("§7buffs by §f" + formatPercentage(getTimeMultiplier(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
