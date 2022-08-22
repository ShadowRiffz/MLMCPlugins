package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.neoblade298.neorelics.Relic;

public class BossRelic extends Augment{
	private Relic relic;
	
	public BossRelic(Relic relic) {
		this.level = 5;
		this.name = "Relic of " + relic.getBossInfo().getDisplay();
		this.relic = relic;
		this.etypes = new ArrayList<EventType>();
	}
	
	public BossRelic(int level, String name) {
		this.level = level;
		this.name = name;
		this.etypes = new ArrayList<EventType>();
	}

	@Override
	public String getLine() {
		return "§7[§c" + name + " §7Lv " + level + "]";
	}
	
	@Override
	public ItemStack getItem(Player user) {
		return relic.getItem();
	}
	
	public boolean isPermanent() {
		return false;
	}

	@Override
	public Augment createNew(int level) {
		return new BossRelic(level, this.name);
	}
}
