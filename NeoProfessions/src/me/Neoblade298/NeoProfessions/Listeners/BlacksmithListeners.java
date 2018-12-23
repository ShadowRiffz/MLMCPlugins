package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Util;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import net.milkbowl.vault.economy.Economy;

public class BlacksmithListeners implements Listener{
	HashMap<Player, ItemStack> selectedRepair = new HashMap<Player, ItemStack>();
	
	Main main;
	public BlacksmithListeners(Main main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		
		// If player has not selected a repair, select it
		if(!selectedRepair.containsKey(p)) {
			if(BlacksmithItems.isRepairItem(p.getInventory().getItemInMainHand())) {
				selectedRepair.put(p, p.getInventory().getItemInMainHand());
	  		Util.sendMessage(p, "&7Hold the item you wish to repair in your main hand and right click!");
				
				// Time out the repair in 10 seconds
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				  public void run() {
				  	if(selectedRepair.containsKey(p)) {
				  		selectedRepair.remove(p);
				  		Util.sendMessage(p, "&cRepair timed out");
				  	}
				  }
				}, 60L);
			}
		}
		
		// If a repair is selected, then complete the repair
		else {
			ItemStack repair = selectedRepair.get(p);
			ItemStack item = p.getInventory().getItemInMainHand();
			int repairLevel = BlacksmithItems.getItemLevel(repair);
			int potency = BlacksmithItems.getItemPotency(repair);
			selectedRepair.remove(p);
			Util.sendMessage(p, "&7Successfully repaired item!");
		}
	}
}
