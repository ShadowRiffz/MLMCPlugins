package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
		lore.add("§7§oFor all items:");
		lore.add("§7§oLeft click to create 1x voucher");
		lore.add("§7§oRight click to sell 1x");
		lore.add("§7§oShift click for 10x");
		lore.add("§7§oPress 1 to see recipes involving the item");
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
		e.setCancelled(true);
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getType().isAir()) {
			return;
		}
		NBTItem nbti = new NBTItem(item);
		int slot = e.getRawSlot();
		String type = nbti.getString("type");
		
		if (type.equals("next")) {
			page++;
			inv.setContents(setupAll(inv.getContents()));
			return;
		}
		else if (type.equals("previous")) {
			page--;
			inv.setContents(setupAll(inv.getContents()));
			return;
		}
		
		if (e.getClick().equals(ClickType.LEFT)) {
			if (slot < 45) {
				createVoucher(p, 1, slot);
			}
			else if (type.equals("sort")) {
				changeSortOrder(nbti.getInteger("priority"));
			}
		}
		else if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
			if (slot < 45) {
				createVoucher(p, 10, slot);
			}
		}
		else if (e.getClick().equals(ClickType.RIGHT)) {
			if (slot < 45) {
				sellItem(p, 1, slot);
			}
		}
		else if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
			if (slot < 45) {
				sellItem(p, 10, slot);
			}
		}
		else if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			if (slot < 45) {
				if (e.getHotbarButton() == 1) {
					viewRecipes(p, slot);
				}
			}
			else if (type.equals("sort")) {
				changeSortPriority(nbti.getInteger("priority"), e.getHotbarButton());
			}
		}
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	private void sellItem(Player p, int amount, int slot) {
		StoredItemInstance si = this.items.get(((page - 1) * 45) + slot);
		if (si.sell(p, amount)) {
			p.sendMessage("§4[§c§lMLMC§4] §7Successfully sold " + si.getItem().getDisplay() + "§fx" + si.getAmount() + 
					"§7for §a" + (si.getAmount() * si.getItem().getValue()) + "g§7!");
		}
	}
	
	private void createVoucher(Player p, int amount, int slot) {
		StoredItemInstance si = this.items.get(((page - 1) * 45) + slot);
		if (si.giveVoucher(p, amount)) {
			p.sendMessage("§4[§c§lMLMC§4] §7Successfully created voucher for " + si.getItem().getDisplay() + "§fx" + si.getAmount() + "§7!");
		}
	}
	
	private void viewRecipes(Player p, int slot) {
		// StoredItemInstance si = this.items.get(((page - 1) * 45) + slot);
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


	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		
	}
}
