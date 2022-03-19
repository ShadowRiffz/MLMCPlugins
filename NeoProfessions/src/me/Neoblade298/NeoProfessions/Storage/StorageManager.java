package me.Neoblade298.NeoProfessions.Storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;

public class StorageManager implements Listener {
	static HashMap<UUID, HashMap<Integer, Integer>> storages = new HashMap<UUID, HashMap<Integer, Integer>>();
	static HashMap<Integer, StoredItem> items = new HashMap<Integer, StoredItem>();
	static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	static Professions main;
	
	public StorageManager(Professions main) {
		StorageManager.main = main;
		
		// Load in items
		loadItems(new File(main.getDataFolder(), "items"));
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			loadPlayer(p);
		}
	}
	
	public static boolean givePlayer(Player p, int id, int amount) {
		if (amount > 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			int total = storage.getOrDefault(id, 0) + amount;
			storage.put(id, total);
			p.sendMessage("§4[§c§lMLMC§4] §7You now have §e" + total + " " + items.get(id).getDisplay());
			return true;
		}
		return false;
	}

	public static boolean takePlayer(Player p, int id, int amount) {
		if (amount > 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			int total = storage.getOrDefault(id, 0) - amount;
			if (total <= 0) {
				storage.remove(id);
			}
			else {
				storage.put(id, storage.get(id) - amount);
			}
			p.sendMessage("§4[§c§lMLMC§4] §7You now have §e" + total + " " + items.get(id).getDisplay());
			return true;
		}
		return false;
	}
	
	public static boolean playerHas(Player p, int id, int amount) {
		if (amount > 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			return storage.getOrDefault(id, 0) >= amount;
		}
		return false;
	}
	
	public static int getAmount(Player p, int id) {
		HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
		return storage.getOrDefault(id, 0);
	}
	
	private void loadItems(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadItems(file);
			}
			else {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
				for (String id : yaml.getKeys(false)) {
					ConfigurationSection itemCfg = yaml.getConfigurationSection(id);
					String name = itemCfg.getString("name");
					String mat = itemCfg.getString("material");
					int level = itemCfg.getInt("level", 0);
					String rarity = itemCfg.getString("rarity", "common");
					ArrayList<String> lore = (ArrayList<String>) itemCfg.getStringList("lore");
					int intid = Integer.parseInt(id);
					StoredItem item = new StoredItem(intid, name, level, rarity, mat, lore);
					items.put(intid, item);
				}
			}
		}
	}
	
	public static void loadPlayer(OfflinePlayer p) {
		// Check if player exists already
		if (storages.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		storages.put(p.getUniqueId(), items);
		
		// Check if player exists on SQL
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM professions_items WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				items.put(rs.getInt(2), rs.getInt(3));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void savePlayer(Player p, Connection con, Statement stmt, boolean savingMultiple) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		try {
			for (Entry<Integer, Integer> entry : storages.get(uuid).entrySet()) {
				if (entry.getValue() == 0) {
					continue;
				}
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + entry.getKey() + "'," + entry.getValue() + ");");
			}
			
			// Set to true if you're saving several accounts at once
			if (!savingMultiple) {
					stmt.executeBatch();
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				savePlayer(p, con, stmt, true);
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();

		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				if (storages.containsKey(uuid)) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
						Statement stmt = con.createStatement();

						// Save account
						savePlayer(p, con, stmt, false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		save.runTaskAsynchronously(main);
	}
	
	public static HashMap<Integer, StoredItem> getItemDefinitions() {
		return items;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(e.getUniqueId());
		loadPlayer(p);
	}
	
	@EventHandler
	public void onVoucherClaim(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		ItemStack item = e.getItem().clone();
		if (item == null || item.getType().equals(Material.PAPER)) {
			return;
		}
		
		Player p = e.getPlayer();
		item.setAmount(1);
		NBTItem nbti = new NBTItem(item);
		if (nbti.hasKey("id")) {
			int id = nbti.getInteger("id");
			int amount = nbti.getInteger("amount");
			StoredItem si = items.get(id);
			givePlayer(p, nbti.getInteger("id"), nbti.getInteger("amount"));
			p.getInventory().removeItem(item);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
			p.sendMessage("§4[§c§lMLMC§4] §7You claimed §f" + amount + " " + si.getDisplay() + "§7!");
		}
	}
}
