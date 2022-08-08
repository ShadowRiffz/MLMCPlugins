package me.neoblade298.neopvp.generators;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;

public class Generator {
	private String path;
	private String key;
	private ItemStack item;
	private double amountPerPlayer;
	private World w;
	private Location loc;
	
	public Generator(ConfigurationSection cfg, File file) {
		this.key = cfg.getName();
		this.path = file.getPath();
		String mat = cfg.getString("material");
		if (mat != null) {
			this.item = new ItemStack(Material.valueOf(mat.toUpperCase()));
		}
		else {
			this.item = MythicBukkit.inst().getItemManager().getItemStack(cfg.getString("mythicitem"));
		}
		this.amountPerPlayer = cfg.getDouble("amount-per-player");
		
		String args[] = cfg.getString("location").split(" ");
		this.w = Bukkit.getWorld(args[0]);
		double x = Double.parseDouble(args[1]);
		double y = Double.parseDouble(args[2]);
		double z = Double.parseDouble(args[3]);
		this.loc = new Location(w, x, y, z);
	}
	
	public int spawnItem() {
		int amount = (int) (Bukkit.getOnlinePlayers().size() * amountPerPlayer);
		item.setAmount(amount);
		w.dropItem(loc, item);
		return amount;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getPath() {
		return path;
	}
}
