package me.Neoblade298.NeoProfessions.Augments.Buffs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.event.SkillBuffEvent;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public abstract class ModBuffAugment extends Augment{
	public ModBuffAugment() {
		super();
	}
	
	public ModBuffAugment(int level) {
		super(level);
	}
	
	public void applyEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public abstract double getTimeMultiplier(LivingEntity user);
	public abstract double getFlatBonus(LivingEntity user);
	public abstract double getMultiplierBonus(LivingEntity user);
	
	public abstract boolean canUse(Player user, LivingEntity target, SkillBuffEvent e);
}
