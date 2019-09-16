package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.droppables.SkillAPIDrop;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();
	HashMap<Player, Integer> slotNum = new HashMap<Player, Integer>();
	HashMap<Player, Long> secondChanceCooldown = new HashMap<Player, Long>();
	final static long SECOND_CHANCE_COOLDOWN = 900000;
	Random gen = new Random();
	
	// Constants
	static final int SLOT_ESSENCE = 3;
	static final int SLOT_GOLD = 2000;

	Main main;
	Economy econ;
	MasonUtils masonUtils;
	Util util;
	CommonItems common;
	public MasonListeners(Main main) {
		this.main = main;
		econ = main.getEconomy();
		masonUtils = new MasonUtils();
		util = new Util();
		common = new CommonItems();
	}
	
	public void prepItemSlot(Player p, ItemStack item, int slot) {
		slotItem.put(p, item);
		slotNum.put(p, slot);
		util.sendMessage(p, "&7Hold the item you wish to slot and right click!");
		
		// Time out the repair in 10 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
		  public void run() {
		  	if(slotItem.containsKey(p)) {
		  		slotItem.remove(p);
		  		slotNum.remove(p);
		  		util.sendMessage(p, "&cSlot command timed out");
		  	}
		  }
		}, 200L);
	}
	
	@EventHandler
	public void onLoot(MythicMobLootDropEvent e) {
		if(e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			ItemStack item = p.getInventory().getItemInMainHand();
			String lootLine = null;
			String expLine = null;
			
			// First check what charms the player has
			if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Looting")) {
						lootLine = line;
					}
					else if(line.contains("Exp")) {
						expLine = line;
					}
				}
			}
			
			// Looting charm
			if(lootLine != null) {
				if(lootLine.contains("Advanced") && e.getMobType().getDisplayName() != null) {
					String[] name = e.getMobType().getDisplayName().get().split(" ");
					if(name.length > 2) {
						if(name[1].contains("]")) {
							double amount = Double.parseDouble(name[1].substring(0, name[1].length() - 1));
							amount = amount * (1.5 + gen.nextDouble());
							econ.depositPlayer(p, amount);
						}
					}
				}
				else if (e.getMobType().getDisplayName() != null){
					String[] name = e.getMobType().getDisplayName().get().split(" ");
					if(name.length > 2) {
						if(name[1].contains("]")) {
							double amount = Double.parseDouble(name[1].substring(0, name[1].length() - 1));
							amount = amount * (0.5 + gen.nextDouble());
							econ.depositPlayer(p, amount);
						}
					}
				}
			}
			
			// Exp charm
			if (expLine != null) {
				for(Drop d : e.getDrops().getDrops()) {
					if(d instanceof SkillAPIDrop) {
						double amount = d.getAmount();
						if(expLine.contains("Advanced")) {
							d.setAmount(amount * 2);
						}
						else {
							d.setAmount(amount * 1.5);
						}
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			// First check what charms the player has
			ItemStack item = p.getInventory().getItemInMainHand();
			String chanceLine = null;
			if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Second Chance")) {
						chanceLine = line;
					}
				}
			}
			if(e.getDamage() > p.getHealth()) {
				if((secondChanceCooldown.containsKey(p) && secondChanceCooldown.get(p) > System.currentTimeMillis()) ||
						!secondChanceCooldown.containsKey(p)) {
					if(chanceLine != null) {
						masonUtils.breakSecondChance(item);
						util.sendMessage(p, "&7Your second chance charm was broken");
						secondChanceCooldown.put(p, System.currentTimeMillis() + SECOND_CHANCE_COOLDOWN);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.8);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer() == null || !(e.getPlayer() instanceof Player) || !(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack itemToSlot = p.getInventory().getItemInMainHand();
		
		if (itemToSlot.getType() == Material.ENDER_PEARL && itemToSlot.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}
		
		if (itemToSlot.getType() == Material.ENDER_EYE && itemToSlot.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}
		
		if(slotItem.containsKey(p)) {
			
			e.setCancelled(true);
			int slot = slotNum.get(p);
			ItemStack itemWithSlot = slotItem.get(p);
			int slotLevel = masonUtils.getSlotLevel(itemWithSlot, slot);
			slotNum.remove(p);
			slotItem.remove(p);
			
			String slotType = masonUtils.slotType(itemToSlot);
			if(p.getInventory().containsAtLeast(itemWithSlot, 1)) {
				if(slotType != null) {
					if(masonUtils.getAugmentLevel(itemToSlot) <= slotLevel) {
						int level = util.getItemLevel(itemWithSlot);
						if(p.getInventory().containsAtLeast(common.getEssence(level), SLOT_ESSENCE)) {
							if(econ.has(p, SLOT_GOLD)) {
								boolean success = false;
								switch (slotType) {
								case "durability":
									success = masonUtils.parseDurability(itemWithSlot, itemToSlot, slot);
									break;
								case "attribute":
									success = masonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
									break;
								case "overload":
									success = masonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
									break;
								case "charm":
									success = masonUtils.parseCharm(p, itemWithSlot, itemToSlot, slot);
									break;
								}
								if (success) {
									p.getInventory().removeItem(util.setAmount(new ItemStack(itemToSlot), 1));
									p.getInventory().removeItem(util.setAmount(common.getEssence(level), SLOT_ESSENCE));
									econ.withdrawPlayer(p, SLOT_GOLD);
									util.sendMessage(p, "&cSuccessfully slotted item!");
								}
								else {
									util.sendMessage(p, "&cFailed to slot item!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the gold to do this!");
								slotItem.remove(p);
								slotNum.remove(p);
							}
						}
						else {
							util.sendMessage(p, "&cYou lack the materials to do this!");
							slotItem.remove(p);
							slotNum.remove(p);
						}
					}
					else {
						util.sendMessage(p, "&cThis item is too high level for this slot!");
						slotItem.remove(p);
						slotNum.remove(p);
					}
				}
				else {
					util.sendMessage(p, "&cThis item cannot be slotted!");
					slotItem.remove(p);
					slotNum.remove(p);
				}
			}
			else {
		  		util.sendMessage(p, "&cSomething went wrong! Please try again.");
		  		slotItem.remove(p);
		  		slotNum.remove(p);
			}
		}
	}
}
