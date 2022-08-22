package me.neoblade298.neolooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	ArrayList<String> isLooting;
	HashMap<Player, Location> homes;
	Random gen;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLooter Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("loot").setExecutor(new LootCommand(this));
	    this.getCommand("home").setExecutor(new HomeCommand(this));
	    this.getCommand("sethome").setExecutor(new SethomeCommand(this));
	    homes = new HashMap<Player, Location>();
	    isLooting = new ArrayList<String>();
	    gen = new Random();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLooter Disabled");
	    super.onDisable();
	}
	
	@EventHandler
	public void onChestOpen(InventoryOpenEvent e) {
		if (!e.getInventory().getType().equals(InventoryType.CHEST) && !e.getInventory().getType().equals(InventoryType.BARREL)) {
			return;
		}
		if (!isLooting.contains(e.getPlayer().getName())) {
			return;
		}
		Inventory chest = e.getInventory();
		chest.getContents();
		
		// Count itemstacks first
		int count = 0;
		for (ItemStack item : chest.getContents()) {
			if (item != null) {
				count += item.getAmount();
				if (count > 128) {
					return;
				}
			}
		}
		if (count == 0) {
			return;
		}
		
		// Calculate online players
		int numOnline = Bukkit.getServer().getOnlinePlayers().size();
		ArrayList<Player> players = new ArrayList<Player>();
		Bukkit.broadcastMessage(e.getPlayer().getName() + " looted a chest!");
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player);
		}

		// Distribute amongst online players
		ItemStack[] contents = chest.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null && !contents[i].getType().equals(Material.AIR)) {
				int num = gen.nextInt(numOnline);
				players.get(num).getWorld().dropItem(players.get(num).getEyeLocation(), contents[i]);
				ItemStack toGive = contents[i].clone();
				contents[i] = null;
				if (toGive.hasItemMeta() && toGive.getItemMeta().hasDisplayName()) players.get(num).sendMessage("You got: " + toGive.getItemMeta().getDisplayName());
				else players.get(num).sendMessage("You got: " + toGive.getType());
			}
		}
		chest.setContents(contents);
	}
	
}
