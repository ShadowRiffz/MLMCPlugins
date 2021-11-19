package me.Neoblade298.NeoProfessions.Inventories;

import java.util.Arrays;

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

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.AugmentEditor;
import me.Neoblade298.NeoProfessions.Augments.AugmentManager;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ConfirmAugmentInventory implements ProfessionInventory {
	private final Inventory inv;
	ItemStack item;
	ItemStack augment;
	MasonUtils masonUtils;
	String slotType;
	int slot;
	Util util;

	public ConfirmAugmentInventory(Professions main, Player p, ItemStack item, ItemStack augment) {
		this.augment = augment;
		this.item = item;
		
		inv = Bukkit.createInventory(p, 9, "§cReplace which slot?");
		main.viewingInventory.put(p, this);

		inv.addItem(item);
		NBTItem nbti = new NBTItem(item);
		inv.addItem(augment);
		ItemStack[] contents = inv.getContents();
		for (int i = 2; i < 8 - nbti.getInteger("slotsCreated"); i++) {
			contents[i] = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "");
		}
		
		int j = 1;
		for (int i = 8 - nbti.getInteger("slotsCreated") + 1; i < 8; i++) {
			String augmentOption = nbti.getString("slot" + j + "Line");
			int augmentLevel = nbti.getInteger("slot" + j + "Level");
			String lore = "";
			if (augmentOption.isEmpty()) {
				lore = "§7Empty slot";
			}
			else {
				lore = AugmentManager.nameMap.get(augmentOption).createNew(augmentLevel).getLine();
			}
			contents[i] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aSwap Slot " + j, lore);
			j++;
		}
		inv.setContents(contents);

		p.openInventory(inv);
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
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

		if (clickedItem.getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
			String clicked = clickedItem.getItemMeta().getDisplayName();
			int selected = Integer.parseInt(clicked.substring(clicked.length() - 1));
			NBTItem nbtaug = new NBTItem(this.augment);
			Augment aug = AugmentManager.nameMap.get(nbtaug.getString("augment")).createNew(nbtaug.getInteger("level"));
			
			if (new AugmentEditor(this.item).setAugment(p, aug, selected)) {
				util.sendMessage(p, "&7Successfully slotted item!");
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
				p.getInventory().removeItem(augment);
				p.closeInventory();
			}
			else {
				util.sendMessage(p, "&cFailed to slot item!");
			}
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
