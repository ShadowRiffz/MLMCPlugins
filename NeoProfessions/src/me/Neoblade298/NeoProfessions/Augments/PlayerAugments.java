package me.Neoblade298.NeoProfessions.Augments;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.nbtapi.NBTItem;

public class PlayerAugments {
	private HashMap<EventType, ArrayList<Augment>> augments;
	private Player p;
	private boolean invChanged;
	private int prevSlot;
	
	public PlayerAugments(Player p) {
		this.p = p;
		this.augments = new HashMap<EventType, ArrayList<Augment>>();
		this.invChanged = true;
		this.prevSlot = -1;

		PlayerInventory inv = p.getInventory();
		checkAugments(inv.getChestplate());
		checkAugments(inv.getLeggings());
		checkAugments(inv.getItemInMainHand());
		checkAugments(inv.getItemInOffHand());
	}
	
	public void inventoryChanged() {
		invChanged = true;
	}
	
	public void checkAugments(ItemStack item) {
		if (item != null && !item.getType().isAir()) {
			NBTItem nbti = new NBTItem(item);
			for (int i = 1; i <= nbti.getInteger("slotsCreated"); i++) {
				String augmentName = nbti.getString("slot" + i + "Augment");
				if (AugmentManager.augmentMap.containsKey(augmentName)) {
					Augment aug = AugmentManager.augmentMap.get(augmentName).get(nbti.getInteger("slot" + i + "Level"));
					
					// Add augment to hashmap
					for (EventType etype : aug.getEventTypes()) {
						if (augments.containsKey(etype)) {
							augments.get(etype).add(aug);
						}
						else {
							ArrayList<Augment> list = new ArrayList<Augment>();
							list.add(aug);
							augments.put(etype, list);
						}
					}
				}
			}
		}
	}
	
	public List<Augment> getAugments(EventType etype) {
		return augments.get(etype);
	}
	
	public boolean containsAugments(EventType etype) {
		PlayerInventory inv = p.getInventory();
		
		// 2 cases for recalculation:
		// 1: Mainhand has changed slots
		// 2: Mainhand is same slot and inv has changed
		if (inv.getHeldItemSlot() != prevSlot || invChanged) {
			augments.clear();
			checkAugments(inv.getChestplate());
			checkAugments(inv.getLeggings());
			checkAugments(inv.getItemInMainHand());
			checkAugments(inv.getItemInOffHand());
			prevSlot = inv.getHeldItemSlot();
			invChanged = false;
		}
		return augments.containsKey(etype);
	}
	
	public String toString() {
		String toReturn = "";
		for (EventType key : augments.keySet()) {
			toReturn += key + ": ";
			for (Augment aug : augments.get(key)) {
				toReturn += aug.getLine() + ",";
			}
			toReturn.substring(0, toReturn.length() - 1);
		}
		return toReturn;
	}
}
