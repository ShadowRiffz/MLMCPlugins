package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public abstract class ProfessionInventory {
	protected Inventory inv;
	public abstract void handleInventoryClick(InventoryClickEvent e);
	public abstract void handleInventoryDrag(InventoryDragEvent e);
	public abstract void handleInventoryClose(InventoryCloseEvent e);
	public Inventory getInventory() {
		return this.inv;
	}
}
