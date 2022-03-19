package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Recipes.Recipe;
import me.Neoblade298.NeoProfessions.Recipes.RecipeManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class RecipeView extends ProfessionInventory {
	private Inventory inv;
	private Player p;
	private ArrayList<Recipe> recipes;
	private int page = 1;
	private int mode = 0;
	
	private static final int RESULT_MODE = 0;
	private static final int REQUIREMENTS_MODE = 1;
	
	public static final int INFO_BUTTON = 49;
	public static final int NEXT_BUTTON = 53;
	public static final int PREVIOUS_BUTTON = 45;
	
	public RecipeView(Player p, StoredItem base, Inventory inv) {
		this.p = p;
		this.inv = inv;
		
		// Setup itemstacks to be used for sorting
		recipes = new ArrayList<Recipe>();
		for (String key : base.getRelevantRecipes()) {
			recipes.add(RecipeManager.recipes.get(key));
		}
		
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
			if (mode == RESULT_MODE) {
				contents[i] = recipes.get(i).getResult().getResultItem();
			}
			else if (mode == REQUIREMENTS_MODE){
				// get requirement icon
			}
		}	
		
		return contents;
	}
	
	private ItemStack[] setupUtilityButtons(ItemStack[] contents) {
		contents[INFO_BUTTON] = createInfoItem();
		if (page > 1) {
			contents[PREVIOUS_BUTTON] = createPreviousButton();
		}
		if (recipes.size() > page * 45) {
			contents[NEXT_BUTTON] = createNextButton();
		}
		
		return contents;
	}
	
	private ItemStack createInfoItem() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Info");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§oFor all items:");
		lore.add("§7§oLeft click to craft 1x");
		lore.add("§7§oRight click to view recipe components");
		lore.add("§7§oPress 1 to toggle between");
		lore.add("§7§oResult and Requirement view");
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
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}


	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		
	}
}
