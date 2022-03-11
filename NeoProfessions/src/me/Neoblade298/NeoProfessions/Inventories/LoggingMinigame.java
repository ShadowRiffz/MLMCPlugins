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
import me.Neoblade298.NeoProfessions.Minigames.MinigameDrop;
import me.Neoblade298.NeoProfessions.Minigames.MinigameManager;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class LoggingMinigame extends ProfessionInventory {
	private static int TICK_RATE = 8;
	private static int TOTAL_TICKS = 25;
	private static int END_TICKS = 6;
	Professions main;
	Player p;
	int stage = 0;
	ArrayList<MinigameDrop> drops;
	ArrayList<FallingItem> falling = new ArrayList<FallingItem>();
	int totalDrops;
	double baseSpawnChance = 0.2;
	int difficulty;
	HashMap<Integer, MinigameDrop> items, itemsAfter; // Need 2 because 1 gets its items removed
	ArrayList<ItemStack> toSpawn = new ArrayList<ItemStack>();
	int dropsAcquired = 0;
	
	private int dropNum = 0;
	
	public LoggingMinigame(Professions main, Player p, ArrayList<MinigameDrop> drops, String name, int totalDrops, int difficulty) {
		this.main = main;
		this.p = p;
		this.totalDrops = totalDrops;
		this.drops = drops;
		this.difficulty = difficulty;
		baseSpawnChance += difficulty * 0.1;
		inv = Bukkit.createInventory(p, 54, name.replaceAll("&", "§"));
		Professions.viewingInventory.put(p, this);
		System.out.println(drops);
		
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
		lore.add("§e" + this.totalDrops + " §7items will drop from the");
		lore.add("§7top. Click them to get them, but don't");
		lore.add("§7click any of the beehives!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateBeehive() {
		ItemStack item = new ItemStack(Material.BEEHIVE);
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
	
	private ItemStack generateDrop(MinigameDrop drop, int tick) {
		ItemStack item = new ItemStack(drop.getItem().getRarity().getMaterial());
		StoredItem sitem = drop.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(sitem.getDisplay() + " x" + drop.getAmt());
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
			if (item.getType().equals(Material.BEEHIVE)) {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
				endGame();
			}
			else {
				p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
				NBTItem nbti = new NBTItem(item);
				MinigameDrop drop = itemsAfter.get(nbti.getInteger("tick"));
				StorageManager.givePlayer(p, drop.getItem().getID(), drop.getAmt());
				dropsAcquired++;
				ItemStack[] contents = inv.getContents();
				contents[e.getRawSlot()] = null;
				inv.setContents(contents);
				if (dropsAcquired == drops.size()) {
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
		items = new HashMap<Integer, MinigameDrop>();
		itemsAfter = new HashMap<Integer, MinigameDrop>();
		ThreadLocalRandom.current().ints(20, 20 + (TICK_RATE * TOTAL_TICKS)).distinct().limit(totalDrops).forEach(num -> {
			int key = num - (num % TICK_RATE);
			while (items.containsKey(key)) {
				key += TICK_RATE;
			}
			items.put(num - (num % TICK_RATE), drops.get(dropNum));
			itemsAfter.put(num - (num % TICK_RATE), drops.get(dropNum));
			dropNum++;
		});
		
		new BukkitRunnable() {
			int tick = 0;
			public void run() {
				if (stage == 2) {
					this.cancel();
					return;
				}
				tick += TICK_RATE;
				moveRowsDown();
				spawnRow(tick);
				if (tick >= 20 + (TICK_RATE * TOTAL_TICKS) + (TICK_RATE * END_TICKS)) {
					this.cancel();
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
	}

	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		
	}
	
	private void moveRowsDown() {
		ItemStack[] contents = inv.getContents();
		Iterator<FallingItem> iter = falling.iterator();
		while (iter.hasNext()) {
			FallingItem fi = iter.next();
			if (fi.pos >= 54 || contents[fi.pos] == null) {
				iter.remove();
			}
			else {
				fi.moveDown(contents);
			}
		}
		inv.setContents(contents);
	}
	
	private void spawnRow(int tick) {
		
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
			if (pos + 9 < 54) {
				pos = pos + 9;
				contents[pos] = item;
			}
		}
	}
}
