package me.neoblade298.neostats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.SkillHealEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import me.neoblade298.neostats.Commands;

import org.bukkit.event.EventHandler;

public class Main extends JavaPlugin implements Listener{
	
	Map<String, Map<String, Double>> damageDealt = new HashMap<String, Map<String, Double>>();
	Map<String, Map<String, Double>> damageTaken = new HashMap<String, Map<String, Double>>();
	Map<String, Double> selfHealed = new HashMap<String, Double>();
	Map<String, Double> allyHealed = new HashMap<String, Double>();
	Map<String, String> inBoss = new HashMap<String, String>();
	Map<String, Long> timeSpawned = new HashMap<String, Long>();
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
	  	
	  	// Load in all damageDealt into a hashmap
	  	for (String key : conf.getKeys(false)) {
	  		for(String mob : conf.getStringList(key)) {
	  			damageDealt.put(mob, new HashMap<String, Double>());
	  			damageTaken.put(mob, new HashMap<String, Double>());
	  		}
	  	}
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();//
	}
	  
	public void onDisable() {
	  Bukkit.getServer().getLogger().info("NeoStats Disabled");
	  super.onDisable();
	}
	  
	public void displayStats(String deadBoss, String displayName) {
		// Check if the death was a boss mob
		if (damageDealt.containsKey(deadBoss)) {
			Map<String, Double> damageDealtMap = damageDealt.get(deadBoss);
			Map<String, Double> damageTakenMap = damageTaken.get(deadBoss);
			for (String mob : conf.getStringList(deadBoss)) {
				
				// Only add up damage that isn't the boss mob that died
				if (!mob.equals(deadBoss)) {
					Map<String, Double> mobDamageDealt = damageDealt.get(mob);
					Map<String, Double> mobDamageTaken = damageTaken.get(mob);

					// Iterate through every player that damaged non-boss mobs and add them
					if (mobDamageDealt != null) {
						for (String player : mobDamageDealt.keySet()) {
							double prevDamage = 0;
							if (damageDealt.get(deadBoss).containsKey(player)) {
								prevDamage = damageDealt.get(deadBoss).get(player);
							}
							prevDamage += damageDealt.get(mob).get(player);
							damageDealtMap.put(player, prevDamage);
						}
					}
					
					// Iterate through players that took damage from non-boss mobs and add them
					if (mobDamageTaken != null) {
						for (String player : mobDamageTaken.keySet()) {
							double prevDamage = 0;
							if (damageTaken.get(deadBoss).containsKey(player)) {
								prevDamage = damageTaken.get(deadBoss).get(player);
							}
							prevDamage += damageTaken.get(mob).get(player);
							damageTakenMap.put(player, prevDamage);
						}
					}
				}
			}
			
			// Figure out time
			long time = -1;
	        String timer = "n/a";
			if (timeSpawned.containsKey(deadBoss)) {
				time = System.currentTimeMillis() - timeSpawned.get(deadBoss);
				timeSpawned.remove(deadBoss);
				final long hr = TimeUnit.MILLISECONDS.toHours(time);
		        final long min = TimeUnit.MILLISECONDS.toMinutes(time - TimeUnit.HOURS.toMillis(hr));
		        final long sec = TimeUnit.MILLISECONDS.toSeconds(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		        final long ms = TimeUnit.MILLISECONDS.toMillis(time - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		        if (hr > 0) {
		        	timer = String.format("%2d:%02d:%02d.%03d", hr, min, sec, ms);
		        }
		        else {
		        	timer = String.format("%2d:%02d.%03d", min, sec, ms);
		        }
			}
			
			// Damage is calculated, now display to all relevant players
			ArrayList<String> toRemove = new ArrayList<String>();
			for (String receiver : inBoss.keySet()) {
				if(Bukkit.getPlayer(receiver) != null && inBoss.get(receiver).equals(deadBoss)) {
					Bukkit.getPlayer(receiver).sendMessage("§cPost-battle Stats §7(§4§l" + displayName + "§7) [Time:§c" + timer + "§7]");
					Bukkit.getPlayer(receiver).sendMessage("§7-----");
					Bukkit.getPlayer(receiver).sendMessage("§7[§cDamage Dealt §7/ §4Damage Taken §7/ §2Self Healing §7/ §aAlly Healing§7]");
					for (String player : inBoss.keySet()) {
						if(Bukkit.getPlayer(player) != null && inBoss.get(player).equals(deadBoss)) {
							int damageDealt = (int) Math.round((damageDealtMap.get(player) * 100) / 100);
							int damageTaken = (int) Math.round((damageTakenMap.get(player) * 100) / 100);
							int selfHeal = (int) Math.round((selfHealed.get(player) * 100) / 100);
							int allyHeal = (int) Math.round((allyHealed.get(player) * 100) / 100);
						
							String stat = new String("§e" + player + "§7 (§e" + SkillAPI.getPlayerData(Bukkit.getPlayer(player)).getClass("class").getData().getName() + "§7) - [§c" + damageDealt + " §7/ §4" + damageTaken + " §7/ §2" + selfHeal + " §7/ §a" + allyHeal + "§7]");
							Bukkit.getPlayer(receiver).sendMessage(stat);
							toRemove.add(receiver);
						}
					}
				}
				
				// Remove offline players
				else if(Bukkit.getPlayer(receiver) == null) {
					toRemove.add(receiver);
				}
			}
			
			for (String player : toRemove) {
				selfHealed.remove(player);
				allyHealed.remove(player);
				inBoss.remove(player);
			}
			
			// Deprecated! Update if you need again
			if (report && Bukkit.getPlayer("Neoblade298") != null) {
				Bukkit.getPlayer("Neoblade298").sendMessage("§cDamage Statistics §7(§4§l" + displayName + "§7)");
				Bukkit.getPlayer("Neoblade298").sendMessage("§7-----");
				for (String player : damageDealtMap.keySet()) {
					double damage = Math.round((damageDealtMap.get(player) * 100) / 100);
					String stat = new String("§e" + player + "§7 - " + damage);
					Bukkit.getPlayer("Neoblade298").sendMessage(stat);
				}
			}
			else if (report && Bukkit.getPlayer("Neoblade298") == null) {
				report = false;
			}
			
			// Reset boss statistics
			for (String mob : conf.getStringList(deadBoss)) {
				damageDealt.put(mob, new HashMap<String, Double>());
				damageTaken.put(mob, new HashMap<String, Double>());
			}
		}
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMMDamage(EntityDamageByEntityEvent e) {
		// If the entity is a mythicmob and in the list, record damage
		// Also ignores "location" entities, which are for skillapi use
		if(this.debug) {
			System.out.println(e.getEntity().getName());
		}
		if (helper == null) {
			helper = MythicMobs.inst().getAPIHelper();
		}
		if (!e.getEntity().getName().contains("Location") &&
			e.getEntity() != null &&
			e.getEntity() instanceof Entity &&
			helper != null) {
			
			// For damage dealt to bosses
			if (helper.isMythicMob(e.getEntity()) &&
			damageDealt.containsKey(helper.getMythicMobInstance(e.getEntity()).getType().getInternalName())) {
	
					String mob = helper.getMythicMobInstance(e.getEntity()).getType().getInternalName();
					
					// Make sure the entity damaging is a player
					String player = null;
					if(e.getDamager() instanceof Player) {
						player = e.getDamager().getName();
					}
					else if(e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Player) {
						player = ((Player)((Arrow) e.getDamager()).getShooter()).getName();
					}
					else {
						return;
					}
					
					if (player != null) {
						double prevDamage = 0;
						Map<String, Double> playerMap = damageDealt.get(mob);
						if(playerMap.containsKey(player)) {
							prevDamage = playerMap.get(player);
						}
						double newDamage = prevDamage + e.getFinalDamage();
						playerMap.put(player, newDamage);
					}
			}
			
			// For damage taken from bosses
			else if (helper.isMythicMob(e.getDamager()) &&
					damageTaken.containsKey(helper.getMythicMobInstance(e.getDamager()).getType().getInternalName())) {

				String mob = helper.getMythicMobInstance(e.getDamager()).getType().getInternalName();
				
				// Make sure the entity damaging is a player
				String player = null;
				if(e.getEntity() instanceof Player) {
					player = e.getEntity().getName();
				}

				
				if (player != null) {
					double prevDamage = 0;
					Map<String, Double> playerMap = damageTaken.get(mob);
					if(playerMap.containsKey(player)) {
						prevDamage = playerMap.get(player);
					}
					
					double newDamage = prevDamage + (e.getFinalDamage());
					playerMap.put(player, newDamage);
				}
			}
		}
	}
	
	@EventHandler
	public void onMMSpawn(MythicMobSpawnEvent e) {
		String name = e.getMobType().getInternalName();
		if(damageDealt.containsKey(name)) {
			timeSpawned.put(name, System.currentTimeMillis());
			for (String mob : conf.getStringList(name)) {
				damageDealt.put(mob, new HashMap<String, Double>());
				damageTaken.put(mob, new HashMap<String, Double>());
			}
		}
		
		// If a player is within 40 of a spawned boss, track their stats
		if(conf.getKeys(false).contains(name)) {
			for(Entity entity : e.getEntity().getNearbyEntities(30, 100, 30)) {
				if(entity instanceof Player) {
					selfHealed.put(entity.getName(), 0.0);
					allyHealed.put(entity.getName(), 0.0);
					damageDealt.get(name).put(entity.getName(), 0.0);
					damageTaken.get(name).put(entity.getName(), 0.0);
					inBoss.put(entity.getName(), name);
				}
			}
		}
	}
	
	@EventHandler
	public void onMMReload(MythicReloadedEvent e) {
	  	manager = MythicMobs.inst().getMobManager();
	  	helper = MythicMobs.inst().getAPIHelper();
	}
	
	@EventHandler
	public void onSkillHeal(SkillHealEvent e) {
		if(inBoss.containsKey(e.getHealer().getName())) {
			// Self healing
			if(e.getHealer().getName().equalsIgnoreCase(e.getTarget().getName())) {
				double prevHeal = selfHealed.get(e.getHealer().getName());
				selfHealed.put(e.getHealer().getName(), prevHeal + e.getAmount());
			}
			else {
				double prevHeal = allyHealed.get(e.getHealer().getName());
				allyHealed.put(e.getHealer().getName(), prevHeal + e.getAmount());
			}
		}
	}
}
