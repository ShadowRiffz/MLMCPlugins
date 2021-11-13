package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.NeoConsumables;

public class TokenConsumable extends Consumable {
	ArrayList<SettingsChanger> settingsChangers = new ArrayList<SettingsChanger>();
	
	public TokenConsumable(NeoConsumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore) {
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
		NBTItem nbti = new NBTItem(item);
		if (!p.getName().equals(nbti.getString("player"))) {
			p.sendMessage("§4[§c§lMLMC§4] §cYou cannot use this as you are not §e" + nbti.getString("player") + "§c!");
			return false;
		}
		
		long now = System.currentTimeMillis();
		long usable = nbti.getLong("timestamp") + 86400000;
		if (now > usable) {
			p.sendMessage("§4[§c§lMLMC§4] §cThis token has already expired!");
			p.getInventory().remove(item);
			return false;
		}
		
		for (SettingsChanger sc : this.settingsChangers) {
			if (!sc.getOverwrite() && sc.exists(p.getUniqueId())) {
				p.sendMessage("§4[§c§lMLMC§4] §cOne of these tokens is already active!");
				return false;
			}
		}
		return true;
	}
	
	public void use(Player p, ItemStack item) {
		UUID uuid = p.getUniqueId();

		for (SettingsChanger sc : this.settingsChangers) {
			if (!sc.changeSetting(uuid)) {
				p.sendMessage("§4[§c§lMLMC§4] §cFailed to change setting!");
				return;
			}
		}
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}

		p.sendMessage("§4[§c§lMLMC§4] §7You used " + displayname + "§7!");
		
		item.setAmount(item.getAmount() - 1);
	}
	
	public ArrayList<SettingsChanger> getSettingsChangers() {
		return this.settingsChangers;
	}
	
	public void setSettingsChangers(ArrayList<SettingsChanger> settingsChangers) {
		this.settingsChangers = settingsChangers;
	}
}
