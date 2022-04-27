package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Objects.Manager;
import me.Neoblade298.NeoProfessions.Objects.StoredItemSource;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.neoblade298.neosettings.NeoSettings;
import me.neoblade298.neosettings.objects.Settings;

public class StorageManager implements IOComponent, Listener, Manager {
	static HashMap<UUID, HashMap<Integer, Integer>> storages = new HashMap<UUID, HashMap<Integer, Integer>>();
	static HashMap<Integer, StoredItem> items = new HashMap<Integer, StoredItem>();
	static Professions main;
	static HashMap<Integer, ArrayList<StoredItemSource>> preloadedSources = new HashMap<Integer, ArrayList<StoredItemSource>>();
	static boolean itemsLoaded = false;

	public static Settings settings;
	
	public StorageManager(Professions main) {
		StorageManager.main = main;
		
		// Load in items
		reload();
		
		// Load settings
		NeoSettings nsettings = (NeoSettings) Bukkit.getPluginManager().getPlugin("NeoSettings");
		settings = nsettings.createSettings("Professions-StorageSort", main, false);
		settings.addSetting("level-priority", 1);
		settings.addSetting("rarity-priority", 2);
		settings.addSetting("amount-priority", 3);
		settings.addSetting("name-priority", 4);
		settings.addSetting("level-order", true);
		settings.addSetting("rarity-order", false);
		settings.addSetting("amount-order", false);
		settings.addSetting("name-order", true);
	}
	
	@Override
	public void reload() {
		Bukkit.getLogger().log(Level.INFO, "[NeoProfessions] Loading Storage manager...");
		items.clear();
		itemsLoaded = false;
		
		// Reload all previously loaded sources
		preloadedSources.clear();
		for (Entry<Integer, StoredItem> ent : items.entrySet()) {
			preloadedSources.put(ent.getKey(), ent.getValue().getSources());
		}
		loadItems(new File(main.getDataFolder(), "items"));
	}
	
	public static boolean givePlayer(Player p, int id, int amount) {
		if (amount > 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			int total = storage.getOrDefault(id, 0) + amount;
			storage.put(id, total);
			p.sendMessage("§a+" + amount + " §7(§f" + total + "§7) " + items.get(id).getDisplay());
			return true;
		}
		return false;
	}

	public static boolean takePlayer(Player p, int id, int amount) {
		if (amount > 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			int total = storage.getOrDefault(id, 0) - amount;
			storage.put(id, storage.get(id) - amount);
			p.sendMessage("§c-" + amount + " §7(§f" + total + "§7) " + items.get(id).getDisplay());
			return true;
		}
		return false;
	}
	
	public static boolean setPlayer(Player p, int id, int amount) {
		if (amount >= 0) {
			HashMap<Integer, Integer> storage = storages.get(p.getUniqueId());
			int total = amount;
			storage.put(id, total);
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
					if (preloadedSources.containsKey(intid)) {
						for (StoredItemSource source : preloadedSources.get(intid)) {
							item.addSource(source.getSource(), source.isMob());
						}
					}
					items.put(intid, item);
				}
			}
		}
		itemsLoaded = true;
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (storages.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		storages.put(p.getUniqueId(), items);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_items WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				items.put(rs.getInt(2), rs.getInt(3));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		if (!storages.containsKey(p.getUniqueId())) {
			return;
		}
		
		try {
			for (Entry<Integer, Integer> entry : storages.get(uuid).entrySet()) {
				stmt.addBatch("REPLACE INTO professions_items "
						+ "VALUES ('" + uuid + "', " + entry.getKey() + "," + entry.getValue() + ");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup(Statement stmt) {
		try {
			stmt.addBatch("DELETE FROM professions_items WHERE amount <= 0");
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to cleanup storage");
			e.printStackTrace();
		}
	}
	
	public static StoredItem getItem(int id) {
		return items.get(id);
	}
	
	public static HashMap<Integer, Integer> getStorage(Player p) {
		return storages.get(p.getUniqueId());
	}
	
	@EventHandler
	public void onVoucherClaim(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		ItemStack item = e.getItem();
		if (item == null || !item.getType().equals(Material.PAPER)) {
			return;
		}
		ItemStack clone = e.getItem().clone();

		Player p = e.getPlayer();
		clone.setAmount(1);
		NBTItem nbti = new NBTItem(clone);
		if (nbti.hasKey("id")) {
			int id = nbti.getInteger("id");
			int amount = nbti.getInteger("amount");
			StoredItem si = items.get(id);
			givePlayer(p, nbti.getInteger("id"), nbti.getInteger("amount"));
			p.getInventory().removeItem(clone);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
			p.sendMessage("§4[§c§lMLMC§4] §7You claimed §f" + amount + " " + si.getDisplay() + "§7!");
		}
	}
	
	@Override
	public String getComponentName() {
		return "StorageManager";
	}
	
	public static void addSource(int id, String source, boolean isMob) {
		if (itemsLoaded) {
			if (items.containsKey(id)) {
				items.get(id).addSource(source, isMob);
			}
			else {
				Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to add source " + source + " for id " + id + ".");
			}
		}
		else {
			ArrayList<StoredItemSource> idSources = preloadedSources.getOrDefault(id, new ArrayList<StoredItemSource>());
			idSources.add(new StoredItemSource(source, isMob));
			preloadedSources.put(id, idSources);
		}
	}
}
