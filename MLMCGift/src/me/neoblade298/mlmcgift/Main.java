package me.neoblade298.mlmcgift;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	GiftRequestSet requests = new GiftRequestSet();
	YamlConfiguration conf;
	int price;
	int timeout;

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("MLMCGift Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("gift").setExecutor(new Commands(this));

	    File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) {
	      saveResource("config.yml", false);
	    }
	  	conf = YamlConfiguration.loadConfiguration(file);
	  	price = conf.getInt("price", 0);
	  	timeout = conf.getInt("timeout", 10);
	  	// Bukkit.getServer().getLogger().info("MLMCGift Config Loaded: Price = " + price);
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("MLMCGift Disabled");
		super.onDisable();
	}
	
	public void sendRequest(Player player, String receiver) {
		if(player.getServer().getPlayer(receiver) == null) { // usually because receiver not online
			player.sendMessage("§4[§c§lMLMC§4] §7" + receiver + " unavailable.");
			return;
		}
		
		if(player.getServer().getPlayer(receiver).getInventory().firstEmpty() == -1) { // no space in inventory
			player.sendMessage("§4[§c§lMLMC§4] §7" + player.getServer().getPlayer(receiver).getName() + " cannot accept items.");
			return;
		}
		
		if(requests.containsReceiver(player.getServer().getPlayer(receiver))) { // avoids multiple requests at once
			player.sendMessage("§4[§c§lMLMC§4] §7" + receiver + " already has a request!");
			return;
		}

		ItemStack item = player.getInventory().getItemInMainHand();
		player.getInventory().setItemInMainHand(null);
		
		player.sendMessage("§4[§c§lMLMC§4] §7Request sent to " + player.getServer().getPlayer(receiver).getName());
		player.getServer().getPlayer(receiver).sendMessage("§4[§c§lMLMC§4] §7" + player.getName() + " is requesting to gift you."); //§r" + item.getAmount() + " " + item.getType() + ".");
		
		GiftRequest request = new GiftRequest(player, player.getServer().getPlayer(receiver), item); 
		requests.add(request);
		
		new BukkitRunnable() {
			public void run() {
				if(requests.containsReceiver(player.getServer().getPlayer(receiver))) {
					player.sendMessage("§4[§c§lMLMC§4] §7" + player.getServer().getPlayer(receiver).getName() + " did not respond to your request.");
					requests.remove(request);
					player.getInventory().addItem(item);
				}
			}
		}.runTaskLater(this, 20L * timeout); // auto-deny after timeout
	}
	
	public void acceptRequest(Player player) { // player is the person accepting (receiver)
		if(!requests.containsReceiver(player)) {
			player.sendMessage("§4[§c§lMLMC§4] §7You do not have a pending request!");
			return;
		}
		
		if(player.getInventory().firstEmpty() == -1) { // no space in inventory
			player.sendMessage("§4[§c§lMLMC§4] §7You cannot accept items.");
			return;
		}
		
		GiftRequest request = requests.getRequestByReceiver(player);
		
		player.getInventory().addItem(request.getItem());
		
		player.sendMessage("§4[§c§lMLMC§4] §7Gift accepted!");
		request.getSender().sendMessage("§4[§c§lMLMC§4] §7" + player.getName() + " has accepted the gift!");
		
		requests.remove(request);
	}
	
	public void denyRequest(Player player) { // player is the person denying (receiver)
		if(!requests.containsReceiver(player)) {
			player.sendMessage("§4[§c§lMLMC§4] §7You do not have a pending request!");
			return;
		}

		GiftRequest request = requests.getRequestByReceiver(player);
		request.getSender().getInventory().addItem(request.getItem());
		
		requests.remove(request);
		
		player.sendMessage("§4[§c§lMLMC§4] §7Request denied.");
		request.getSender().sendMessage("§4[§c§lMLMC§4] §7" + player.getName() + " has denied your request.");
	}
}