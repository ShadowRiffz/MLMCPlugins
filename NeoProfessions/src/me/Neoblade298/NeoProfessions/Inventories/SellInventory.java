package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class SellInventory implements Listener {
	private final Inventory inv;
	Util util;
	private final int SELL_ICON = 8;
	private final int MENU_MODEL = 5000;
	private boolean sold = false;

	public SellInventory(Main main, Player p) {
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		inv = Bukkit.createInventory(p, 54, "§cPlace items here to sell");
		ItemStack[] contents = inv.getContents();
		contents[SELL_ICON] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "§aConfirm Sell", "§7Unsellable items will remain");
		inv.setContents(contents);

		p.openInventory(inv);
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		meta.setCustomModelData(MENU_MODEL);
		item.setItemMeta(meta);
		return item;
	}

	// Check for clicks on items
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (!sold && e.getClickedInventory() == inv && e.getRawSlot() == SELL_ICON) {
			e.setCancelled(true);
			confirmSell((Player) e.getWhoClicked());
		}
		
		else if (sold) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrag(final InventoryDragEvent e) {
		if (sold) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getInventory() == inv) {
			for (ItemStack item : inv.getContents()) {
				if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == MENU_MODEL) {
					continue;
				}
				HashMap<Integer, ItemStack> failed = p.getInventory().addItem(item);
				for (Entry<Integer, ItemStack> entry : failed.entrySet()) {
					p.getWorld().dropItem(p.getLocation(), entry.getValue());
				}
			}
		}
	}
	
	private void confirmSell(Player p) {
		double totalSell = 0;
		int numSold = 0;
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];
			if (i == SELL_ICON) continue;
			
			if (item != null) {
				NBTItem nbti = new NBTItem(item);
				
				if (!nbti.hasKey("value")) continue;
				
				double value = nbti.getDouble("value");
				totalSell += value;
				numSold += item.getAmount();
				
				ItemStack receipt = new ItemStack(Material.PAPER);
				ItemMeta receiptMeta = receipt.getItemMeta();
				
				String name = item.hasItemMeta() ? item.getItemMeta().getDisplayName() : item.getType().name();
				receiptMeta.setDisplayName(name);
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Sold for: §a" + value + "g");
				receiptMeta.setLore(lore);
				receiptMeta.setCustomModelData(MENU_MODEL);
				receipt.setItemMeta(receiptMeta);
				contents[i] = receipt;
			}
		}
		ItemStack totalReceipt = new ItemStack(Material.OAK_SIGN);
		ItemMeta receiptMeta = totalReceipt.getItemMeta();
		receiptMeta.setDisplayName("§aSuccessfully sold " + numSold + " items!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Total gold gained: §a" + totalSell + "g");
		lore.add("§7If any items remain, they cannot be sold.");
		receiptMeta.setLore(lore);
		receiptMeta.setCustomModelData(MENU_MODEL);
		totalReceipt.setItemMeta(receiptMeta);
		contents[SELL_ICON] = totalReceipt;
		sold = true;
	}
}
