package me.neoblade298.neostats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import org.bukkit.event.EventHandler;

public class Main extends JavaPlugin implements Listener{
	
	Map<String, Map<String, Double>> bosses = new HashMap<String, Map<String, Double>>();
	BukkitAPIHelper helper = new BukkitAPIHelper();
	MobManager manager = MythicMobs.inst().getMobManager();
	  
	public void onEnable() {
	  super.onEnable();
	  Bukkit.getServer().getLogger().info("NeoStats Enabled");
	  getServer().getPluginManager().registerEvents(this, this);
	}
	  
	public void onDisable() {
	  Bukkit.getServer().getLogger().info("NeoStats Disabled");
	  super.onDisable();
	}
	  
	@EventHandler
	public void onMMSpawn(MythicMobSpawnEvent e) {
		if (e.getMobType().getInternalName().equals("Markus")) {
			  bosses.put("Markus", new HashMap<String, Double>());
		}
	}
	  
	@EventHandler
	public void onMMDeath(MythicMobDeathEvent e) {
		if (e.getMobType().getInternalName().equals("Markus")) {
			Bukkit.getPlayer("Neoblade298").sendMessage("Damage chart:");
			Iterator<String> players = bosses.get("Markus").keySet().iterator();
			while(players.hasNext()) {
				String player = players.next();
				Bukkit.getPlayer("Neoblade298").sendMessage(player + " did " + Math.round(bosses.get("Markus").get(player) * 100) / 100 + " damage.");
			}
			bosses.remove("Markus");
		}
	}
	
	@EventHandler
	public void onMMDamage(EntityDamageByEntityEvent e) {
		
		// If the entity is a mythicmob and in the list, record damage
		if (helper.isMythicMob(e.getEntity()) && helper.getMythicMobInstance(e.getEntity()).getType().getInternalName().equals("Markus")) {
			
			// Make sure the entity damaging is a player
			String player = null;
			if(e.getDamager() instanceof Player) {
				player = e.getDamager().getName();
			}
			else if(e.getDamager() instanceof Arrow) {
				player = ((Player)((Arrow) e.getDamager()).getShooter()).getName();
				Bukkit.getPlayer("Neoblade298").sendMessage(player);
			}
			
			double prevDamage = 0;
			Map<String, Double> playerMap = bosses.get("Markus");
			if(playerMap.containsKey(player)) {
				prevDamage = playerMap.get(player);
			}
			double newDamage = prevDamage + e.getDamage();
			playerMap.put(player, newDamage);
		}
	}
}
