package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.Consumables;
import net.md_5.bungee.api.ChatColor;

public abstract class Consumable {
	Consumables main;
	String key;
	ArrayList<Sound> sounds;
	HashMap<String, String> nbt;

	public Consumable(Consumables main, String key) {
		this.main = main;
		this.key = key;
		this.sounds = new ArrayList<Sound>();
		this.nbt = new HashMap<String, String>();
	}
	
	public Consumables getMain() {
		return main;
	}

	public ArrayList<Sound> getSounds() {
		return sounds;
	}
	
	public HashMap<String, String> getNbt() {
		return nbt;
	}

	public abstract boolean canUse(Player p, ItemStack item);
	public abstract void use(Player p, ItemStack item);
}
