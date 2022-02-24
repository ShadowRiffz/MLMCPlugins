package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoConsumables.Consumables;
import net.md_5.bungee.api.ChatColor;

public abstract class Consumable {
	Consumables main;
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	String name;
	protected String displayname;
	ArrayList<String> lore;
	HashMap<String, String> nbt;

	public Consumable(Consumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore, HashMap<String, String> nbt) {
		this.main = main;
		this.displayname = name.replaceAll("&", "§").replaceAll("@", "&");
		this.name = ChatColor.stripColor(this.displayname);
		this.sounds = sounds;
		this.lore = lore;
		this.nbt = nbt;
	}
	
	public Consumables getMain() {
		return main;
	}

	public void setMain(Consumables main) {
		this.main = main;
	}

	public ArrayList<Sound> getSounds() {
		return sounds;
	}

	public void setSounds(ArrayList<Sound> sounds) {
		this.sounds = sounds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public ArrayList<String> getLore() {
		return lore;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}
	
	public HashMap<String, String> getNbt() {
		return nbt;
	}

	public abstract boolean isSimilar(ItemStack item);
	public abstract boolean canUse(Player p, ItemStack item);
	public abstract void use(Player p, ItemStack item);
}
