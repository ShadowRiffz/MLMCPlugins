package me.Neoblade298.NeoProfessions.Augments.DamageTaken;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ManaGain.ModManaGainAugment;

public class MenacingAugment extends Augment implements ModDamageTakenAugment, ModManaGainAugment {
	public MenacingAugment() {
		super();
		this.name = "Menacing";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN, EventType.MANA_GAIN});
	}

	public MenacingAugment(int level) {
		super(level);
		this.name = "Menacing";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN, EventType.MANA_GAIN});
	}
	
	@Override
	public void applyDamageTakenEffects(Player user, LivingEntity target, double damage) {
		FlagManager.addFlag(user, user, "aug_menacing", 40);
	}

	@Override
	public double getManaGainMult(Player user) {
		return 0.02 * (level / 5);
	}
	
	@Override
	public double getDamageTakenMult(LivingEntity user) {
		return 0.005 * (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new MenacingAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		Location diff = user.getLocation().subtract(target.getLocation());
		double diffxy = (diff.getX() * diff.getX()) + (diff.getY() * diff.getY());
		return diffxy >= 144;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Decreases damage taken by §f" + formatPercentage(getDamageTakenMult(user)) + "%");
		lore.add("§7when within 3 blocks of an enemy.");
		lore.add("§7Taking damage from this distance");
		lore.add("§7increases resource regen by 10%");
		lore.add("§7for 2 seconds.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean canUse(PlayerData user, ManaSource src) {
		FlagManager.hasFlag(user, "aug_menacing");
	}

}
