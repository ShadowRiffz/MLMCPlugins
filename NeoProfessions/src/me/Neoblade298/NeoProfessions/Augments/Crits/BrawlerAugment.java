package me.Neoblade298.NeoProfessions.Augments.Crits;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.PlayerCriticalSuccessEvent;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.DamageTaken.ModDamageTakenAugment;

public class BrawlerAugment extends Augment implements ModCritSuccessAugment, ModDamageTakenAugment {
	
	public BrawlerAugment() {
		super();
		this.name = "Ferocious";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK, EventType.DAMAGE_TAKEN});
	}

	public BrawlerAugment(int level) {
		super(level);
		this.name = "Ferocious";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK, EventType.DAMAGE_TAKEN});
	}
	
	@Override
	public void applyCritSuccessEffects(PlayerData user, double chance) {
		FlagManager.addFlag(user.getPlayer(), user.getPlayer(), "aug_ferocious", 60);
	}

	@Override
	public Augment createNew(int level) {
		return new BrawlerAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalSuccessEvent e) {
		// crit
		return true;
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		// damage taken
		return FlagManager.hasFlag(user, "aug_ferocious");
	}
	
	@Override
	public double getDamageTakenMult(LivingEntity user) {
		return 0.006 * (level / 5);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Upon critical hit, decrease damage taken");
		lore.add("§7by §f" + formatPercentage(getDamageTakenMult(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
