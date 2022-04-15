package me.Neoblade298.NeoProfessions.Augments;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.Neoblade298.NeoProfessions.Objects.StoredAttributes;

public class PlayerAugments {
	private HashMap<EventType, ArrayList<Augment>> augments;
	private HashMap<Augment, Integer> hitCount;
	private HashMap<Augment, Integer> counts;
	private HashMap<Augment, StoredAttributes> attrs;
	private Player p;
	private boolean invChanged;
	private int prevSlot;
	
	public PlayerAugments(Player p) {
		this.p = p;
		this.augments = new HashMap<EventType, ArrayList<Augment>>();
		this.counts = new HashMap<Augment, Integer>();
		this.hitCount = new HashMap<Augment, Integer>();
		this.attrs = new HashMap<Augment, StoredAttributes>();
		this.invChanged = true;
		this.prevSlot = -1;

		PlayerInventory inv = p.getInventory();
		checkAugments(inv.getHelmet());
		checkAugments(inv.getChestplate());
		checkAugments(inv.getLeggings());
		checkAugments(inv.getBoots());
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
				if (AugmentManager.hasAugment(augmentName)) {
					Augment aug = AugmentManager.getFromCache(augmentName, nbti.getInteger("slot" + i + "Level"));
					
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
					
					// Add augment to counts
					counts.put(aug, counts.getOrDefault(aug, 0) + 1);
				}
			}
		}
	}
	
	public List<Augment> getAugments(EventType etype) {
		return augments.get(etype);
	}
	
	public int getCount(Augment aug) {
		return counts.getOrDefault(aug, 0);
	}
	
	public boolean containsAugments(EventType etype) {
		PlayerInventory inv = p.getInventory();
		
		// 2 cases for recalculation:
		// 1: Mainhand has changed slots
		// 2: Mainhand is same slot and inv has changed
		if (inv.getHeldItemSlot() != prevSlot || invChanged) {
			augments.clear();
			counts.clear();
			checkAugments(inv.getHelmet());
			checkAugments(inv.getChestplate());
			checkAugments(inv.getLeggings());
			checkAugments(inv.getBoots());
			checkAugments(inv.getItemInMainHand());
			checkAugments(inv.getItemInOffHand());
			prevSlot = inv.getHeldItemSlot();
			invChanged = false;
		}
		return augments.containsKey(etype);
	}
	
	public String toString() {
		return augments.toString();
	}
	
	public void incrementHitCount(Augment aug) {
		hitCount.put(aug, hitCount.getOrDefault(aug, 0) + 1);
	}
	
	public void resetHitCount(Augment aug) {
		hitCount.put(aug, 0);
	}
	
	public int getHitCount(Augment aug) {
		return hitCount.getOrDefault(aug, 0);
	}
	
	public void applyAttributes(Augment aug, StoredAttributes sattr) {
		sattr.applyAttributes(p);
		attrs.put(aug, sattr);
	}
	
	public void removeAttributes(Augment aug) {
		if (attrs.containsKey(aug)) {
			attrs.remove(aug).removeAttributes(p);
		}
	}
}
