package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.droppables.SkillAPIDrop;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Legacy.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonListeners implements Listener {
	HashMap<Player, ItemStack> slotItem = new HashMap<Player, ItemStack>();
	HashMap<Player, Integer> slotNum = new HashMap<Player, Integer>();
	Random gen = new Random();

	// Constants
	static final int SLOT_ESSENCE = 3;
	static final int SLOT_GOLD = 2000;

	Professions main;
	Economy econ;
	MasonUtils masonUtils;
	Util util;
	CommonItems common;
	CurrencyManager cm;

	public MasonListeners(Professions main) {
		this.main = main;
		econ = main.getEconomy();
		masonUtils = new MasonUtils();
		util = new Util();
		common = new CommonItems();
		cm = main.cManager;
	}

	@EventHandler
	public void onLoot(MythicMobLootDropEvent e) {
		if (e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			ItemStack item = p.getInventory().getItemInMainHand();
			String expLine = null;

			// First check what charms the player has
			if (!item.getType().equals(Material.PRISMARINE_CRYSTALS) && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				for (String line : item.getItemMeta().getLore()) {
					if (line.contains("Exp")) {
						expLine = line;
					}
				}
			}

			// Exp charm is handled by NeoPartyExp
			if (expLine != null) {
				for (Drop d : e.getDrops().getDrops()) {
					if (d instanceof SkillAPIDrop) {
						double amount = d.getAmount();
						if (expLine.contains("Advanced")) {
							d.setAmount(amount * 2);
						}
						else {
							d.setAmount(amount * 1.5);
						}
						break;
					}
				}
			}
		}
	}
}
