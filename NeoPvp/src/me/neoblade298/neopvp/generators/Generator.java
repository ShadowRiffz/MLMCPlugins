package me.neoblade298.neopvp.generators;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Generator {
	private ItemStack item;
	private double amountPerPlayer;
	private Location loc;
	
	public void spawnItem() {
		item.setAmount((int) (Bukkit.getOnlinePlayers().size() * amountPerPlayer));
		loc.getWorld().dropItem(loc, item);
	}
}
