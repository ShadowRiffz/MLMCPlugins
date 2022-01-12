package me.Neoblade298.NeoProfessions.Augments.Crits;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class SpellweavingAugment extends Augment implements ModCritCheckAugment {
	private double manaGain;
	
	public SpellweavingAugment() {
		super();
		this.name = "Spellweaving";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
		this.manaGain = 1.5 * (level / 5);
	}

	public SpellweavingAugment(int level) {
		super(level);
		this.name = "Spellweaving";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
		this.manaGain = 1.5 * (level / 5);
	}
	
	@Override
	public void applyCritEffects(PlayerData user, double chance) {
		user.giveMana(this.manaGain, ManaSource.SPECIAL);
	}

	@Override
	public Augment createNew(int level) {
		return new SpellweavingAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalCheckEvent e) {
		return user.getClass("class").getData().getManaName().endsWith("MP");
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Upon critical hit, gain §f" + this.manaGain + " §7mana.");
		lore.add("§7Only works with mana.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
