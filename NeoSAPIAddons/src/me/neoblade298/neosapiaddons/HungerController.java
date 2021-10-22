package me.neoblade298.neosapiaddons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class HungerController implements Listener {
	
	HashSet<Player> sprintingPlayers;
	HashMap<Player, Integer> hunger;
	
	// How many seconds it takes for food to decrease by 1
	final int FOOD_SCALE = 10;
	
	private Main main;
	public HungerController(Main main) {
		this.main = main;
		sprintingPlayers = new HashSet<Player>();
		hunger = new HashMap<Player, Integer>();
		
		BukkitRunnable foodTimer = new BukkitRunnable() {
			public void run() {
				Iterator<Player> iter = sprintingPlayers.iterator();
				while (iter.hasNext()) {
					Player p = iter.next();
					if (!main.isQuestWorld(p.getWorld())) {
						iter.remove();
					}
					else if (hunger.containsKey(p)) {
						int pHunger = hunger.get(p);
						if (pHunger % FOOD_SCALE == 0) {
							FoodLevelChangeEvent e = new FoodLevelChangeEvent(p, pHunger / FOOD_SCALE);
							Bukkit.getPluginManager().callEvent(e);
							if (e.isCancelled()) {
								return;
							}
							p.setFoodLevel(pHunger / FOOD_SCALE);
						}
						
						// Only happens if event is not cancelled
						hunger.put(p, hunger.get(p) - 1);
					}
					else {
						hunger.put(p, (p.getFoodLevel() * FOOD_SCALE) - 1);
					}
				}
			}
		};
		foodTimer.runTaskTimer(main, 0, 20);
	}

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		int newFoodLevel = Math.min(20, e.getFoodLevel());
		if (main.isQuestWorld(p.getWorld())) {
			if (newFoodLevel < p.getFoodLevel()) {
				if (hunger.containsKey(p)) {
					if (hunger.get(p) / FOOD_SCALE != e.getFoodLevel()) {
						
						// Only allow decreased hunger if it roughly matches the hunger scale
						// (When this plugin calls it, hunger % FOOD_SCALE = 1 actually)
						e.setCancelled(true);
						return;
					}
				}
			}
			else {
				if (hunger.containsKey(p)) {
					int oldHunger = hunger.get(p);
					if (e.getFoodLevel() == 20) {
						hunger.put(p, 20 * FOOD_SCALE);
					}
					hunger.put(p, oldHunger + ((newFoodLevel - p.getFoodLevel()) * FOOD_SCALE));
				}
				else {
					hunger.put(p, newFoodLevel * FOOD_SCALE);
				}
			}
		}
	}
	
	@EventHandler
	public void onHungerRegen(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player && main.isQuestWorld(e.getEntity().getWorld())) {
			if (e.getRegainReason().equals(RegainReason.SATIATED)) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent e) {
		Player p = e.getPlayer();
		if (main.isQuestWorld(p.getWorld())) {
			if (e.isSprinting() && p.getGameMode().equals(GameMode.SURVIVAL)) {
				sprintingPlayers.add(p);
			}
			else {
				sprintingPlayers.remove(p);
			}
		}
		else {
			sprintingPlayers.remove(p);
		}
	}
}
