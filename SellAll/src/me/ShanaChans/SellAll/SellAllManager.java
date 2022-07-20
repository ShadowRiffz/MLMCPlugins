package me.ShanaChans.SellAll;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBTItem;
import me.ShanaChans.SellAll.Commands.SellAllCap;
import me.ShanaChans.SellAll.Commands.SellAllCommand;
import me.ShanaChans.SellAll.Commands.SellAllGive;
import me.ShanaChans.SellAll.Commands.SellAllReload;
import me.ShanaChans.SellAll.Commands.SellAllSet;
import me.ShanaChans.SellAll.Commands.SellAllValue;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.scheduler.ScheduleInterval;
import me.neoblade298.neocore.scheduler.SchedulerAPI;

public class SellAllManager extends JavaPlugin implements Listener, IOComponent {
	private static HashMap<Material, Double> itemPrices = new HashMap<Material, Double>();
	private static HashMap<Material, Integer> itemCaps = new HashMap<Material, Integer>();
	private static HashMap<UUID, SellAllPlayer> players = new HashMap<UUID, SellAllPlayer>();
	private static TreeMap<Double, String> permMultipliers = new TreeMap<Double, String>();
	private static YamlConfiguration cfg;
	private static SellAllManager inst;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("SellAll Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		initCommands();
		loadConfigs();
		SchedulerAPI.scheduleRepeating("sellall-resetcaps", ScheduleInterval.DAILY, new Runnable() {
		    public void run() {
		        resetPlayers();
		    }
		});
		inst = this;
		NeoCore.registerIOComponent(this, this);
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("SellAll Disabled");
		super.onDisable();
	}

	private void initCommands() {
		CommandManager sellAll = new CommandManager("sellall", this);
		CommandManager value = new CommandManager("value", this);
		sellAll.register(new SellAllCommand());
		sellAll.register(new SellAllCap());
		sellAll.register(new SellAllSet());
		sellAll.register(new SellAllGive());
		sellAll.register(new SellAllReload());
		value.register(new SellAllValue());
		sellAll.registerCommandList("help");
		this.getCommand("sellall").setExecutor(sellAll);
		this.getCommand("value").setExecutor(value);
	}

	public void loadConfigs() 
	{
		itemPrices.clear();
		itemCaps.clear();
		permMultipliers.clear();
		File cfg = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!cfg.exists()) {
			saveResource("config.yml", false);
		}

		SellAllManager.cfg = YamlConfiguration.loadConfiguration(cfg);
		ConfigurationSection sec = SellAllManager.cfg.getConfigurationSection("price-list");

		for (String key : sec.getKeys(false)) {
			try {
				if (Material.valueOf(key) == null) {
					Bukkit.getLogger().warning("Item failed to load: " + key);
				}
				else 
				{
					itemPrices.put(Material.valueOf(key), sec.getDouble(key));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		sec = SellAllManager.cfg.getConfigurationSection("item-cap");
		
		for(Material mat : itemPrices.keySet())
		{
			if(sec.contains(mat.name()))
			{
				itemCaps.put(mat, sec.getInt(mat.name()));
			}
			else
			{
				itemCaps.put(mat, 100);
			}
		}
		
		sec = SellAllManager.cfg.getConfigurationSection("multipliers");
		
		for (String key : sec.getKeys(false)) 
		{
			key.replaceAll("-", ".");
			permMultipliers.put(sec.getDouble(key), key);
		}
	}

	@EventHandler
	public void rightClick(PlayerInteractEvent e) 
	{
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND
				&& e.getClickedBlock().getType() == Material.CHEST && e.getItem() != null) 
		{
			NBTItem heldItem = new NBTItem(e.getItem());
			if (heldItem.hasKey("sellStick")) {
				e.setCancelled(true);
				Chest chest = (Chest) e.getClickedBlock().getState();
				Inventory inv = chest.getInventory();
				SellAllManager.getPlayers().get(player.getUniqueId()).sellAll(inv, player);
			}
		}
	}

	public static HashMap<UUID, SellAllPlayer> getPlayers() {
		return players;
	}

	public static HashMap<Material, Double> getItemPrices() {
		return itemPrices;
	}
	
	public static HashMap<Material, Integer> getItemCaps() {
		return itemCaps;
	}
	
	public static double getMultiplier(Player p) 
	{
		Iterator<Double> iter = permMultipliers.descendingKeySet().iterator();
		while (iter.hasNext()) 
		{
			double mult = iter.next();
			String perm = permMultipliers.get(mult);
			if (p.hasPermission(perm)) 
			{
				return mult;	
			}
		}
		return 1.0;
	}
    
    public void resetPlayers()
    {
    	Statement stmt = NeoCore.getStatement();
    	
    	try {
			stmt.executeUpdate("DELETE FROM sellall_players;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	for(UUID uuid : players.keySet())
    	{
    		players.get(uuid).resetSold();
    	}
    }

	@Override
	public void cleanup(Statement arg0, Statement arg1) {}

	@Override
	public String getKey() {
		return "SellAllManager";
	}

	@Override
	public void loadPlayer(Player arg0, Statement arg1) {}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {
		if (!players.containsKey(p.getUniqueId())) {
			HashMap<Material, Integer> sold = new HashMap<Material, Integer>();
			
			try {
				ResultSet rs = stmt.executeQuery("SELECT * FROM sellall_players WHERE uuid = '" + p.getUniqueId() + "';");
				while (rs.next()) {
					Material key = Material.valueOf(rs.getString(2));
					int value = rs.getInt(3);
					sold.put(key, value);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			players.put(p.getUniqueId(), new SellAllPlayer(sold));
		}
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		if (players.containsKey(p.getUniqueId())) {
			HashMap<Material, Integer> sold = players.get(p.getUniqueId()).getItemAmountSold();
			try {
				for (Entry<Material, Integer> e : sold.entrySet()) {
						insert.addBatch("INSERT INTO sellall_players VALUES ('" + p.getUniqueId() + "','"
								+ e.getKey() + "'," + e.getValue() + ");");
				}
				insert.executeBatch();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void getValue(Player p)
	{
		ItemStack item = p.getInventory().getItemInMainHand();
        
        if (item == null || item.getType().isAir()) 
        {
            p.sendMessage("§6You're not holding anything!");
            return;
        }
        
        NBTItem nbti = new NBTItem(item);
        double value = 0;
        
        if (!nbti.getString("value").isBlank()) 
        {
            value = Double.parseDouble(nbti.getString("value"));
        }
        else 
        {
            value = nbti.getDouble("value");
        }
        
        String name = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
        
        if(value == 0)
        {
        	if(itemPrices.containsKey(item.getType()))
        	{
        		name = item.getType().name();
        		value = itemPrices.get(item.getType());
        	}
        	else
        	{
        		p.sendMessage("§6This item does not have a price!");
        		return;
        	}
        }
        
        p.sendMessage("§6Value of §7" + name + "§7: §e" + value + "g");
	}
	
	public static SellAllManager inst()
	{
		return inst;
	}
}
