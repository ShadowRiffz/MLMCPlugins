package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.Consumables;
import me.neoblade298.neocore.util.Util;

public class TokenConsumable extends Consumable implements GeneratableConsumable {
	ArrayList<String> commands, negatePerms, lore;
	String display;
	Material material;
	
	public TokenConsumable(Consumables main, String key) {
		super(main, key);
		
		commands = new ArrayList<String>();
		negatePerms = new ArrayList<String>();
		lore = new ArrayList<String>();
		material = Material.GOLD_INGOT;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}
	
	public void setNegatedPerms(ArrayList<String> negatedPerms) {
		this.negatePerms = negatedPerms;
	}
	
	public void setDisplay(String display) {
		this.display = correctColors(display);
	}
	
	public void setLore(ArrayList<String> lore) {
		for (String line : lore) {
			this.lore.add(correctColors(line));
		}
	}

	public boolean canUse(Player p, ItemStack item) {
		// negate perms
		for (String perm : negatePerms) {
			System.out.println(perm + " checking " + p.hasPermission(perm));
			if (p.hasPermission(perm)) {
				Util.msg(p, "&cThis token is currently active.");
				return false;
			}
		}
		return true;
	}

	public void use(final Player p, ItemStack item) {
		// First get rid of any existing effects
		for (Sound s : this.getSounds()) {
			p.playSound(p.getLocation(), s, 1.0F, 1.0F);
		}
		executeCommands(p);
		item.setAmount(item.getAmount() - 1);
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}
	
	@Override
	public ItemStack getItem(int amount) {
		ItemStack item = new ItemStack(material);
		item.setAmount(amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(display);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("consumable", key);
		return nbti.getItem();
	}
	
	private String correctColors(String line) {
		return line.replaceAll("\\\\&", "@").replaceAll("&", "§").replaceAll("@", "&");
	}

	public String getDisplay() {
		return display;
	}
}
