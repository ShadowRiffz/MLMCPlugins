package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ConfirmSlotInventory implements ProfessionInventory {
	private final Inventory inv;
	ItemStack item;
	ItemStack augment;
	MasonUtils masonUtils;
	String slotType;
	int slot;
	Util util;

	public ConfirmSlotInventory(Professions main, Player p, ItemStack item, ItemStack augment, MasonUtils masonUtils, String slotType, int slot, Util util) {
		inv = Bukkit.createInventory(p, 9, "§cAre you sure?");
		main.viewingInventory.put(p, this);

		inv.addItem(item);
		inv.addItem(augment);
		ItemStack[] contents = inv.getContents();
		for (int i = 2; i < 8; i++) {
			contents[i] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "§cNo");
		}
		inv.setContents(contents);
		inv.addItem(createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aYes"));
		this.augment = augment;
		this.item = item;
		this.slotType = slotType;
		this.masonUtils = masonUtils;
		this.slot = slot;
		this.util = util;

		p.openInventory(inv);
	}

	protected ItemStack createGuiItem(final Material material, final String name) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	// Check for clicks on items
	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != inv)
			return;

		e.setCancelled(true);

		final ItemStack clickedItem = e.getCurrentItem();

		// verify current item is not null
		if (clickedItem == null || clickedItem.getType().isAir()) {
			return;
		}

		final Player p = (Player) e.getWhoClicked();

		// Using slots click is a best option for your inventory click's
		if (e.getRawSlot() == 8) {
		boolean success = false;
		switch (slotType) {
			case "durability":
				success = masonUtils.parseDurability(item, augment, slot);
				break;
			case "weaponattribute":
				success = masonUtils.parseAttribute(item, augment, slot);
				break;
			case "armorattribute":
				success = masonUtils.parseAttribute(item, augment, slot);
				break;
			case "weaponoverload":
				success = masonUtils.parseOverload(item, augment, slot);
				break;
			case "armoroverload":
				success = masonUtils.parseOverload(item, augment, slot);
				break;
			case "charm":
				success = masonUtils.parseCharm(p, item, augment, slot);
				break;
			case "relic":
				if (!masonUtils.hasRelic(item)) {
					success = masonUtils.parseRelic(p, item, augment, slot);
				}
				else {
					util.sendMessage(p, "&cOnly one relic may be slotted per item!");
				}
				break;
			}
			if (success) {
				util.sendMessage(p, "&7Successfully slotted item!");
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
				p.getInventory().removeItem(augment);
				p.closeInventory();
			}
			else {
				util.sendMessage(p, "&cFailed to slot item!");
			}
		}
		else if (e.getRawSlot() > 1) {
			p.closeInventory();
		}
	}

	// Cancel dragging in our inventory
	public void handleInventoryDrag(final InventoryDragEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
}
