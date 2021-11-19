package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.nbtapi.NBTItem;

public class PlayerAugments {
	private final int MAX_AUGMENTS = 30;
	private ArrayList<Augment> chestAugs;
	private ArrayList<Augment> legsAugs;
	private ArrayList<Augment> mainAugs;
	private ArrayList<Augment> offAugs;
	private Player p;
	
	public PlayerAugments(Player p) {
		this.p = p;
		recalculateAll();
	}
	
	public void swapHands() {
		ArrayList<Augment> temp = this.mainAugs;
		this.mainAugs = this.offAugs;
		this.mainAugs = temp;
	}
	
	public void recalculateMainhand() {
		this.mainAugs = checkAugments(p.getInventory().getItemInMainHand());
	}
	
	public void recalculateMainhand(ItemStack item) {
		this.mainAugs = checkAugments(item);
	}
	
	public void recalculateAll() {
		PlayerInventory inv = p.getInventory();
		this.chestAugs = checkAugments(inv.getChestplate());
		this.legsAugs = checkAugments(inv.getLeggings());
		this.mainAugs = checkAugments(inv.getItemInMainHand());
		this.offAugs = checkAugments(inv.getItemInOffHand());
	}
	
	public ArrayList<Augment> checkAugments(ItemStack item) {
		ArrayList<Augment> augs = new ArrayList<Augment>();
		
		if (item != null && !item.getType().isAir()) {
			NBTItem nbti = new NBTItem(item);
			for (int i = 1; i <= nbti.getInteger("slotsCreated"); i++) {
				String augmentName = nbti.getString("slot" + i + "Augment");
				if (AugmentManager.nameMap.containsKey(augmentName)) {
					Augment aug = AugmentManager.nameMap.get(augmentName);
					augs.add(aug);
				}
			}
		}
		
		return augs;
	}
	
	public ArrayList<Augment> getAugments() {
		ArrayList<Augment> augments = new ArrayList<Augment>(MAX_AUGMENTS);
		augments.addAll(chestAugs);
		augments.addAll(legsAugs);
		augments.addAll(mainAugs);
		augments.addAll(offAugs);
		return augments;
	}
}
