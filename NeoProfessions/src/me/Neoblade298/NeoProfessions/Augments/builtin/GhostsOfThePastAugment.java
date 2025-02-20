package me.Neoblade298.NeoProfessions.Augments.builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.skills.Skill;
import com.sucy.skill.api.skills.SkillShot;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModDamageDealtAugment;

public class GhostsOfThePastAugment extends Augment implements ModDamageDealtAugment {
	
	public GhostsOfThePastAugment() {
		super();
		this.name = "Ghosts of the Past";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	public GhostsOfThePastAugment(int level) {
		super(level);
		this.name = "Ghosts of the Past";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	@Override
	public double getDamageDealtMult(LivingEntity user) {
		return 0.2;
	}

	@Override
	public Augment createNew(int level) {
		return new GhostsOfThePastAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		return true;
	}
	
	@Override
	public void applyDamageDealtEffects(Player user, LivingEntity target, double damage) {
		Skill skill = SkillAPI.getSkill("aug_ghostsOfThePast");
        ((SkillShot) skill).cast(user, (int) (damage * 0.01));
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public String getLine() {
		return "§7[§4§o" + name + " Lv " + level + "§7]";
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + formatPercentage(getDamageDealtMult(user)) + "% §7but deal");
		lore.add("§71% of pre-buff damage to yourself");
		lore.add("§7as corruption damage.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
