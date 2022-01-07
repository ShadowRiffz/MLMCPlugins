package me.Neoblade298.NeoProfessions.Augments.Types;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ManaCost;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class OverloadAugment extends ModDamageDealtAugment {
	double manaTaken;
	
	public OverloadAugment() {
		super();
		this.name = "Overload";
		this.etype = EventType.DAMAGE;
		this.manaTaken = (this.level / 5) * 0.5;
	}

	public OverloadAugment(int level) {
		super(level);
		this.name = "Overload";
		this.etype = EventType.DAMAGE;
		this.manaTaken = (this.level / 5) * 0.5;
	}
	
	@Override
	public void applyEffects(Player user, LivingEntity target, double damage) {
		SkillAPI.getPlayerData(user).useMana(this.manaTaken, ManaCost.SPECIAL);
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
		return new OverloadAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		PlayerData pdata = SkillAPI.getPlayerData(user);
		return (pdata.getMana() / pdata.getMaxMana()) > 0.1 && pdata.getClass("class").getData().getManaName().endsWith("MP");
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + getMultiplierBonus(user) + "% §7when dealing");
		lore.add("§7damage while above 10% mana. Costs");
		lore.add("§f" + this.manaTaken + " §7mana per damage instance.");
		lore.add("§cOnly works with mana.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
