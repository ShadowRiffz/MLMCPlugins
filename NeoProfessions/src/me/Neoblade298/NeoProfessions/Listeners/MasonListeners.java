package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities.BlacksmithUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();

	Main main;
	public MasonListeners(Main main) {
		this.main = main;
	}
	
	public void prepItemSlot(Player p, ItemStack item) {
		slotItem.put(p, item);
		Util.sendMessage(p, "&7Hold the item you wish to slot and right click!");
		
		// Time out the repair in 10 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
		  public void run() {
		  	if(slotItem.containsKey(p)) {
		  		slotItem.remove(p);
		  		Util.sendMessage(p, "&cSlot command timed out");
		  	}
		  }
		}, 200L);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer() == null || !(e.getPlayer() instanceof Player)) {
			return;
		}
		Player p = e.getPlayer();
		
		if(slotItem.containsKey(p)) {

			ItemStack itemWithSlot = slotItem.get(p);
			ItemStack itemToSlot = p.getInventory().getItemInMainHand();
		}
	}
}
