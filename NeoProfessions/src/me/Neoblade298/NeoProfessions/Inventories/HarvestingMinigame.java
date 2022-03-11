package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Minigames.MinigameDrop;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class HarvestingMinigame extends ProfessionInventory {
	private static float NOTE1 = 0.5F, NOTE2 = 0.629961F, NOTE3 = 0.749154F, NOTE4 = 1.0F, ERROR = 0.594604F;
	Professions main;
	Player p;
	int stage = 0;
	ArrayList<MinigameDrop> drops;
	int totalDrops;
	int difficulty;
	HashMap<Integer, MinigameDrop> hiddenItems;
	HashSet<Integer> guesses;
	
	private int dropNum = 0;
	
	public HarvestingMinigame(Professions main, Player p, ArrayList<MinigameDrop> drops, String name, int totalDrops, int difficulty) {
		this.main = main;
		this.p = p;
		this.totalDrops = totalDrops;
		this.drops = drops;
		this.difficulty = difficulty;
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
		lore.add("§e" + this.totalDrops + " §7items will flash onscreen,");
		lore.add("§7then be covered by grey glass. Remember");
		lore.add("§7where they were and click the glass");
		lore.add("§7to retrieve the items!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateCover() {
		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aClick to reveal!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7If an item was under this pane,");
		lore.add("§7you'll receive it!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateEmpty() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cNothing under here :(");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack generateDrop(MinigameDrop drop) {
		ItemStack item = new ItemStack(drop.getItem().getRarity().getMaterial());
		StoredItem sitem = drop.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(sitem.getDisplay() + " x" + drop.getAmt());
		ArrayList<String> lore = sitem.getLore();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
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
			initGame();
		}
		
		else if (stage == 2) {
			int slot = e.getRawSlot();
			if (!guesses.contains(slot)) {
				guesses.add(slot);
				ItemStack[] contents = inv.getContents();
				if (hiddenItems.containsKey(slot)) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
					MinigameDrop drop = hiddenItems.get(slot);
					contents[slot] = generateDrop(drop);
					StorageManager.givePlayer(p, drop.getItem().getID(), drop.getAmt());
				}
				else {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, ERROR);
					contents[slot] = generateEmpty();
				}
				inv.setContents(contents);
				
				// If you're out of guesses, close the inventory
				if (guesses.size() >= totalDrops) {
					stage = 3;
					endGame();
					new BukkitRunnable() {
						public void run() {
							if (p.getOpenInventory().getTopInventory() == inv) {
								p.closeInventory();
							}
						}
					}.runTaskLater(main, 40);
				}
			}
		}
	}

	@Override
	public void handleInventoryDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	private void initGame() {
		int timeToMemorize = 60 - (4 * this.difficulty);
		int segment = timeToMemorize / 4;
		stage = 1;
		ItemStack[] contents = new ItemStack[54];
		hiddenItems = new HashMap<Integer, MinigameDrop>();
		ThreadLocalRandom.current().ints(0, 54).distinct().limit(totalDrops).forEach(num -> {
			contents[num] = generateDrop(drops.get(dropNum));
			hiddenItems.put(num, drops.get(dropNum));
			dropNum++;
		});
		inv.setContents(contents);
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, NOTE1);
		
		new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, NOTE2);
			}
		}.runTaskLater(main, segment);
		
		new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, NOTE3);
			}
		}.runTaskLater(main, segment * 2);
		
		new BukkitRunnable() {
			public void run() {
				startGame();
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, NOTE4);
			}
		}.runTaskLater(main, segment * 3);
	}
	
	private void startGame() {
		ItemStack[] contents = new ItemStack[54];
		for (int i = 0; i < 54; i++) {
			contents[i] = generateCover();
		}
		inv.setContents(contents);
		stage = 2;
		guesses = new HashSet<Integer>();
	}
	
	private void endGame() {
		ItemStack[] contents = new ItemStack[54];
		for (Integer place : hiddenItems.keySet()) {
			contents[place] = generateDrop(hiddenItems.get(place));
		}
		inv.setContents(contents);
	}

	@Override
	public void handleInventoryClose(InventoryCloseEvent e) {
		
	}
}
