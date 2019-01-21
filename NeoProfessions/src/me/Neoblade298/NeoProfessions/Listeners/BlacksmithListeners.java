package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities.BlacksmithUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class BlacksmithListeners implements Listener{
	HashMap<Player, ItemStack> selectedRepair = new HashMap<Player, ItemStack>();
	
	Main main;
	public BlacksmithListeners(Main main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer() == null || !(e.getPlayer() instanceof Player) || !e.getAction().equals(Action.RIGHT_CLICK_AIR) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player p = e.getPlayer();
		
		
		// If player has not selected a repair, select it
		if(!selectedRepair.containsKey(p)) {
			if(BlacksmithUtils.isRepairItem(p.getInventory().getItemInMainHand())) {
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
				}, 200L);
			}
		}
		
		// If a repair is selected, then complete the repair
		else {
			e.setCancelled(true);
			ItemStack repair = selectedRepair.get(p);
			ItemStack item = p.getInventory().getItemInMainHand();
			int repairLevel = BlacksmithUtils.getItemLevel(repair);
			int potency = BlacksmithUtils.getItemPotency(repair);
			double percentage = (double) potency / 100;
			int itemLevel = Util.getItemLevel(item);
			if(itemLevel != -1) {
				if(itemLevel <= repairLevel ){
					if(p.getInventory().containsAtLeast(repair, 1)) {
						p.getInventory().removeItem(repair);
						selectedRepair.remove(p);
						Util.setCurrentDurability(item, Util.getCurrentDurability(item) + (int)(percentage * Util.getMaxDurability(item)));
						Util.sendMessage(p, "&7Successfully repaired item!");
					}
					else {
			  		Util.sendMessage(p, "&cSomething went wrong! Please try again.");
					}
				}
				else {
		  		Util.sendMessage(p, "&cThis repair kit is incompatible with this item!");
				}
			}
			else {
	  		Util.sendMessage(p, "&cRepair kits only work on quest items!");
			}
		}
	}
}
