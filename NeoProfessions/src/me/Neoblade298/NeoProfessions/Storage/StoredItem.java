package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.Objects.SkullCreator;

public class StoredItem {
	private String mat;
	private String display;
	private String name;
	private int id;
	private int level;
	private Rarity rarity;
	private ArrayList<String> baseLore;
	private ArrayList<String> storageLore;
	private ArrayList<String> sources;

	public StoredItem(int id, String name, int level, String rarity, String mat, ArrayList<String> lore) {
		this.id = id;
		switch (rarity) {
		case "uncommon":
			this.rarity = Rarity.UNCOMMON;
			break;
		case "rare":
			this.rarity = Rarity.RARE;
			break;
		case "epic":
			this.rarity = Rarity.EPIC;
			break;
		case "legendary":
			this.rarity = Rarity.LEGENDARY;
			break;
		default:
			this.rarity = Rarity.COMMON;
			break;
		}
		this.baseLore = new ArrayList<String>();
		this.name = name;
		this.display = "§6[Lv " + level + "] " + this.rarity.getCode() + name;
		this.baseLore.add("§7Rarity: " + this.rarity.getDisplay());
		if (lore != null) {
			for (String line : lore) {
				this.baseLore.add("§7§o" + line);
			}
		}
		this.mat = mat;
		this.storageLore = new ArrayList<String>(baseLore);
		this.storageLore.add("§7Sources:");
		this.storageLore.add("§7§oLeft click to view recipes containing this");
		this.storageLore.add("§7§oRight click to create a voucher for this");
		this.storageLore.add("§7§oShift right click for 10x vouchers");
		// Sources added as more things are loaded
		this.sources = new ArrayList<String>();
	}
	
	public int getId() {
		return this.id;
	}

	public String getDisplay() {
		return display;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<String> getBaseLore() {
		return this.baseLore;
	}

	public Rarity getRarity() {
		return this.rarity;
	}

	public int getLevel() {
		return this.level;
	}
	
	public void addSource(String source, boolean isMob) {
		if (isMob) {
			MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(source);
			if (mm != null) {
				source = mm.getDisplayName().get();
			}
		}
		this.storageLore.add(storageLore.size() - 3, "§7- " + source);
		sources.add(source);
	}
	
	public ArrayList<String> getSources() {
		return sources;
	}
	
	public ItemStack getItem() {
		ItemStack item;
		if (mat.length() > 40) {
			item = SkullCreator.itemFromBase64(mat);
		}
		else {
			item = new ItemStack(Material.getMaterial(mat));
		}
		
		return item;
	}
	
	public ItemStack getStorageView(Player p, int amount) {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(storageLore);
		if (amount <= 0) {
			return null;
		}
		meta.setDisplayName(display + " §fx" + amount);
		item.setItemMeta(meta);
		return item;
	}
}
