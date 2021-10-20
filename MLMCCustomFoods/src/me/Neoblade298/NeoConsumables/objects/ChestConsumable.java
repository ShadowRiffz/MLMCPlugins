package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.Neoblade298.NeoConsumables.NeoConsumables;

public class ChestConsumable extends Consumable {
	ArrayList<String> commands = new ArrayList<String>();
	
	public ChestConsumable(NeoConsumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
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
		if (main.isInstance) {
			String message = "&cYou cannot open chests in a boss fight!";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}
		return true;
	}
	
	public void use(Player p, ItemStack item) {
		p.sendMessage(displayname + " §7was opened.");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		p.sendMessage("§4[§c§lMLMC§4] §7You opened " + displayname + "§7!");
		executeCommands(p);
		ItemStack clone = item.clone();
		clone.setAmount(1);
		p.getInventory().removeItem(clone);
		return;
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}
}
