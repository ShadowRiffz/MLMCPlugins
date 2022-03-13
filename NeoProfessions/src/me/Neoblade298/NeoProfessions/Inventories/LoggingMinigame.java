package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;
import me.Neoblade298.NeoProfessions.Minigames.MinigameManager;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class LoggingMinigame extends ProfessionInventory {
	private static float ERROR = 0.594604F;
	private static int TICK_RATE = 8;
	private static int TOTAL_TICKS = 20;
	private static int END_TICKS = 6;
	Professions main;
	Player p;
	int stage = 0;
	ArrayList<StoredItemInstance> drops;
	ArrayList<FallingItem> falling = new ArrayList<FallingItem>();
	double baseSpawnChance = 0.2;
	int difficulty;
	HashMap<Integer, StoredItemInstance> items, itemsAfter; // Need 2 because 1 gets its items removed
	ArrayList<ItemStack> toSpawn = new ArrayList<ItemStack>();
	int dropsPast = 0;
	
	private int dropNum = 0;
	
	public LoggingMinigame(Professions main, Player p, ArrayList<StoredItemInstance> drops, String name, int difficulty) {
		this.main = main;
		this.p = p;
		this.drops = drops;
		this.difficulty = difficulty;
		baseSpawnChance += difficulty * 0.1;
		inv = Bukkit.createInventory(p, 54, name.replaceAll("&", "§"));
		Professions.viewingInventory.put(p, this);
		
		ItemStack[] contents = inv.getContents();
		for (int i = 11; i <= 15; i++) {
			for (int j = 0; j < 4; j++) {
				contents[i + (j*9)] = generateStartButton();
			}
		}
		inv.setContents(contents);

		p.openInventory(inv);
	}
	
	private ItemStack generateStartButton() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aClick to start!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7§l§nInstructions");
		lore.add("§e" + drops.size() + " §7items will drop from the");
		lore.add("§7top. Click them to get them, but don't");
		lore.add("§7click any of the beehives!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateBeehive() {
		ItemStack item = new ItemStack(Material.BEE_NEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cDon't click me!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§cBzzzz");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateGameOver() {
		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7Game over!");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateDrop(StoredItemInstance drop, int tick) {
		ItemStack item = new ItemStack(drop.getItem().getRarity().getMaterial());
		StoredItem sitem = drop.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(sitem.getDisplay() + " x" + drop.getAmount());
		ArrayList<String> lore = sitem.getLore();
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("tick", tick);
		return nbti.getItem();
	}

	@Override
	public void handleInventoryClick(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getType().isAir()) {
			return;
		}
		e.setCancelled(true);
		
		// Not yet started
		if (stage == 0) {
			// In stage 0, if item != null, it's the start button
			startGame();
		}
		
		else if (stage == 1) {
			if (item.getType().equals(Material.BEE_NEST)) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, ERROR);
				endGame();
			}
			else {
				p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
				NBTItem nbti = new NBTItem(item);
				StoredItemInstance drop = itemsAfter.get(nbti.getInteger("tick"));
				StorageManager.givePlayer(p, drop.getItem().getID(), drop.getAmount());
				dropsPast++;
				ItemStack[] contents = inv.getContents();
				contents[e.getRawSlot()] = null;
				inv.setContents(contents);
				if (dropsPast == drops.size()) {
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
					endGame();
				}
			}
		}
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	private void startGame() {
		stage = 1;
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, 1.0F);
		ItemStack[] contents = new ItemStack[54];
		inv.setContents(contents);
		items = new HashMap<Integer, StoredItemInstance>();
		itemsAfter = new HashMap<Integer, StoredItemInstance>();
		ThreadLocalRandom.current().ints(0, TOTAL_TICKS).distinct().limit(drops.size()).forEach(num -> {
			int key = num * TICK_RATE;
			items.put(key, drops.get(dropNum));
			itemsAfter.put(key, drops.get(dropNum));
			dropNum++;
		});
		
		new BukkitRunnable() {
			int tick = 0;
			public void run() {
				if (stage == 2) {
					this.cancel();
					return;
				}
				moveRowsDown();
				spawnRow(tick);
				tick += TICK_RATE;
				if (tick >= (TICK_RATE * TOTAL_TICKS) + (TICK_RATE * END_TICKS)) {
					this.cancel();
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, ERROR);
					endGame();
				}
			}
		}.runTaskTimer(MinigameManager.main, 0, TICK_RATE);
	}
	
	private void endGame() {
		// Make sure endgame doesn't happen twice
		stage = 2;
		ItemStack[] contents = new ItemStack[54];
		for (int i = 0; i < 54; i++) {
			contents[i] = generateGameOver();
		}
		inv.setContents(contents);
		
		new BukkitRunnable() {
			public void run() {
				if (p.getOpenInventory().getTopInventory() == inv) {
					p.closeInventory();
				}
			}
		}.runTaskLater(main, 40);
	}

	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		ProfessionInventory thisInv = this;
		if (stage < 2) {
			new BukkitRunnable() {
				public void run() {
					p.openInventory(inv);
					Professions.viewingInventory.put(p, thisInv);
				}
			}.runTask(MinigameManager.main);
		}
	}
	
	private void moveRowsDown() {
		ItemStack[] contents = inv.getContents();
		Iterator<FallingItem> iter = falling.iterator();
		while (iter.hasNext()) {
			FallingItem fi = iter.next();
			if (fi.pos >= 54 || contents[fi.pos] == null) {
				if (fi.pos >= 54 && new NBTItem(fi.item).hasKey("tick")) {
					dropsPast++;
				}
				iter.remove();
			}
			else {
				fi.moveDown(contents);
			}
		}
		if (dropsPast == drops.size()) {
			endGame();
			return;
		}
		inv.setContents(contents);
	}
	
	private void spawnRow(int tick) {
		if (stage == 2) {
			return;
		}
		double spawnChance = this.baseSpawnChance;
		spawnChance -= Professions.gen.nextDouble();
		while (spawnChance >= 0 && toSpawn.size() < 9) {
			toSpawn.add(generateBeehive());
			spawnChance -= Professions.gen.nextDouble();
		}
		
		int numToSpawn = toSpawn.size();
		numToSpawn += items.containsKey(tick) ? 1 : 0;
		ItemStack[] contents = inv.getContents();
		ThreadLocalRandom.current().ints(0, 9).distinct().limit(numToSpawn).forEach(num -> {
			ItemStack item;
			if (items.containsKey(tick)) {
				item = generateDrop(items.get(tick), tick);
				items.remove(tick);
			}
			else {
				item = toSpawn.remove(0);
			}
			falling.add(new FallingItem(num, item, contents));
		});
		inv.setContents(contents);
	}
	
	private class FallingItem {
		int pos;
		ItemStack item;
		public FallingItem(int startPos, ItemStack item, ItemStack[] contents) {
			this.pos = startPos;
			this.item = item;
			contents[this.pos] = item;
		}
		
		public void moveDown(ItemStack[] contents) {
			contents[pos] = null;
			pos = pos + 9;
			if (pos < 54) {
				contents[pos] = item;
			}
		}
	}
}
