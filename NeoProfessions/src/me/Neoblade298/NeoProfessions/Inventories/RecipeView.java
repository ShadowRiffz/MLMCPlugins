package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Recipes.Recipe;
import me.Neoblade298.NeoProfessions.Recipes.RecipeManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;

public class RecipeView extends ProfessionInventory {
	private Inventory inv;
	private Player p;
	private ArrayList<Recipe> recipes;
	StoredItem base;
	private int page = 1;
	private int mode = 0;
	private static RecipeSorter rsorter;
	
	private int min, max;
	
	private static final int RESULT_MODE = 0;
	private static final int REQUIREMENTS_MODE = 1;
	
	public static final String HOUSE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzVhMzViNWNhMTUyNjg2ODVjNDY2MDUzNWU1ODgzZDIxYTVlYzU3YzU1ZDM5NzIzNDI2OWFjYjVkYzI5NTRmIn19fQ==";
	
	public static final int INFO_BUTTON = 49;
	public static final int NEXT_BUTTON = 53;
	public static final int PREVIOUS_BUTTON = 45;
	public static final int HOME_BUTTON = 46;
	
	public RecipeView(Player p, StoredItem base, Inventory inv, int min, int max) {
		this.p = p;
		this.inv = inv;
		this.base = base;
		rsorter = new RecipeSorter();
		Professions.viewingInventory.put(p, this);
		
		this.min = min;
		this.max = max;
		
		// Setup recipes relevant to the base
		recipes = new ArrayList<Recipe>();
		for (String key : base.getRelevantRecipes()) {
			recipes.add(RecipeManager.recipes.get(key));
		}
		Collections.sort(recipes, rsorter);
		
		inv.setContents(new ItemStack[54]);
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
		int count = 0;
		for (int i = (page - 1) * 45; i < 45 * page; i++) {
			if (recipes.size() <= i) {
				break;
			}
			
			if (mode == RESULT_MODE) {
				contents[count++] = recipes.get(i).getResult().getResultItem(p);
			}
			else if (mode == REQUIREMENTS_MODE){
				contents[count++] = recipes.get(i).getReqsIcon(p);
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
		if (recipes.size() > page * 45) {
			contents[NEXT_BUTTON] = createNextButton();
		}
		
		return contents;
	}
	
	private ItemStack createInfoItem() {
		ItemStack item = SkullCreator.itemFromBase64(StorageView.INFO_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Info");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oFor all items:");
		lore.add("§9§oLeft click §7§oto craft 1x");
		lore.add("§9§oShift left click §7§ofor 10x");
		lore.add("§9§oRight click §7§oto view components");
		lore.add("§9§oPress 1 §7§oto toggle between");
		lore.add("§7§oResult and Requirement view");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "info");
		return nbti.getItem();
	}
	
	private ItemStack createHomeItem() {
		ItemStack item = SkullCreator.itemFromBase64(HOUSE_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Back");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oReturn to storage");
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
		else if (type.equals("home")) {
			new StorageView(p, min, max, inv);
			return;
		}
		
		if (e.getClick().equals(ClickType.LEFT)) {
			if (slot < 45) {
				craftItem(slot, 1);
			}
		}
		else if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
			if (slot < 45) {
				craftItem(slot, 10);
			}
		}
		else if (e.getClick().equals(ClickType.RIGHT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
			if (slot < 45) {
				viewComponents(p, slot);
			}
		}
		else if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			int hotbar = e.getHotbarButton() + 1;
			if (hotbar == 1) {
				toggleIcons();
			}
		}
	}
	
	private void toggleIcons() {
		if (mode == RESULT_MODE) {
			mode = REQUIREMENTS_MODE;
		}
		else {
			mode = RESULT_MODE;
		}
		inv.setContents(setupInventory(inv.getContents()));
	}
	
	private void viewComponents(Player p, int slot) {
		Recipe recipe = this.recipes.get(((page - 1) * 45) + slot);
		new ComponentView(p, recipe, base, min, max, inv);
	}
	
	private void craftItem(int slot, int amount) {
		Recipe recipe = this.recipes.get(((page - 1) * 45) + slot);
		if (recipe.canMulticraft() || amount == 1) {
			recipe.craftRecipe(p);
		}
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}


	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		
	}
	 
	private class RecipeSorter implements Comparator<Recipe> {
	    public int compare(Recipe a, Recipe b)
	    {
	    	int comp = 0;
	    	int canA = a.canCraft(p) ? 1 : 0;
	    	int canB = b.canCraft(p) ? 1 : 0;
	    	comp = canB - canA;
	    	if (comp == 0) {
	    		comp = b.getKey().compareTo(a.getKey());
	    	}
	    	return comp;
	    }
	}
}
