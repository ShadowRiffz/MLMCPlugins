package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class StorageView extends ProfessionInventory {
	private Inventory inv;
	private Player p;
	private ArrayList<StoredItem> items;
	private int page = 1;
	private String sort;
	private boolean reverse;
	
	public StorageView(Player p, int min, int max, Inventory inv) {
		this.p = p;
		this.inv = inv;
		
		
		// Setup itemstacks to be used for sorting
		int arrSize = 0;
		int newMax = 0;
		items = new ArrayList<StoredItem>();
		for (int i = min; i <= max; i++) {
			if (!StorageManager.getItemDefinitions().containsKey(i)) {
				break;
			}
			if (StorageManager.getAmount(p, i) > 0) {
				items.add(StorageManager.getItemDefinitions().get(i));
			}
			newMax++;
		}
		
		setupInventory();
	}
	
	// Sort by rarity
	// Sort alphabetically
	// Sort by level
	private void setupInventory() {
		// Sort items on construction and on change sort type
		
		ItemStack[] contents = inv.getContents();
		for (int i = (page - 1) * 45; i < 45 * page; i++) {
			contents[i] = items.get(i).getStorageView(p);
		}
		
		contents[49] = createInfoItem();
		if (page > 1) {
			contents[48] = createPreviousButton();
		}
		if (items.size() > page * 45) {
			contents[50] = createNextButton();
		}
	}
	
	private ItemStack createInfoItem() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Info");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oLeft click to view recipes");
		lore.add("§7§oRight click to create voucher");
		lore.add("§7§oShift right click to create 10x vouchers");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "info");
		return nbti.getItem();
	}
	
	private ItemStack createPreviousButton() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Previous Page");
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "previous");
		return nbti.getItem();
	}
	
	private ItemStack createNextButton() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Next Page");
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "next");
		return nbti.getItem();
	}
	
	private ItemStack createSortButton() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sorting");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oLeft click: By name");
		lore.add("§7§oRight click: By level, then rarity");
		lore.add("§7§oShift left click: By rarity, then level");
		lore.add("§7§oShift Right click: By amount, then level");
		lore.add("§7§oSame click twice: Reverse order");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "sort");
		return nbti.getItem();
	}

	@Override
	public void handleInventoryClick(InventoryClickEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void sortItems() {
		
	}

	 
	// Helper class implementing Comparator interface
	private class Sorter implements Comparator<StoredItem> {
		HashMap<String, Integer> rarityPriorities = new HashMap<String, Integer>();
		
		public Sorter() {
			rarityPriorities.put("common", 1);
			rarityPriorities.put("uncommon", 2);
			rarityPriorities.put("rare", 3);
			rarityPriorities.put("epic", 4);
			rarityPriorities.put("legendary", 5);
		}
	 
	    // Method
	    // Sorting in ascending order of roll number
	    public int compare(StoredItem a, StoredItem b)
	    {
	    	int comp;
	    	if (sort.equalsIgnoreCase("level")) {
	    		comp = a.getLevel() - b.getLevel();
	    		if (comp == 0) {
	    			comp = rarityPriorities.get(a.getRarity()) - rarityPriorities.get(b.getRarity());
	    		}
	    	}
	    	else if (sort.equalsIgnoreCase("rarity")) {
	    		comp = rarityPriorities.get(a.getRarity()) - rarityPriorities.get(b.getRarity());
	    		if (comp == 0) {
		    		comp = a.getLevel() - b.getLevel();
	    		}
	    	}
	    	else if (sort.equalsIgnoreCase("amount")) {
	    		comp = StorageManager.getAmount(p, a.getID()) - StorageManager.getAmount(p, b.getID());
	    		if (comp == 0) {
		    		comp = a.getLevel() - b.getLevel();
	    		}
	    	}
	    	else {
	    		comp = a.getDisplay().compareTo(b.getDisplay());
	    	}
	    	
	    	if (reverse) {
	    		comp *= -1;
	    	}
	    	return comp;
	    }
	}
	
}
