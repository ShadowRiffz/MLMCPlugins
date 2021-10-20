package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoConsumables.NeoConsumables;
import net.md_5.bungee.api.ChatColor;

public abstract class Consumable {
	NeoConsumables main;
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	String name, displayname;
	ArrayList<String> lore;

	public Consumable(NeoConsumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
		this.main = main;
		this.displayname = name.replaceAll("&", "§");
		this.name = ChatColor.stripColor(this.displayname);
		this.sounds = sounds;
		this.lore = lore;
	}
	
	public NeoConsumables getMain() {
		return main;
	}

	public void setMain(NeoConsumables main) {
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

	public abstract boolean isSimilar(ItemMeta meta);
	public abstract boolean canUse(Player p, ItemStack item);
	public abstract void use(Player p, ItemStack item);
}
