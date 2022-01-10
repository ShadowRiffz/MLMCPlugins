package me.Neoblade298.NeoProfessions.Augments.Healing;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;

public class RejuvenatingAugment extends Augment implements ModHealAugment {
	private double manaGain;
	public RejuvenatingAugment() {
		super();
		this.name = "Rejuvenating";
		this.etypes = Arrays.asList(new EventType[] {EventType.HEAL});
		this.manaGain = (level / 5);
	}

	public RejuvenatingAugment(int level) {
		super(level);
		this.name = "Rejuvenating";
		this.etypes = Arrays.asList(new EventType[] {EventType.HEAL});
		this.manaGain = (level / 5);
	}

	@Override
	public Augment createNew(int level) {
		return new RejuvenatingAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, LivingEntity target) {
		if (target instanceof Player) {
			Player p = (Player) target;
			PlayerData data = SkillAPI.getPlayerData(p);
			return data.getClass("class").getData().getManaName().endsWith("MP");
		}
		return false;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Healing also grants §f" + this.manaGain + " §7mana.");
		lore.add("§7Only works on mana users.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
