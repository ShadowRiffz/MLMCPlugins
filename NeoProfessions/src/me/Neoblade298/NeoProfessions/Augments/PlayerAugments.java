package me.Neoblade298.NeoProfessions.Augments;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.nbtapi.NBTItem;

public class PlayerAugments {
	private HashMap<EventType, List<Augment>> augments;
	private Player p;
	private boolean needsRecalculation;
	
	public PlayerAugments(Player p) {
		this.p = p;
		this.augments = new HashMap<EventType, List<Augment>>();
		recalculate();
	}
	
	public void recalculate() {
		needsRecalculation = true;
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
						augments.put(etype, Arrays.asList(aug));
					}
				}
			}
		}
	}
	
	public List<Augment> getAugments(EventType etype) {
		if (needsRecalculation) {
			augments.clear();
			PlayerInventory inv = p.getInventory();
			checkAugments(inv.getChestplate());
			checkAugments(inv.getLeggings());
			checkAugments(inv.getItemInMainHand());
			checkAugments(inv.getItemInOffHand());
		}
		return augments.getOrDefault(etype, null);
	}
}
