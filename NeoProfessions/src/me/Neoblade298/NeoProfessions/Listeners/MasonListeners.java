package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
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
	public void onMobDeath(MythicMobDeathEvent e) {
		if(e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
				String lootLine = null;
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Looting")) {
						lootLine = line;
						break;
					}
				}
				if(lootLine != null) {
					if(lootLine.contains("Advanced")) {
						String[] name = e.getMobType().getDisplayName().split(" ");
						if(name.length > 2) {
							if(name[2].contains("]")) {
								double amount = Double.parseDouble(name[2].substring(0, name[2].length() - 1));
								amount = amount * (1.5 + gen.nextDouble());
								econ.depositPlayer(p, amount);
							}
						}
					}
					else {
						String[] name = e.getMobType().getDisplayName().split(" ");
						if(name.length > 2) {
							if(name[2].contains("]")) {
								double amount = Double.parseDouble(name[2].substring(0, name[2].length() - 1));
								amount = amount * (0.5 + gen.nextDouble());
								econ.depositPlayer(p, amount);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLoot(MythicMobLootDropEvent e) {
		for(Drop d : e.getDrops().getDrops()) {
			if(d instanceof SkillAPIDrop) {
				double amount = d.getAmount();
				if(e.getKiller() instanceof Player) {
					Player p = (Player) e.getKiller();
					ItemStack item = p.getInventory().getItemInMainHand();
					String expLine = MasonUtils.charmLine(item, "Exp");
					if(expLine != null && expLine.contains("Advanced")) {
						d.setAmount(amount * 2);
					}
					else if(expLine != null) {
						d.setAmount(amount * 1.5);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(e.getDamage() > p.getHealth()) {
				if((secondChanceCooldown.containsKey(p) && secondChanceCooldown.get(p) > System.currentTimeMillis()) ||
						!secondChanceCooldown.containsKey(p)) {
					ItemStack item = p.getInventory().getItemInMainHand();
					String line = MasonUtils.charmLine(item, "Second Chance");
					if(line != null) {
						MasonUtils.breakSecondChance(item);
						Util.sendMessage(p, "&7Your second chance charm was broken");
						secondChanceCooldown.put(p, System.currentTimeMillis() + SECOND_CHANCE_COOLDOWN);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.8);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer() == null || !(e.getPlayer() instanceof Player) || !e.getAction().equals(Action.RIGHT_CLICK_AIR) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
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
								success = MasonUtils.parseCharm(p, itemWithSlot, itemToSlot, slot);
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
