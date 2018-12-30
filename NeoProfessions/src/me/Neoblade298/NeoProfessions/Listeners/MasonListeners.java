package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();

	Main main;
	public MasonListeners(Main main) {
		this.main = main;
	}
}
