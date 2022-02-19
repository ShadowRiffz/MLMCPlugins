package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.Consumables;

public class ChestConsumable extends Consumable {
	
	public ChestConsumable(Consumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore, HashMap<String, String> nbt) {
		super(main, name, sounds, lore, nbt);
	}

	public boolean isSimilar(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return getNbt().get("chest").equals(nbti.getString("chest"));
	}

	public boolean canUse(Player p, ItemStack item) {
		if (main.isInstance) {
			String message = "&cYou cannot open chests in a boss fight!";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}
		return true;
	}
	
	public void use(Player p, ItemStack item) {
		p.sendMessage("§4[§c§lMLMC§4] §7You opened " + displayname + "§7 to find...");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		item.setAmount(item.getAmount() - 1);
		Consumables.bosschests.get(getNbt().get("chest")).useChest(p);
		return;
	}
}
