package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public abstract class Augment {
	protected String name;
	protected int level;
	
	public Augment() {
		this.level = 5;
	}
	
	public Augment(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLine() {
		return "§7[" + name + " Lv " + level + "]";
	}
	
	public abstract Augment createNew(int level);
	
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §c" + name + " Augment");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " " + name + " Augment");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", level);
		nbti.setString("augment", name);
		return nbti.getItem();
	}

}
