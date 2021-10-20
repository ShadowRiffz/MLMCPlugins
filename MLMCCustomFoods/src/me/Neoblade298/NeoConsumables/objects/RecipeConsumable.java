package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.Neoblade298.NeoConsumables.NeoConsumables;

public class RecipeConsumable extends Consumable {
	String permission;

	public RecipeConsumable(NeoConsumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		super(main, name, sounds, lore);
	}

	public boolean isSimilar(ItemMeta meta) {
		if (!getLore().isEmpty()) {
			if (!meta.hasLore()) {
				return false;
			}

			ArrayList<String> flore = getLore();
			ArrayList<String> mlore = (ArrayList<String>) meta.getLore();

			for (int i = 0; i < flore.size(); i++) {
				String fLine = getLore().get(i);
				String mLine = mlore.get(i);
				if (!mLine.contains(fLine)) {
					return false;
				}
			}
		}
		return true;
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
		ItemStack clone = item.clone();
		item.setAmount(1);
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		p.getInventory().removeItem(clone);
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}
