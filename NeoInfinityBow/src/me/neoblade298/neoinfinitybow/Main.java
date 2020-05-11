package me.neoblade298.neoinfinitybow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	public Main() {
	}

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInfinityBow Enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// Get command listener
		this.getCommand("neoinfinitybow").setExecutor(new Commands());
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoInfinityBow Disabled");
		super.onDisable();
	}
	
	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		if (e.getBow().getType().equals(Material.CROSSBOW) &&
				e.getEntity() instanceof Player &&
				e.getProjectile() instanceof Arrow) {
			ItemStack bow = e.getBow();
			if (bow.getEnchantmentLevel(Enchantment.ARROW_INFINITE) > 0) {
				Player p = (Player) e.getEntity();
				p.getInventory().addItem(new ItemStack(Material.ARROW));
				Arrow arrow = (Arrow) e.getProjectile();
				arrow.setPickupStatus(PickupStatus.DISALLOWED);
				return;
			}
		}
	}
}