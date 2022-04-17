package me.Neoblade298.NeoProfessions.Augments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.player.PlayerData;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class KillerInstinctAugment extends Augment implements ModCritCheckAugment {
	
	public KillerInstinctAugment() {
		super();
		this.name = "Killer Instinct";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
	}

	public KillerInstinctAugment(int level) {
		super(level);
		this.name = "Killer Instinct";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
	}
	
	@Override
	public double getCritChanceMult(Player user) {
		return 0.25;
	}

	@Override
	public Augment createNew(int level) {
		return new KillerInstinctAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalCheckEvent e) {
		return AugmentManager.getPlayerAugments(user.getPlayer()).getCount(this) >= 4;
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
		lore.add("§7If you have 4 of these");
		lore.add("§7equipped, increase crit");
		lore.add("§7chance by §f100%§7 when");
		lore.add("§7within §f4§7 blocks of an enemy.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
