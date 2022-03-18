package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Collections;
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
import me.Neoblade298.NeoProfessions.Storage.Sorter;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class StorageView extends ProfessionInventory {
	private Inventory inv;
	private InvSorter invsorter;
	private Player p;
	private ArrayList<StoredItemInstance> items;
	private int page = 1;
	private Sorter[] sorters;
	
	public static final int INFO_BUTTON = 49;
	public static final int NEXT_BUTTON = 53;
	public static final int PREVIOUS_BUTTON = 45;
	
	public StorageView(Player p, int min, int max, Inventory inv) {
		this.p = p;
		this.inv = inv;
		this.sorters = new Sorter[5];
		this.invsorter = new InvSorter();
		
		// Setup sorters
		sorters[1] = new Sorter(Sorter.LEVEL_SORT, 1, false);
		sorters[2] = new Sorter(Sorter.RARITY_SORT, 2, true);
		sorters[3] = new Sorter(Sorter.AMOUNT_SORT, 3, true);
		sorters[4] = new Sorter(Sorter.NAME_SORT, 4, false);
		
		// Setup itemstacks to be used for sorting
		items = new ArrayList<StoredItemInstance>();
		for (int i = min; i <= max; i++) {
			if (!StorageManager.getItemDefinitions().containsKey(i)) {
				break;
			}
			StoredItem item = StorageManager.getItemDefinitions().get(i);
			int amount = StorageManager.getAmount(p, i);
			if (amount > 0) {
				items.add(new StoredItemInstance(item, amount));
			}
		}
		
		sortItems();
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private ItemStack[] setupAll(ItemStack[] contents) {
		return setupUtilityButtons(setupInventory(contents));
	}
	
	// Sort by rarity
	// Sort alphabetically
	// Sort by level
	private ItemStack[] setupInventory(ItemStack[] contents) {
		// Sort items on construction and on change sort type
		for (int i = (page - 1) * 45; i < 45 * page; i++) {
			contents[i] = items.get(i).getStorageView(p);
		}	
		
		return contents;
	}
	
	private ItemStack[] setupUtilityButtons(ItemStack[] contents) {
		contents[INFO_BUTTON] = createInfoItem();
		if (page > 1) {
			contents[PREVIOUS_BUTTON] = createPreviousButton();
		}
		if (items.size() > page * 45) {
			contents[NEXT_BUTTON] = createNextButton();
		}
		
		int sortNum = 0;
		for (int i = 47; i <= 51; i++) {
			if (i == INFO_BUTTON) {
				continue;
			}
			contents[i] = sorters[sortNum].createSortButton();
			sortNum++;
		}
		
		return contents;
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
	
	private void createVoucher(Player p, int amount, int slot) {
		this.items.get(((page - 1) * 45) + slot).giveVoucher(p, amount);
	}
	
	private void viewRecipes(StoredItemInstance inst) {
		
	}
	
	private void changeSortOrder(int priority) {
		sorters[priority].flipOrder();
		sortItems();
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private void changeSortPriority(int oldPriority, int hotbar) {
		if (hotbar > 3 || hotbar <= 0) {
			return;
		}
		if (oldPriority == hotbar) {
			return;
		}
		
		Sorter temp = sorters[oldPriority];
		sorters[hotbar].setPriority(oldPriority);
		temp.setPriority(hotbar);
		sorters[oldPriority] = sorters[hotbar];
		sorters[hotbar] = temp;
		sortItems();
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private void sortItems() {
		Collections.sort(items, invsorter);
	}

	 
	// Helper class implementing Comparator interface
	private class InvSorter implements Comparator<StoredItemInstance> {
	 
	    // Method
	    // Sorting in ascending order of roll number
	    public int compare(StoredItemInstance a, StoredItemInstance b)
	    {
	    	int comp = 0;
	    	for (int i = 0; i <= 3; i++) {
	    		comp = sorters[i].compare(a, b);
	    		if (comp != 0) {
	    			break;
	    		}
	    	}
	    	return comp;
	    }
	}
	
}
