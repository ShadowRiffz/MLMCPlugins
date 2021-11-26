package me.neoblade298.neoresearch.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import me.neoblade298.neoresearch.Research;

public class InventoryListeners implements Listener {
	Research main;
	public InventoryListeners(Research main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (main.viewingInventory.containsKey(p)) {
			main.viewingInventory.get(p).handleInventoryClick(e);
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (main.viewingInventory.containsKey(p)) {
			main.viewingInventory.get(p).handleInventoryDrag(e);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (main.viewingInventory.containsKey(p)) {
			main.viewingInventory.get(p).handleInventoryClose(e);
		}
		main.viewingInventory.remove(p);
	}
}
