package me.neoblade298.mlmcgift;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	Set<Player> awaitingConfirmation = new HashSet<Player>();

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("MLMCGift Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("gift").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("MLMCGift Disabled");
		super.onDisable();
	}

	public void sendItem(Player player, String receiver) {
		if(player.getServer().getPlayer(receiver) == null) {
			player.sendMessage("§4[§c§lMLMC§4] §7" + receiver + " unavailable.");
			return;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		player.getInventory().setItemInMainHand(null);
		player.getServer().getPlayer(receiver).getInventory().setItemInMainHand(item);
	}
}