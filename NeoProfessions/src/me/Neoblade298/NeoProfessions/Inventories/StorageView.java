package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.SkullCreator;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.Sorter;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class StorageView extends ProfessionInventory {
	private static InvSorter invsorter;
	private Player p;
	private ArrayList<StoredItemInstance> items;
	private int page = 1;
	private Sorter[] sorters;
	
	private int min, max;
	private int mode = 0;
	private static final int INFO_MODE = 0;
	private static final int DISPLAY_MODE = 1;
	
	public static final int INFO_BUTTON = 49;
	public static final int NEXT_BUTTON = 53;
	public static final int PREVIOUS_BUTTON = 45;
	public static ArrayList<String> info = new ArrayList<String>();
	
	public static final String INFO_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RiYWYyMDNjZTlkNDliNzJjYWRlNDA1Yjg2MWFjZmU0YjY1M2RjOGM4YTQzZTgwYjY3MGZhOTdlNTYwZWZlYiJ9fX0=";
	public static final String PREV_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0=";
	public static final String NEXT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0=";
	
	static {
		info.add("�9�oLeft click �7�oto create 1x voucher");
		info.add("�9�oRight click �7�oto sell 1x");
		info.add("�9�oShift click �7�ofor 10x");
		info.add("�9�oPress 1 �7�ofor info mode (Current)");
		info.add("�9�oPress 2 �7�ofor display mode");
		info.add("�9�oPress 3 �7�oto see relevant recipes");
	}
	
	public StorageView(Player p, int min, int max) {
		this.p = p;
		this.inv = Bukkit.createInventory(p, 54, "�9Storage View");
		p.openInventory(inv);
		this.min = min;
		this.max = max;
		this.sorters = new Sorter[5];
		invsorter = new InvSorter();
		Professions.viewingInventory.put(p, this);
		
		// Setup sorters
		int namePriority = (int) StorageManager.settings.getValue(p.getUniqueId(), "name-priority");
		int levelPriority = (int) StorageManager.settings.getValue(p.getUniqueId(), "level-priority");
		int rarityPriority = (int) StorageManager.settings.getValue(p.getUniqueId(), "rarity-priority");
		int amountPriority = (int) StorageManager.settings.getValue(p.getUniqueId(), "amount-priority");
		boolean nameOrder = (boolean) StorageManager.settings.getValue(p.getUniqueId(), "name-order");
		boolean levelOrder = (boolean) StorageManager.settings.getValue(p.getUniqueId(), "level-order");
		boolean rarityOrder = (boolean) StorageManager.settings.getValue(p.getUniqueId(), "rarity-order");
		boolean amountOrder = (boolean) StorageManager.settings.getValue(p.getUniqueId(), "amount-order");
		sorters[namePriority] = new Sorter(Sorter.NAME_SORT, namePriority, nameOrder);
		sorters[levelPriority] = new Sorter(Sorter.LEVEL_SORT, levelPriority, levelOrder);
		sorters[rarityPriority] = new Sorter(Sorter.RARITY_SORT, rarityPriority, rarityOrder);
		sorters[amountPriority] = new Sorter(Sorter.AMOUNT_SORT, amountPriority, amountOrder);
		
		// Setup itemstacks to be used for sorting
		items = new ArrayList<StoredItemInstance>();
		if (min == -1) {
			setupItems();
		}
		else {
			setupItems(min, max);
		}
		
		sortItems();
		inv.setContents(new ItemStack[54]);
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private void setupItems() {
		for (int id : StorageManager.getStorage(p).keySet()) {
			StoredItem item = StorageManager.getItemDefinitions().get(id);
			int amount = StorageManager.getAmount(p, id);
			if (amount > 0) {
				items.add(new StoredItemInstance(item, amount));
			}
		}
	}
	
	private void setupItems(int min, int max) {
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
	}
	
	private ItemStack[] setupAll(ItemStack[] contents) {
		return setupUtilityButtons(setupInventory(contents));
	}
	
	private ItemStack[] updateSlot(ItemStack[] contents, int slot) {
		int top = (page - 1) * 45;
		contents[slot] = items.get(top + slot).getStorageView(p);
		return contents;
	}
	
	// Sort by rarity
	// Sort alphabetically
	// Sort by level
	private ItemStack[] setupInventory(ItemStack[] contents) {
		// Sort items on construction and on change sort type
		int count = 0;
		for (int i = (page - 1) * 45; i < 45 * page; i++) {
			if (items.size() <= i) {
				break;
			}
			
			if (mode == INFO_MODE) {
				ItemStack item = items.get(i).getStorageView(p);
				ItemMeta meta = item.getItemMeta();
				meta.setLore(info);
				item.setItemMeta(meta);
				contents[count++] = item;
			}
			else if (mode == DISPLAY_MODE) {
				contents[count++] = items.get(i).getStorageView(p);
			}
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
		
		int sortNum = 1;
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
		ItemStack item = SkullCreator.itemFromBase64(INFO_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�9Info");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("�9�oLeft click �7�oto create 1x voucher");
		lore.add("�9�oRight click �7�oto sell 1x");
		lore.add("�9�oShift click �7�ofor 10x");
		lore.add("�9�oPress 1 �7�ofor info mode");
		lore.add("�9�oPress 2 �7�ofor display mode");
		lore.add("�9�oPress 3 �7�oto see relevant recipes");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "info");
		return nbti.getItem();
	}
	
	private ItemStack createPreviousButton() {
		ItemStack item = SkullCreator.itemFromBase64(PREV_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�9Previous Page");
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "previous");
		return nbti.getItem();
	}
	
	private ItemStack createNextButton() {
		ItemStack item = SkullCreator.itemFromBase64(NEXT_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�9Next Page");
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
			int hotbar = e.getHotbarButton() + 1;
			if (slot < 45) {
				if (slot < 45 && (hotbar == 1 || hotbar == 2)) {
					changeMode(hotbar);
				}
				else if (hotbar == 3) {
					viewRecipes(p, slot);
				}
			}
			else if (type.equals("sort")) {
				changeSortPriority(nbti.getInteger("priority"), hotbar);
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
			p.sendMessage("�4[�c�lMLMC�4] �7Successfully sold " + si.getItem().getDisplay() + " �fx" + amount + 
					" �7for �a" + (amount * si.getItem().getValue()) + "g�7!");
		}
		inv.setContents(updateSlot(inv.getContents(), slot));
	}
	
	private void createVoucher(Player p, int amount, int slot) {
		StoredItemInstance si = this.items.get(((page - 1) * 45) + slot);
		if (si.giveVoucher(p, amount)) {
			p.sendMessage("�4[�c�lMLMC�4] �7Successfully created voucher for " + si.getItem().getDisplay() + " �fx" + amount + "�7!");
		}
		inv.setContents(updateSlot(inv.getContents(), slot));
	}
	
	private void viewRecipes(Player p, int slot) {
		StoredItemInstance si = this.items.get(((page - 1) * 45) + slot);
		if (si.getItem().getRelevantRecipes().size() > 0) {
			new RecipeView(p, si.getItem(), min, max);
		}
	}
	
	private void changeMode(int hotbar) {
		mode = hotbar - 1;
		inv.setContents(setupInventory(inv.getContents()));
	}
	
	private void changeSortOrder(int priority) {
		Sorter sorter = sorters[priority];
		boolean newOrder = sorter.flipOrder();
		StorageManager.settings.changeSetting(sorter.getSettingString() + "-order", Boolean.toString(newOrder), p.getUniqueId());
		
		sortItems();
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private void changeSortPriority(int oldPriority, int hotbar) {
		if (hotbar > 4 || hotbar < 1) {
			return;
		}
		if (oldPriority == hotbar) {
			return;
		}
		
		Sorter toChange = sorters[oldPriority];
		Sorter changingWith = sorters[hotbar];
		StorageManager.settings.changeSetting(toChange.getSettingString() + "-priority", Integer.toString(hotbar), p.getUniqueId());
		StorageManager.settings.changeSetting(changingWith.getSettingString() + "-priority", Integer.toString(oldPriority), p.getUniqueId());
		
		changingWith.setPriority(oldPriority);
		toChange.setPriority(hotbar);
		sorters[oldPriority] = changingWith;
		sorters[hotbar] = toChange;
		sortItems();
		inv.setContents(setupAll(inv.getContents()));
	}
	
	private void sortItems() {
		Collections.sort(items, invsorter);
	}
	 
	private class InvSorter implements Comparator<StoredItemInstance> {
	    public int compare(StoredItemInstance a, StoredItemInstance b)
	    {
	    	int comp = 0;
	    	for (int i = 1; i <= 4; i++) {
	    		comp = sorters[i].compare(a, b);
	    		if (comp != 0) {
	    			break;
	    		}
	    	}
	    	return comp;
	    }
	}


	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {}
}