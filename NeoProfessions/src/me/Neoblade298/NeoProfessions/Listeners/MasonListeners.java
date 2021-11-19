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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.droppables.SkillAPIDrop;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Inventories.ConfirmAugmentInventory;
import me.Neoblade298.NeoProfessions.Inventories.ConfirmSlotInventory;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();
	HashMap<Player, Integer> slotNum = new HashMap<Player, Integer>();
	Random gen = new Random();

	// Constants
	static final int SLOT_ESSENCE = 3;
	static final int SLOT_GOLD = 2000;

	Professions main;
	Economy econ;
	MasonUtils masonUtils;
	Util util;
	CommonItems common;
	CurrencyManager cm;

	public MasonListeners(Professions main) {
		this.main = main;
		econ = main.getEconomy();
		masonUtils = new MasonUtils();
		util = new Util();
		common = new CommonItems();
		cm = main.cManager;
	}

	public void prepItemSlot(Player p, ItemStack item, int slot) {
		slotItem.put(p, item);
		slotNum.put(p, slot);
		util.sendMessage(p, "&7Hold the item you wish to slot and right click!");

		// Time out the repair in 10 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				if (slotItem.containsKey(p)) {
					slotItem.remove(p);
					slotNum.remove(p);
					util.sendMessage(p, "&cSlot command timed out");
				}
			}
		}, 200L);
	}

	@EventHandler
	public void onLoot(MythicMobLootDropEvent e) {
		if (e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			ItemStack item = p.getInventory().getItemInMainHand();
			String expLine = null;

			// First check what charms the player has
			if (!item.getType().equals(Material.PRISMARINE_CRYSTALS) && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				for (String line : item.getItemMeta().getLore()) {
					if (line.contains("Exp")) {
						expLine = line;
					}
				}
			}

			// Exp charm is handled by NeoPartyExp
			if (expLine != null) {
				for (Drop d : e.getDrops().getDrops()) {
					if (d instanceof SkillAPIDrop) {
						double amount = d.getAmount();
						if (expLine.contains("Advanced")) {
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
	public void onAugmentSlot(InventoryClickEvent e) {
		if (!e.isLeftClick()) {
			return;
		}
		if (e.getCursor() == null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		ItemStack augment = e.getCursor();
		ItemStack item = e.getCurrentItem();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		NBTItem nbti = new NBTItem(item);
		NBTItem nbtaug = new NBTItem(augment);
		if (nbti.getInteger("slotsCreated") <= 0) {
			util.sendMessage(p, "&cNo slots available on this item!");
			return;
		}
		else if (nbti.getInteger("version") <= 0) {
			util.sendMessage(p, "&cUnsupported item version, update with /prof convert!");
			return;
		}
		else if (nbti.getInteger("level") < nbtaug.getInteger("level")) {
			util.sendMessage(p, "&cItem level must be greater than or equal to augment level!");
			return;
		}
		/*
		else if (slot == 0) {
			int augmentLevel = masonUtils.getAugmentLevel(augment);
			int gearLevel = util.getItemLevel(item);
			String slotError = masonUtils.canSlot(item, augment, gearLevel, augmentLevel, slotType);
			if (slotError != null) {
				util.sendMessage(p, slotError);
				return;
			}
			ItemStack clone = augment.clone();
			clone.setAmount(1);
			e.setCancelled(true);
			p.getOpenInventory().close();
			new ReplaceSlotInventory(main, p, item, clone, masonUtils, slotType, util);
		}
			*/
		else {
			ItemStack clone = augment.clone();
			clone.setAmount(1);
			e.setCancelled(true);
			p.getOpenInventory().close();
			new ConfirmAugmentInventory();
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getPlayer() == null || !(e.getPlayer() instanceof Player)
				|| !(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack itemToSlot = p.getInventory().getItemInMainHand();
		ItemStack offhand = p.getInventory().getItemInOffHand();

		if (itemToSlot.getType() == Material.ENDER_PEARL && itemToSlot.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}

		if (itemToSlot.getType() == Material.ENDER_EYE && itemToSlot.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}

		if (offhand.getType() == Material.ENDER_PEARL && offhand.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}

		if (offhand.getType() == Material.ENDER_EYE && offhand.getItemMeta().hasLore()) {
			e.setCancelled(true);
		}

		if (slotItem.containsKey(p)) {

			e.setCancelled(true);
			int slot = slotNum.get(p);
			ItemStack itemWithSlot = slotItem.get(p);
			int slotLevel = masonUtils.getSlotLevel(slot, itemWithSlot);
			slotNum.remove(p);
			slotItem.remove(p);

			String slotType = masonUtils.slotType(itemToSlot);
			if (p.getInventory().containsAtLeast(itemWithSlot, 1)) {
				if (slotType != null) {
					if (masonUtils.getAugmentLevel(itemToSlot) == slotLevel
							|| (itemToSlot.getType().equals(Material.PRISMARINE_CRYSTALS)
									&& masonUtils.getAugmentLevel(itemToSlot) <= slotLevel)
							|| (itemToSlot.getType().equals(Material.QUARTZ))
									&& masonUtils.getAugmentLevel(itemToSlot) <= slotLevel) {
						int level = util.getItemLevel(itemWithSlot);
						if ((util.isArmor(itemWithSlot) && slotType.contains("armor"))
								|| (util.isWeapon(itemWithSlot) && slotType.contains("weapon"))
								|| (!slotType.contains("armor")
										&& !slotType.contains("weapon"))) {
							if (cm.hasEnough(p, "essence", level, SLOT_ESSENCE)) {
								if (econ.has(p, SLOT_GOLD)) {
									boolean success = false;
									switch (slotType) {
									case "durability":
										success = masonUtils.parseDurability(itemWithSlot, itemToSlot, slot);
										break;
									case "weaponattribute":
										success = masonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
										break;
									case "armorattribute":
										success = masonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
										break;
									case "weaponoverload":
										success = masonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
										break;
									case "armoroverload":
										success = masonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
										break;
									case "charm":
										success = masonUtils.parseCharm(p, itemWithSlot, itemToSlot, slot);
										break;
									case "relic":
										if (!masonUtils.hasRelic(itemWithSlot)) {
											success = masonUtils.parseRelic(p, itemWithSlot, itemToSlot, slot);
										}
										else {
											util.sendMessage(p, "&cOnly one relic may be slotted per item!");
										}
										break;
									}
									if (success) {
										p.getInventory().removeItem(util.setAmount(new ItemStack(itemToSlot), 1));
										cm.subtract(p, "essence", level, SLOT_ESSENCE);
										econ.withdrawPlayer(p, SLOT_GOLD);
										util.sendMessage(p, "&7Successfully slotted item!");
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
							util.sendMessage(p, "&cThis augment is incompatible with this item type!");
							slotItem.remove(p);
							slotNum.remove(p);
						}
					}
					else {
						util.sendMessage(p,
								"&cThis item must be the same level as this slot (or be a charm/relic)!");
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

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() > p.getHealth()) {
				// First check what charms the player has
				ItemStack item = p.getInventory().getItemInMainHand();
				boolean hasChance = false;
				if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
					for (String line : item.getItemMeta().getLore()) {
						if (line.contains("Second Chance")) {
							hasChance = true;
							break;
						}
					}
				}
				if (hasChance) {
					masonUtils.breakSecondChance(item);
					util.sendMessage(p, "&7Your second chance charm was broken!");
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.8);
					e.setCancelled(true);
				}
			}
		}
	}
}
