package me.Neoblade298.NeoProfessions.Managers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class CurrencyManager implements IOComponent, Listener {
	// UUID, essence/oretype, amount
	Professions main;
	
	private static HashMap<UUID, HashMap<Integer, Integer>> essence;
	static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	private static ArrayList<String> voucherLore = new ArrayList<String>();
	
	static {
		voucherLore.add("�7�oRight click to claim!");
	}
	
	public CurrencyManager(Professions main) {
		this.main = main;
		essence = new HashMap<UUID, HashMap<Integer, Integer>>();
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (essence.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<Integer, Integer> essences = new HashMap<Integer, Integer>();
		essence.put(p.getUniqueId(), essences);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_currency WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				essences.put(rs.getInt(2), rs.getInt(3));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init currency for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		if (!essence.containsKey(p.getUniqueId())) {
			return;
		}
		
		try {
			for (Entry<Integer, Integer> entry : essence.get(uuid).entrySet()) {
				if (entry.getValue() == 0) {
					continue;
				}
				stmt.addBatch("REPLACE INTO professions_essence "
						+ "VALUES ('" + uuid + "', " + entry.getKey() + "," + entry.getValue() + ");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save currency for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static void add(Player p, int level, int amount) {
		// Standardize the level
		level -= (level % 5);
		
		if (level > 60 || level < 5) {
			return;
		}
		
		if (amount < 0) {
			subtract(p, level, -amount);
			return;
		}
		
		HashMap<Integer, Integer> essences = essence.get(p.getUniqueId());
		int newAmount = essences.getOrDefault(level, 0) + amount;
		essences.put(level, newAmount);
		Util.sendMessage(p, "&a+" + amount + " &7(�f" + newAmount + "�7) �6Lv " + level + " �7Essence.");
	}
	
	public static void subtract(Player p, int level, int amount) {
		HashMap<Integer, Integer> essences = essence.get(p.getUniqueId());
		int newAmount = essences.getOrDefault(level, 0) - amount;
		essences.put(level, newAmount);
		Util.sendMessage(p, "&c-" + amount + " &7(�f" + newAmount + "�7) �6Lv " + level + " �7Essence.");
	}
	
	public static void set(Player p, int level, int amount) {
		HashMap<Integer, Integer> essences = essence.get(p.getUniqueId());
		essences.put(level, amount);
	}
	
	public static int get(Player p, int level) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return -1;
		}
		return essence.get(p.getUniqueId()).getOrDefault(level, 0);
	}
	
	public static boolean hasEnough(Player p, int level, int compare) {
		if (!(level <= 60 && level > 0 && level % 5 == 0)) {
			return false;
		}
		HashMap<Integer, Integer> pCurrency = essence.get(p.getUniqueId());
		return pCurrency.getOrDefault(level, 0) >= compare;
	}

	public static boolean giveVoucher(Player p, int level, int amount) {
		if (CurrencyManager.hasEnough(p, level, amount)) {
			p.sendMessage("�4[�c�lMLMC�4] �cNot enough items to create the voucher!");
			return false;
		}
		
		StorageManager.takePlayer(p, level, amount);
		p.getInventory().addItem(getVoucher(level, amount));
		return true;
	}
	
	private static ItemStack getVoucher(int level, int amount) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�6[Lv " + level + "] �7Essence �fx" + amount);
		meta.setLore(voucherLore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "essence");
		nbti.setInteger("level", level);
		nbti.setInteger("amount", amount);
		return nbti.getItem();
	}
	
	
	@EventHandler
	public void onVoucherClaim(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		ItemStack item = e.getItem().clone();
		if (item == null || !item.getType().equals(Material.PAPER)) {
			return;
		}

		Player p = e.getPlayer();
		item.setAmount(1);
		NBTItem nbti = new NBTItem(item);
		if (nbti.getString("type").equals("essence")) {
			int level = nbti.getInteger("level");
			int amount = nbti.getInteger("amount");
			p.getInventory().removeItem(item);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
			p.sendMessage("�4[�c�lMLMC�4] �7You claimed �f" + amount + " �6Lv " + level + "�7 Essence!");
			CurrencyManager.add(p, level, amount);
		}
	}
}