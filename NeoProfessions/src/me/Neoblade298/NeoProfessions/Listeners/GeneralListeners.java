package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.AugmentManager;
import me.Neoblade298.NeoProfessions.Inventories.ConfirmAugmentInventory;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class GeneralListeners implements Listener {
	Professions main;
	Util util;

	public GeneralListeners(Professions main) {
		this.main = main;
		util = new Util();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			Professions.cm.initPlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		try {
			Professions.cm.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		try {
			Professions.cm.savePlayer(e.getPlayer());
		} catch (Exception e1) {
			e1.printStackTrace();
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

		if (item == null || item.getType().isAir() || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (augment == null || augment.getType().isAir() || !augment.hasItemMeta() || !augment.getItemMeta().hasDisplayName()) {
			return;
		}
		
		NBTItem nbti = new NBTItem(item);
		NBTItem nbtaug = new NBTItem(augment);
		if (!Util.isWeapon(item) && !Util.isArmor(item)) {
			return;
		}
		if (!AugmentManager.isAugment(augment)) {
			return;
		}
		if (nbti.getInteger("version") <= 0) {
			Util.sendMessage(p, "&cUnsupported item version, update with /prof convert!");
			return;
		}
		if (nbti.getInteger("slotsCreated") <= 0) {
			Util.sendMessage(p, "&cNo slots available on this item!");
			return;
		}
		if (!nbtaug.getString("level").isBlank() && nbtaug.getInteger("level") == 0) {
			nbtaug.setInteger("level", Integer.parseInt(nbtaug.getString("level")));
			nbtaug.applyNBT(augment);
		}
		if (nbti.getInteger("level") < nbtaug.getInteger("level")) {
			Util.sendMessage(p, "&cItem level must be greater than or equal to augment level!");
			return;
		}
		else {
			ItemStack clone = augment.clone();
			clone.setAmount(1);
			e.setCancelled(true);
			p.getOpenInventory().close();
			new ConfirmAugmentInventory(main, p, item, clone);
		}
	}
}
