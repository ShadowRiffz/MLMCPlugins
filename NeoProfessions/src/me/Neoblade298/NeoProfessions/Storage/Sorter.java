package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class Sorter {
	int priority;
	int sortType;
	boolean reverse;
	String settingString;
	
	public static final int NAME_SORT = 0;
	public static final int LEVEL_SORT = 1;
	public static final int RARITY_SORT = 2;
	public static final int AMOUNT_SORT = 3;
	
	public Sorter(int sortType, int startPriority, boolean reverse) {
		this.priority = startPriority;
		this.sortType = sortType;
		this.reverse = reverse;

		switch (sortType) {
		case NAME_SORT: settingString = "name";
		break;
		case LEVEL_SORT: settingString = "level";
		break;
		case RARITY_SORT: settingString = "rarity";
		break;
		default: settingString = "amount";
		break;
		}
	}
	
	public int compare(StoredItemInstance a, StoredItemInstance b) {
		int comp = 0;
		switch (sortType) {
		case NAME_SORT: comp = a.getItem().getName().compareTo(b.getItem().getName());
		break;
		case LEVEL_SORT: comp = a.getItem().getLevel() - b.getItem().getLevel();
		break;
		case RARITY_SORT: comp = a.getItem().getRarity().getPriority() - b.getItem().getRarity().getPriority();
		break;
		case AMOUNT_SORT: comp = a.getAmount() - b.getAmount();
		break;
		}
		if (reverse) {
			comp *= -1;
		}
		return comp;
	}
	
	public ItemStack createSortButton() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		switch (sortType) {
		case NAME_SORT: meta.setDisplayName("§9Sort by Name");
		break;
		case LEVEL_SORT: meta.setDisplayName("§9Sort by Level");
		break;
		case RARITY_SORT: meta.setDisplayName("§9Sort by Rarity");
		break;
		case AMOUNT_SORT: meta.setDisplayName("§9Sort by Amount");
		break;
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Priority: §e" + priority);
		lore.add("§7Order: §e" + (reverse ? "Ascending" : "Descending"));
		lore.add("§7§oPress 1-4 to set sort priority");
		lore.add("§7§oLeft click to set ascending/descending");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "sort");
		nbti.setInteger("priority", priority);
		return nbti.getItem();
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean flipOrder() {
		reverse = !reverse;
		return reverse;
	}
	
	public String getSettingString() {
		return settingString;
	}
}
