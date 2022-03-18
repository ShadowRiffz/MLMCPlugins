package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class StoredItemInstance {
	StoredItem item;
	int amt;
	private static ArrayList<String> voucherLore = new ArrayList<String>();
	
	static {
		voucherLore.add("§7§oRight click to claim!");
	}
	
	public StoredItemInstance(StoredItem item, int amt) {
		this.item = item;
		this.amt = amt;
	}

	public StoredItem getItem() {
		return item;
	}

	public int getAmount() {
		return amt;
	}
	
	public String toString() {
		return item.getDisplay() + " §fx" + amt;
	}
	
	public ItemStack getStorageView(Player p) {
		return item.getStorageView(p, amt);
	}
	
	public void giveVoucher(Player p, int amount) {
		if (amount > this.amt) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to create the voucher!");
			return;
		}
		else if (!StorageManager.playerHas(p, this.item.getId(), amount)) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to create the voucher!");
			Bukkit.getLogger().log(Level.WARNING,
					"[NeoProfessions] StorageView item instance doesn't match actual storage for id " + this.item.getId() +
					". Expected: " + amt + ", actual: " + StorageManager.getAmount(p, this.item.getId()));
			return;
		}
		
		amt -= amount;
		StorageManager.takePlayer(p, this.item.getId(), amount);
		p.getInventory().addItem(getVoucher(p, amount));
	}
	
	private ItemStack getVoucher(Player p, int amount) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.toString());
		meta.setLore(voucherLore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("id", this.item.getId());
		nbti.setInteger("amount", amount);
		return nbti.getItem();
	}
}
