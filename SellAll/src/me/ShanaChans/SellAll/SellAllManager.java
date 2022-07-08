package me.ShanaChans.SellAll;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBTItem;
import me.ShanaChans.SellAll.Commands.SellAllCap;
import me.ShanaChans.SellAll.Commands.SellAllCommand;
import me.ShanaChans.SellAll.Commands.SellAllGive;
import me.ShanaChans.SellAll.Commands.SellAllSet;
import me.neoblade298.neocore.commands.CommandManager;


public class SellAllManager extends JavaPlugin implements Listener
{
	private static HashMap<Material, Double> itemPrices = new HashMap<Material, Double>();
	private static HashMap<UUID, SellAllPlayer> players = new HashMap<UUID, SellAllPlayer>();
	private YamlConfiguration cfg;
	
	public void onEnable() 
	{
        Bukkit.getServer().getLogger().info("SellAll Enabled");
        getServer().getPluginManager().registerEvents(this, this);
        initCommands();
        loadConfigs();  
    }
	
    public void onDisable() 
    {
        org.bukkit.Bukkit.getServer().getLogger().info("SellAll Disabled");
        super.onDisable();
    }
    
    private void initCommands()
    {
    	CommandManager sellAll = new CommandManager("sellall", this);
    	sellAll.register(new SellAllCommand());
    	sellAll.register(new SellAllCap());
    	sellAll.register(new SellAllSet());
    	sellAll.register(new SellAllGive());
    	sellAll.registerCommandList("help");
    	this.getCommand("sellall").setExecutor(sellAll);
    }
    
    public void loadConfigs() 
    {
		File cfg = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!cfg.exists()) {
			saveResource("config.yml", false);
		}
		
		this.cfg = YamlConfiguration.loadConfiguration(cfg);
		ConfigurationSection sec = this.cfg.getConfigurationSection("pricelist");
		
		for (String key : sec.getKeys(false))
		{
			if(Material.valueOf(key) == null)
			{
				Bukkit.getLogger().warning("Item failed to load: " + key);
			}
			else
			{
				itemPrices.put(Material.valueOf(key), sec.getDouble(key));
			}
		}
		
	}
    
    @EventHandler
    public void rightClick(PlayerInteractEvent e) 
    {
    	Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.getClickedBlock().getType() == Material.CHEST &&  e.getItem() != null)
        {
        	NBTItem heldItem = new NBTItem(e.getItem());
        	if(heldItem.hasKey("sellStick"))
        	{
        		e.setCancelled(true);
        		Chest chest = (Chest) e.getClickedBlock().getState();
        		Inventory inv = chest.getInventory();
        		SellAllManager.getPlayers().get(player.getUniqueId()).sellAll(inv, player);
        		
        	}
        }
    }
    
    @EventHandler
    public void join(PlayerJoinEvent e) 
    {
       if(!e.getPlayer().hasPlayedBefore() || !players.containsKey(e.getPlayer().getUniqueId()))
       {
    	   players.put(e.getPlayer().getUniqueId(), new SellAllPlayer());
       }
    }
    
    public static HashMap<UUID, SellAllPlayer> getPlayers() 
    {
		return players;
	}
    
    public static HashMap<Material, Double> getItemPrices() 
    {
		return itemPrices;
	}

}
