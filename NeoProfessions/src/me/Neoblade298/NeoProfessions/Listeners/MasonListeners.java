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
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();
	HashMap<Player, Integer> slotNum = new HashMap<Player, Integer>();

	Main main;
	public MasonListeners(Main main) {
		this.main = main;
	}
	
	public void prepItemSlot(Player p, ItemStack item, int slot) {
		slotItem.put(p, item);
		slotNum.put(p, slot);
		Util.sendMessage(p, "&7Hold the item you wish to slot and right click!");
		
		// Time out the repair in 10 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
		  public void run() {
		  	if(slotItem.containsKey(p)) {
		  		slotItem.remove(p);
		  		slotNum.remove(p);
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
			
			int slot = slotNum.get(p);
			ItemStack itemWithSlot = slotItem.get(p);
			ItemStack itemToSlot = p.getInventory().getItemInMainHand();
			String slotType = MasonUtils.slotType(itemToSlot);
			if(p.getInventory().containsAtLeast(itemWithSlot, 1)) {
				if(slotType != null) {
					boolean success = false;
					switch (slotType) {
					case "durability":
						success = MasonUtils.parseDurability(itemWithSlot, itemToSlot, slot);
						break;
						/*
					case "attribute":
						success = MasonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
						break;
					case "overload":
						success = MasonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
						break;
					case "advancedexp":
						success = MasonUtils.parseAdvancedExp(itemWithSlot, itemToSlot, slot);
						break;
					case "advancedgold":
						success = MasonUtils.parseAdvancedGold(itemWithSlot, itemToSlot, slot);
						break;
					case "advanceddrop":
						success = MasonUtils.parseAdvancedDrop(itemWithSlot, itemToSlot, slot);
						break;
					case "exp":
						success = MasonUtils.parseExp(itemWithSlot, itemToSlot, slot);
						break;
					case "gold":
						success = MasonUtils.parseGold(itemWithSlot, itemToSlot, slot);
						break;
					case "drop":
						success = MasonUtils.parseDrop(itemWithSlot, itemToSlot, slot);
						break;
					case "traveler":
						success = MasonUtils.parseTraveler(itemWithSlot, itemToSlot, slot);
						break;
					case "recovery":
						success = MasonUtils.parseRecovery(itemWithSlot, itemToSlot, slot);
						break;
					case "hunger":
						success = MasonUtils.parseHunger(itemWithSlot, itemToSlot, slot);
						break;
					case "secondchance":
						success = MasonUtils.parseSecondChance(itemWithSlot, itemToSlot, slot);
						break;*/
					}
					if (success) {
						Util.sendMessage(p, "&cSuccessfully slotted item!");
					}
					else {
						Util.sendMessage(p, "&cFailed to slot item!");
					}
				}
				else {
					Util.sendMessage(p, "&cThis item cannot be slotted!");
				}
			}
			else {
	  		Util.sendMessage(p, "&cSomething went wrong! Please try again.");
			}
		}
	}
}
