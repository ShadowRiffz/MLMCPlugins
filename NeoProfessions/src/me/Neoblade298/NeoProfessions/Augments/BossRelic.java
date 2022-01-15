package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;

public class BossRelic extends Augment{
	
	public BossRelic(String name) {
		this.level = 5;
		this.name = name;
		this.etypes = new ArrayList<EventType>();
	}
	
	public BossRelic(int level, String name) {
		this.level = level;
		this.name = name;
		this.etypes = new ArrayList<EventType>();
	}

	@Override
	public String getLine() {
		ItemStack item = MythicMobs.inst().getItemManager().getItemStack(this.name);
		
		String[] relicStrings = item.getItemMeta().getLore().get(0).split(" ");
		String display = relicStrings[2];
		for (int i = 3; i < relicStrings.length; i++) {
			display += " " + relicStrings[i];
		}
		
		return "§7[" + display + " Lv " + level + "]";
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = MythicMobs.inst().getItemManager().getItemStack(this.name);
		NBTItem nbti = new NBTItem(item);
		
		nbti.setInteger("level", level);
		nbti.setString("augment", name);
		return nbti.getItem();
	}
	
	public boolean isPermanent() {
		return false;
	}

	@Override
	public Augment createNew(int level) {
		return new BossRelic(level, this.name);
	}
}
