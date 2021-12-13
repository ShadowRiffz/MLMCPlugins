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
	}
	
	public void inventoryChanged() {
		invChanged = true;
	}
	
	public void checkAugments(ItemStack item) {
		if (item != null && !item.getType().isAir()) {
			NBTItem nbti = new NBTItem(item);
			for (int i = 1; i <= nbti.getInteger("slotsCreated"); i++) {
				String augmentName = nbti.getString("slot" + i + "Augment");
				if (AugmentManager.nameMap.containsKey(augmentName)) {
					Augment aug = AugmentManager.nameMap.get(augmentName);
					
					// Add augment to hashmap
					EventType etype = aug.getEventType();
					if (augments.containsKey(etype)) {
						augments.get(etype).add(aug);
					}
					else {
						ArrayList<Augment> list = new ArrayList<Augment>();
						augments.put(etype, list);
					}
				}
			}
		}
	}
	
	public List<Augment> getAugments(EventType etype) {
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
		}
		return augments.get(etype);
	}
	
	public boolean containsAugments(EventType etype) {
		return augments.containsKey(etype);
	}
}
