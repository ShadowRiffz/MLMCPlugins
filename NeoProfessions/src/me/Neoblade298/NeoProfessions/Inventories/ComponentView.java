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
import me.Neoblade298.NeoProfessions.Recipes.Recipe;
import me.Neoblade298.NeoProfessions.Storage.Sorter;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class ComponentView extends ProfessionInventory {
	private static InvSorter invsorter;
	private Player p;
	private ArrayList<StoredItemInstance> components;
	private int page = 1;
	private StoredItem base;
	private Sorter[] sorters;
	private int mode = 0;
	private Recipe recipe;
	
	private int min, max;
	public static ArrayList<String> info = new ArrayList<String>();

	private static final int INFO_MODE = 0;
	private static final int DISPLAY_MODE = 1;
	public static final int INFO_BUTTON = 49;
	public static final int HOME_BUTTON = 46;
	public static final int NEXT_BUTTON = 53;
	public static final int PREVIOUS_BUTTON = 45;
	
	static {
		info.add("§9§oLeft click §7§oto see relevant recipes");
		info.add("§9§oPress 1 §7§ofor info mode (Current)");
		info.add("§9§oPress 2 §7§ofor display mode");
	}
	
	public ComponentView(Player p, Recipe recipe, StoredItem base, int min, int max) {
		this.p = p;
		this.inv = Bukkit.createInventory(p, 54, "§9Components View");
		p.openInventory(inv);
		this.min = min;
		this.max = max;
		this.recipe = recipe;
		this.base = base;
		this.sorters = new Sorter[5];
		invsorter = new InvSorter();
		
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
		components = new ArrayList<StoredItemInstance>();
		for (StoredItemInstance si : recipe.getComponents()) {
			components.add(si);
		}
		Collections.sort(components, invsorter);
		
		inv.setContents(new ItemStack[54]);
		inv.setContents(setupAll(inv.getContents()));
		Professions.viewingInventory.put(p, this);
	}
	
	private ItemStack[] setupAll(ItemStack[] contents) {
		return setupUtilityButtons(setupInventory(contents));
	}
	
	// Sort by rarity
	// Sort alphabetically
	// Sort by level
	private ItemStack[] setupInventory(ItemStack[] contents) {
		// Sort items on construction and on change sort type
		int count = 0;
		for (int i = (page - 1) * 45; i < 45 * page; i++) {
			if (components.size() <= i) {
				break;
			}
			StoredItemInstance si = components.get(i);
			if (mode == INFO_MODE) {
				ItemStack item = si.getCompareView(p, si.getAmount());
				ItemMeta meta = item.getItemMeta();
				meta.setLore(info);
				item.setItemMeta(meta);
				contents[count++] = item;
			}
			else if (mode == DISPLAY_MODE) {
				contents[count++] = si.getCompareView(p, si.getAmount());
			}
		}	
		
		return contents;
	}
	
	private ItemStack[] setupUtilityButtons(ItemStack[] contents) {
		contents[INFO_BUTTON] = createInfoItem();
		contents[HOME_BUTTON] = createHomeItem();
		if (page > 1) {
			contents[PREVIOUS_BUTTON] = createPreviousButton();
		}
		if (components.size() > page * 45) {
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
		ItemStack item = recipe.getResult().getResultItem(p, false);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Info");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Recipe: " + recipe.getResult().getResultItem(p, recipe.canCraft(p)).getItemMeta().getDisplayName());
		lore.add("§7§oFor all items:");
		lore.add("§9§oLeft click §7§oto see relevant recipes");
		lore.add("§9§oPress 1 §7§ofor info mode");
		lore.add("§9§oPress 2 §7§ofor display mode");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "info");
		return nbti.getItem();
	}
	
	private ItemStack createHomeItem() {
		ItemStack item = SkullCreator.itemFromBase64(RecipeView.HOUSE_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Back");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oReturn to recipe view");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "home");
		return nbti.getItem();
	}
	
	private ItemStack createPreviousButton() {
		ItemStack item = SkullCreator.itemFromBase64(StorageView.PREV_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Previous Page");
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "previous");
		return nbti.getItem();
	}
	
	private ItemStack createNextButton() {
		ItemStack item = SkullCreator.itemFromBase64(StorageView.NEXT_HEAD);
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
		String type = nbti.getString("type");
		int slot = e.getRawSlot();
		
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
		else if (type.equals("home")) {
			returnToRecipe();
			return;
		}
		
		if (e.getClick().equals(ClickType.LEFT)) {
			if (slot < 45) {
				viewRecipes(p, slot);
			}
			if (type.equals("sort")) {
				changeSortOrder(nbti.getInteger("priority"));
			}
		}
		else if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			int hotbar = e.getHotbarButton() + 1;
			if (slot < 45) {
				changeMode(hotbar);
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
	
	private void returnToRecipe() {
		new RecipeView(p, base, min, max);
	}
	
	private void viewRecipes(Player p, int slot) {
		StoredItemInstance si = this.components.get(((page - 1) * 45) + slot);
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
		
		Collections.sort(components, invsorter);
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
		Collections.sort(components, invsorter);
		inv.setContents(setupAll(inv.getContents()));
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
