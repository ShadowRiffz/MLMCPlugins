package im.hoss.OldEnchanting;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class OldEnchanting extends JavaPlugin implements Listener {
	FileConfiguration config = getConfig();

	public void onEnable() {
		FileConfiguration config = getConfig();
		config.addDefault("FreeLapis", Boolean.valueOf(true));
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("OldEnchanting Enabled!");

		config.options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable() {
		getLogger().info("OldEnchanting Disabled!");
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		if (e.isCancelled()) {
			return;
		}
		Map<Enchantment, Integer> enchs = e.getEnchantsToAdd();
		if (enchs.containsKey(Enchantment.RIPTIDE) || enchs.containsKey(Enchantment.CHANNELING)) {
			return;
		}
		e.getEnchanter().setLevel(e.getEnchanter().getLevel() - e.getExpLevelCost() + (e.whichButton() + 1));
		e.setExpLevelCost(1);
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		if (e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
			if (this.config.getBoolean("FreeLapis")) {
				EnchantingInventory en = (EnchantingInventory) e.getInventory();
				en.setSecondary(new ItemStack(Material.LAPIS_LAZULI, 64));
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getType().equals(InventoryType.ENCHANTING)) {
			event.getInventory().setItem(1, null);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getInventory().getType().equals(InventoryType.ENCHANTING)) {
			if (event.getRawSlot() == 1) {
				event.setCancelled(true);
			}
		}
	}
}
