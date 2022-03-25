package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Gardens.Garden;
import me.Neoblade298.NeoProfessions.Gardens.GardenSlot;
import me.Neoblade298.NeoProfessions.Managers.GardenManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;

public class GardenInventory extends ProfessionInventory {
	
	private Garden garden;
	private static final int HOME_SLOT = 49;

	public GardenInventory(Player p, ProfessionType type) {
		String display;
		switch (type) {
		case HARVESTER: display = "§9Your Garden";
		break;
		case LOGGER: display = "§9Your Arbor";
		break;
		case STONECUTTER: display = "§9Your Quarry";
		default: display = "§9Your Area";
		break;
		}
		garden = GardenManager.getGardens(p).get(type);
		
		inv = Bukkit.createInventory(p, 54, display);
		Professions.viewingInventory.put(p, this);

		inv.setContents(setupInventory(inv.getContents()));

		p.openInventory(inv);
	}
	private ItemStack[] setupInventory(ItemStack[] contents) {
		HashMap<Integer, GardenSlot> slots = garden.getSlots();
		for (int i = 0; i < garden.getSize(); i++) {
			if (slots.containsKey(i)) {
				contents[i] = slots.get(i).getIcon();
			}
			else {
				contents[i] = createEmptySlot();
			}
		}
		
		contents[HOME_SLOT] = createHomeButton();
		return contents;
	}
	
	private ItemStack createEmptySlot() {
		ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aEmpty Plot");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Left click to plant something!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack createHomeButton() {
		ItemStack item = SkullCreator.itemFromBase64(RecipeView.HOUSE_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Back to Gardens");
		item.setItemMeta(meta);
		return item;
	}

	// Check for clicks on items
	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != inv)
			return;

		e.setCancelled(true);
		final ItemStack item = e.getCurrentItem();
		// verify current item is not null
		if (item == null || item.getType().isAir()) {
			return;
		}
	}

	// Cancel dragging in this inventory
	public void handleInventoryDrag(final InventoryDragEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
}
