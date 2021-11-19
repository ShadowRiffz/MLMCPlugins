package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ReplaceSlotInventory implements Listener {
	private final Inventory inv;
	ItemStack item;
	ItemStack augment;
	MasonUtils masonUtils;
	String slotType;
	Util util;
	ArrayList<String> bonusSlots;

	public ReplaceSlotInventory(Professions main, Player p, ItemStack item, ItemStack augment, MasonUtils masonUtils, String slotType, Util util) {
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		inv = Bukkit.createInventory(p, 9, "§cReplace an existing augment?");

		inv.addItem(item);
		inv.addItem(augment);
		ItemStack[] contents = inv.getContents();
		bonusSlots = masonUtils.getBonusSlots(item);
		for (int i = 2; i < 9 - bonusSlots.size(); i++) {
			contents[i] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "§cNo");
		}
		for (int i = 9 - bonusSlots.size(); i < 9; i++) {
			String line = bonusSlots.get(i - (9 - bonusSlots.size()));
			contents[i] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aReplace " + line, "§cWARNING: You will permanently lose", "§cthe augment you replace!");
		}
		inv.setContents(contents);
		this.augment = augment;
		this.item = item;
		this.slotType = slotType;
		this.masonUtils = masonUtils;
		this.util = util;

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
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
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
		if (e.getRawSlot() >= 9 - bonusSlots.size()) {
		boolean success = false;
		switch (slotType) {
			case "durability":
				success = masonUtils.parseDurability(item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "weaponattribute":
				success = masonUtils.parseAttribute(item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "armorattribute":
				success = masonUtils.parseAttribute(item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "weaponoverload":
				success = masonUtils.parseOverload(item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "armoroverload":
				success = masonUtils.parseOverload(item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "charm":
				success = masonUtils.parseCharm(p, item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				break;
			case "relic":
				if (!masonUtils.hasRelic(item) && e.getCurrentItem().getItemMeta().getDisplayName().contains("Relic")) {
					success = masonUtils.parseRelic(p, item, augment, 8 - e.getRawSlot() + bonusSlots.size());
				}
				else {
					Util.sendMessage(p, "&cOnly one relic may be slotted per item!");
				}
				break;
			}
			if (success) {
				Util.sendMessage(p, "&7Successfully slotted item!");
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
				p.getInventory().removeItem(augment);
				p.closeInventory();
			}
			else {
				Util.sendMessage(p, "&cFailed to slot item!");
			}
		}
		else if (e.getRawSlot() > 1) {
			p.closeInventory();
		}
	}

	// Cancel dragging in our inventory
	@EventHandler
	public void onInventoryClick(final InventoryDragEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
}
