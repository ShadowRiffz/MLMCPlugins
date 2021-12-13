package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.api.event.PlayerAttributeLoadEvent;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;
import com.sucy.skill.api.event.PlayerLoadCompleteEvent;

import de.tr7zw.nbtapi.NBTItem;

public class AugmentManager implements Listener {
	// event types
	public static HashMap<String, Augment> nameMap = new HashMap<String, Augment>();
	public static HashMap<Player, PlayerAugments> playerAugments = new HashMap<Player, PlayerAugments>();
	public static ArrayList<String> enabledWorlds = new ArrayList<String>();
	
	static {
		enabledWorlds.add("Argyll");
		enabledWorlds.add("Dev");
		enabledWorlds.add("ClassPVP");
	}
	
	public AugmentManager() {
		// Load augments? Maybe don't need
		nameMap.put("Finisher", new FinisherAugment());
		nameMap.put("Initiator", new InitiatorAugment());
	}
	
	public static boolean isAugment(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return nameMap.containsKey(nbti.getString("augment"));
	}
	
	public boolean containsAugments(Player p, EventType etype) {
		return playerAugments.containsKey(p) && playerAugments.get(p).containsAugments(etype);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onAttributeLoad(PlayerAttributeLoadEvent e) {
		Player p = e.getPlayer();
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onAttributeUnload(PlayerAttributeUnloadEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onKicked(PlayerKickEvent e) {
		Player p = e.getPlayer();
		playerAugments.remove(p);
	}

	@EventHandler(ignoreCancelled = true)
	public void onItemBreak(PlayerItemBreakEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;

		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSQLLoad(PlayerLoadCompleteEvent e) {
		Player p = e.getPlayer();
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSwapHand(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;

		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!enabledWorlds.contains(p.getWorld().getName())) return;
		
		if (!playerAugments.containsKey(p)) {
			playerAugments.put(p, new PlayerAugments(p));
		}
		else {
			playerAugments.get(p).inventoryChanged();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) e.getDamager();
			double multiplier = 1;
			double flat = 0;
			if (containsAugments(p, EventType.DAMAGE)) {
				for (Augment augment : AugmentManager.playerAugments.get(p).getAugments(EventType.DAMAGE)) {
					if (augment instanceof ModDamageDealtAugment) {
						ModDamageDealtAugment aug = (ModDamageDealtAugment) augment;
						if (aug.canUse(p, (LivingEntity) e.getEntity())) {
							multiplier += aug.getMultiplierBonus();
							flat += aug.getFlatBonus();
						}
					}
				}
			}
			e.setDamage(e.getDamage() * multiplier + flat);
		}
	}
}
