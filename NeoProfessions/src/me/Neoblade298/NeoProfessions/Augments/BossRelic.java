package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;

public class BossRelic extends Augment{
	private String display;
	
	public BossRelic(String name) {
		this.level = 5;
		this.name = name;
	}
	
	public BossRelic(int level, String name) {
		this.level = level;
		this.name = name;
	}

	@Override
	public String getLine() {
		return "§7[" + display + " Lv " + level + "]";
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = MythicMobs.inst().getItemManager().getItemStack(this.name);
		NBTItem nbti = new NBTItem(item);
		
		String[] relicStrings = item.getItemMeta().getLore().get(0).split(" ");
		this.display = "§b" + relicStrings[2];
		for (int i = 3; i < relicStrings.length; i++) {
			this.display += " " + relicStrings[i];
		}
		
		nbti.setInteger("level", level);
		nbti.setString("augment", name);
		return nbti.getItem();
	}
	
	public boolean isPermanent() {
		return false;
	}

	@Override
	public Augment createNew(int level) {
		return new BossRelic(this.level, this.name);
	}
}
