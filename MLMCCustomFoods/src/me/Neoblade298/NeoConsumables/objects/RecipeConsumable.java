package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoConsumables.Consumables;

public class RecipeConsumable extends Consumable {
	String permission;

	public RecipeConsumable(Consumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore, HashMap<String, String> nbt) {
		super(main, name, sounds, lore, nbt);
	}

	public boolean canUse(Player p, ItemStack item) {
		if (p.hasPermission(permission)) {
			p.sendMessage("§4[§c§lMLMC§4] §cYou already know this recipe!");
			return false;
		}
		return true;
	}
	
	public void use(Player p, ItemStack item) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set " + permission);
		p.sendMessage("§4[§c§lMLMC§4] §7You learned " + displayname + "§7!");
		p.sendMessage("§4[§c§lMLMC§4] §7Type §c/recipes §7to see it!");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		
		item.setAmount(item.getAmount() - 1);
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}
