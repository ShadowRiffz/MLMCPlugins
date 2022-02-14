package me.Neoblade298.NeoProfessions.Inventories;

import java.util.Arrays;
import java.util.HashMap;
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
import me.Neoblade298.NeoProfessions.Methods.ProfessionsMethods;
import me.Neoblade298.NeoProfessions.Objects.ScaleSet;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class CreateSlotInventory implements ProfessionInventory {
	private final Inventory inv;
	private final ItemStack item;
	private final Player p;
	private final int goldCost;
	private final int essenceCost;
	private final int level;
	private static final int CREATESLOT_ICON = 8;
	private static final int MENU_MODEL = 5000;
	private static final HashMap<Integer, ScaleSet> goldPrices = new HashMap<Integer, ScaleSet>();
	private static final HashMap<Integer, Integer> essencePrices = new HashMap<Integer, Integer>();
	
	static {
		goldPrices.put(1, new ScaleSet(500, 500));
		goldPrices.put(2, new ScaleSet(750, 750));
		goldPrices.put(3, new ScaleSet(1000, 1000));
		goldPrices.put(4, new ScaleSet(1500, 1500));
		goldPrices.put(5, new ScaleSet(2500, 2500));
		goldPrices.put(6, new ScaleSet(4000, 4000));
		
		essencePrices.put(1, 12);
		essencePrices.put(2, 24);
		essencePrices.put(3, 36);
		essencePrices.put(4, 48);
		essencePrices.put(5, 60);
		essencePrices.put(6, 80);
	}

	public CreateSlotInventory(Professions main, Player p) {
		this.p = p;
		this.item = p.getInventory().getItemInMainHand();
		p.getInventory().removeItem(item);
		level = new NBTItem(item).getInteger("level");
		int nextSlot = new NBTItem(item).getInteger("slotsCreated") + 1;
		inv = Bukkit.createInventory(p, 9, "�cAdd slot " + nextSlot + "?");
		
		ScaleSet costSet = goldPrices.get(nextSlot);
		goldCost = costSet.getBase() + (costSet.getScale() * (level - 1));
		essenceCost = essencePrices.get(nextSlot);
		
		ItemStack[] contents = inv.getContents();
		contents[0] = item;
		contents[1] = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
		for (int i = 2; i < CREATESLOT_ICON; i++) {
			contents[i] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "�cNo");
		}
		contents[CREATESLOT_ICON] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "�aYes",
				"�7Gold cost: �e" + goldCost + "g", "�7Essence cost: �e" + essenceCost);
		inv.setContents(contents);
		main.viewingInventory.put(p, this);

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

	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getRawSlot() >= 2 && e.getRawSlot() <= 7) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
			p.closeInventory();
		}
		else if (e.getRawSlot() == CREATESLOT_ICON) {
			if (!Professions.econ.has(p, goldCost)) {
				Util.sendMessage(p, "&cYou don't have enough money!");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
				p.closeInventory();
				return;
			}
			else if (!Professions.cm.hasEnough(p, "essence", level, essenceCost)) {
				Util.sendMessage(p, "&cYou don't have enough essence!");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
				p.closeInventory();
				return;
			}
			
			if (!ProfessionsMethods.createSlot(p, item)) {
				e.setCancelled(true);
				p.closeInventory();
				return;
			}
			Professions.econ.withdrawPlayer(p, goldCost);
			Professions.cm.add(p, "essence", level, -essenceCost);
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 1F);
			p.closeInventory();
		}
		e.setCancelled(true);
	}
	
	public void handleInventoryDrag(final InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		HashMap<Integer, ItemStack> failed = p.getInventory().addItem(item);
		for (Integer num : failed.keySet()) {
			p.getWorld().dropItem(p.getLocation(), failed.get(num));
		}
	}
}
