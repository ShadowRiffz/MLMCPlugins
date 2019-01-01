package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();
	HashMap<Player, Integer> slotNum = new HashMap<Player, Integer>();
	
	// Constants
	static final int SLOT_ESSENCE = 3;
	static final int SLOT_GOLD = 2000;

	Main main;
	Economy econ;
	public MasonListeners(Main main) {
		this.main = main;
		econ = main.getEconomy();
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
			
			e.setCancelled(true);
			int slot = slotNum.get(p);
			ItemStack itemWithSlot = slotItem.get(p);
			ItemStack itemToSlot = p.getInventory().getItemInMainHand();
			slotNum.remove(p);
			slotItem.remove(p);
			
			String slotType = MasonUtils.slotType(itemToSlot);
			if(p.getInventory().containsAtLeast(itemWithSlot, 1)) {
				if(slotType != null) {
					int level = Util.getItemLevel(itemWithSlot);
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), SLOT_ESSENCE)) {
						if(econ.has(p, SLOT_GOLD)) {
							boolean success = false;
							switch (slotType) {
							case "durability":
								success = MasonUtils.parseDurability(itemWithSlot, itemToSlot, slot);
								break;
							case "attribute":
								success = MasonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
								break;
							case "overload":
								success = MasonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
								break;
							case "charm":
								success = MasonUtils.parseCharm(itemWithSlot, itemToSlot, slot);
								break;
							}
							if (success) {
								p.getInventory().removeItem(Util.setAmount(new ItemStack(itemToSlot), 1));
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), SLOT_ESSENCE));
								econ.withdrawPlayer(p, SLOT_GOLD);
								Util.sendMessage(p, "&cSuccessfully slotted item!");
							}
							else {
								Util.sendMessage(p, "&cFailed to slot item!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the gold to do this!");
							slotItem.remove(p);
							slotNum.remove(p);
						}
					}
					else {
						Util.sendMessage(p, "&cYou lack the materials to do this!");
						slotItem.remove(p);
						slotNum.remove(p);
					}
				}
				else {
					Util.sendMessage(p, "&cThis item cannot be slotted!");
					slotItem.remove(p);
					slotNum.remove(p);
				}
			}
			else {
	  		Util.sendMessage(p, "&cSomething went wrong! Please try again.");
	  		slotItem.remove(p);
	  		slotNum.remove(p);
			}
		}
	}
}
