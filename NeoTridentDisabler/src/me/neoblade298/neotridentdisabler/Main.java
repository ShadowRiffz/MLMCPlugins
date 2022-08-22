package me.neoblade298.neotridentdisabler;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoTridentDisabler Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	}
  
	public void onDisable() {
	  Bukkit.getServer().getLogger().info("NeoTridentDisabler Disabled");
    	super.onDisable();
  	}
  
  	@EventHandler
  	public void onEnchant(EnchantItemEvent e) {
  		Map<Enchantment, Integer> enchs = e.getEnchantsToAdd();
  		if (enchs.containsKey(Enchantment.RIPTIDE)) {
  			e.getEnchanter().sendMessage("§4[§c§lMLMC§4] §7The §eRiptide §7enchantment is disabled.");
  			e.setCancelled(true);
  		}
  		if (enchs.containsKey(Enchantment.CHANNELING)) {
  			e.getEnchanter().sendMessage("§4[§c§lMLMC§4] §7The §eChanneling §7enchantment is disabled.");
  			e.setCancelled(true);
  		}
  	}
  	
  	@EventHandler
  	public void onAnvilPrep(PrepareAnvilEvent e) {
  		if (e.getResult() == null) return;
  		if (e.getResult().containsEnchantment(Enchantment.RIPTIDE)) {
  			e.getViewers().get(0).sendMessage("§4[§c§lMLMC§4] §7The §eRiptide §7enchantment is disabled.");
  			e.getViewers().get(0).closeInventory();
  		}
  		if (e.getResult().containsEnchantment(Enchantment.CHANNELING)) {
  			e.getViewers().get(0).sendMessage("§4[§c§lMLMC§4] §7The §eChanneling §7enchantment is disabled.");
  			e.getViewers().get(0).closeInventory();
  		}
  	}
  	
  	@EventHandler
  	public void changeHeld(PlayerItemHeldEvent e) {
  		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
  		Map<Enchantment, Integer> enchs = item.getEnchantments();
  		if (enchs.containsKey(Enchantment.RIPTIDE)) {
  			e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7The §eRiptide §7enchantment is disabled.");
  			item.removeEnchantment(Enchantment.RIPTIDE);
  		}
  		if (enchs.containsKey(Enchantment.CHANNELING)) {
  			e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7The §eChanneling §7enchantment is disabled.");
  			item.removeEnchantment(Enchantment.CHANNELING);
  		}
  	}
}