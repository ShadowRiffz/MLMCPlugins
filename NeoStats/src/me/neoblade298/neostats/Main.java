package me.neoblade298.neostats;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import me.neoblade298.neostats.Commands;

import org.bukkit.event.EventHandler;

public class Main extends JavaPlugin implements Listener{
	
	Map<String, Map<String, Double>> bosses = new HashMap<String, Map<String, Double>>();
	BukkitAPIHelper helper;
	MobManager manager;
	YamlConfiguration conf;
	boolean debug;
	boolean report;
	  
	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoStats Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("neostats").setExecutor(new Commands(this));
	    
	    // Save config if doesn't exist
	    File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) {
	      saveResource("config.yml", false);
	    }
	  	conf = YamlConfiguration.loadConfiguration(file);
	  	
	  	// Load in all bosses into a hashmap
	  	for (String key : conf.getKeys(false)) {
	  		for(String mob : conf.getStringList(key)) {
	  			bosses.put(mob, new HashMap<String, Double>());
	  		}
	  	}
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();
	}
	  
	public void onDisable() {
	  Bukkit.getServer().getLogger().info("NeoStats Disabled");
	  super.onDisable();
	}
	  
	public void displayStats(String deadBoss, String displayName) {
		// Check if the death was a boss mob
		if (bosses.containsKey(deadBoss)) {
			Map<String, Double> bossMap = bosses.get(deadBoss);
			for (String mob : conf.getStringList(deadBoss)) {
				
				// Only add up damage that isn't the boss mob that died
				if (!mob.equals(deadBoss)) {
					Map<String, Double> mobMap = bosses.get(mob);

					// Iterate through every player that damaged non-boss mobs and add them
					if (mobMap != null) {
						for (String player : mobMap.keySet()) {
							double prevDamage = 0;
							if (bosses.get(deadBoss).containsKey(player)) {
								prevDamage = bosses.get(deadBoss).get(player);
							}
							prevDamage += bosses.get(mob).get(player);
							bossMap.put(player, prevDamage);
						}
					}
				}
			}
			
			// Damage is calculated, now display to all relevant players
			for (String receiver : bossMap.keySet()) {
				if(Bukkit.getPlayer(receiver) != null) {
					Bukkit.getPlayer(receiver).sendMessage("§cDamage Statistics §7(§4§l" + displayName + "§7)");
					Bukkit.getPlayer(receiver).sendMessage("§7-----");
					for (String player : bossMap.keySet()) {
						double damage = Math.round((bossMap.get(player) * 100) / 100);
						String stat = new String("§e" + player + "§7 - " + damage);
						Bukkit.getPlayer(receiver).sendMessage(stat);
					}
				}
			}
			if (report && Bukkit.getPlayer("Neoblade298") != null) {
				Bukkit.getPlayer("Neoblade298").sendMessage("§cDamage Statistics §7(§4§l" + displayName + "§7)");
				Bukkit.getPlayer("Neoblade298").sendMessage("§7-----");
				for (String player : bossMap.keySet()) {
					double damage = Math.round((bossMap.get(player) * 100) / 100);
					String stat = new String("§e" + player + "§7 - " + damage);
					Bukkit.getPlayer("Neoblade298").sendMessage(stat);
				}
			}
			else if (report && Bukkit.getPlayer("Neoblade298") == null) {
				report = false;
			}
			
			// Reset boss statistics
			for (String mob : conf.getStringList(deadBoss)) {
				bosses.put(mob, new HashMap<String, Double>());
			}
		}
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();
	}
	
	@EventHandler
	public void onMMDamage(EntityDamageByEntityEvent e) {
		// If the entity is a mythicmob and in the list, record damage
		// Also ignores "location" entities, which are for skillapi use
		if(this.debug) {
			System.out.println(e.getEntity().getName());
		}
		if (!e.getEntity().getName().equalsIgnoreCase("Location") &&
				e.getEntity() != null &&
				e.getEntity() instanceof Entity &&
				helper.isMythicMob(e.getEntity()) &&
				bosses.containsKey(helper.getMythicMobInstance(e.getEntity()).getType().getInternalName())) {

			String mob = helper.getMythicMobInstance(e.getEntity()).getType().getInternalName();
			
			// Make sure the entity damaging is a player
			String player = null;
			if(e.getDamager() instanceof Player) {
				player = e.getDamager().getName();
			}
			else if(e.getDamager() instanceof TippedArrow && ((TippedArrow)e.getDamager()).getShooter() instanceof Player) {
				player = ((TippedArrow)((Arrow) e.getDamager()).getShooter()).getName();
			}
			else if(e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Player) {
				player = ((Player)((Arrow) e.getDamager()).getShooter()).getName();
			}
			else {
				return;
			}
			
			if (player != null) {
				double prevDamage = 0;
				Map<String, Double> playerMap = bosses.get(mob);
				if(playerMap.containsKey(player)) {
					prevDamage = playerMap.get(player);
				}
				double newDamage = prevDamage + e.getDamage();
				playerMap.put(player, newDamage);
			}
		}
	}
	
	@EventHandler
	public void onMMSpawn(MythicMobSpawnEvent e) {
		if(bosses.containsKey(e.getMobType().getInternalName())) {
			for (String mob : conf.getStringList(e.getMobType().getInternalName())) {
				bosses.put(mob, new HashMap<String, Double>());
			}
		}
	}
	
	@EventHandler
	public void onMMReload(MythicReloadedEvent e) {
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();
	}
}
