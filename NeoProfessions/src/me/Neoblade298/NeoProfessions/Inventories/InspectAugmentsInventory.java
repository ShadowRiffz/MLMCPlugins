package me.Neoblade298.NeoProfessions.Inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import me.Neoblade298.NeoProfessions.Augments.ItemEditor;

public class InspectAugmentsInventory implements ProfessionInventory {
	private final Inventory inv;
	ItemStack item;
	ItemEditor editor;

	public InspectAugmentsInventory(Professions main, Player p, ItemStack item) {
		this.item = item;
		this.editor = new ItemEditor(item);
		
		inv = Bukkit.createInventory(p, 9, "§cAugment Viewer");
		main.viewingInventory.put(p, this);

		inv.addItem(item);
		NBTItem nbti = new NBTItem(item);
		ItemStack[] contents = inv.getContents();
		for (int i = 1; i < 9 - nbti.getInteger("slotsCreated"); i++) {
			contents[i] = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
		}
		
		int j = 1;
		for (int i = 9 - nbti.getInteger("slotsCreated"); i < 9; i++) {
			Augment oldAug = editor.getAugment(j);
			if (oldAug == null) {
				contents[i] = createGuiItem(Material.ENDER_PEARL, "§7Empty slot");
			}
			else {
				contents[i] = createGuiItem(Material.ENDER_PEARL, oldAug.getLine(), oldAug.getItem().getItemMeta().getLore(), oldAug);
			}
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

	protected ItemStack createGuiItem(final Material material, final String name, List<String> list, Augment aug) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		if (aug.isPermanent()) {
			meta.setDisplayName("§cThis augment cannot be swapped");
		}
		else {
			meta.setDisplayName(name);
		}
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	// Check for clicks on items
	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != inv)
			return;

		e.setCancelled(true);
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
