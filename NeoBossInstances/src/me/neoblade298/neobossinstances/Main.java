package me.neoblade298.neobossinstances;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;

public class Main extends JavaPlugin implements Listener {

	// Config items
	File file = null;
	FileConfiguration conf = null;
	String returnCommand = null;
	String sendCommand = null;
	int cmdDelay = 0;
	boolean isInstance = false;
	String instanceName = null;
	Plugin main = this;
	Location mainSpawn = null;
	Location instanceSpawn = null;
	boolean isDebug = false;
	
	// SQL
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";
	
	static String clearScoreboardCmd = "scoreboard players reset %player%";

	// Databases
	// Cooldowns: Key is boss name, payload is ConcurrentHashMap where key is playername and
	// payload is last fought
	public ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> cooldowns = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>();
	public ConcurrentHashMap<String, Boss> bossInfo = new ConcurrentHashMap<String, Boss>();
	public ConcurrentHashMap<String, Long> dropCooldown = new ConcurrentHashMap<String, Long>();
	public ArrayList<String> bossNames = new ArrayList<String>();
	ArrayList<String> instanceNames = null;
	ArrayList<String> activeBosses = new ArrayList<String>();
	public ConcurrentHashMap<String, ArrayList<Player>> activeFights = new ConcurrentHashMap<String, ArrayList<Player>>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("boss").setExecutor(new Commands(this));

		// Save config if doesn't exist
		file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		conf = YamlConfiguration.loadConfiguration(file);

		// Load values from config
		sendCommand = getConfig().getString("Send_Command");
		returnCommand = getConfig().getString("Return_Command");
		cmdDelay = getConfig().getInt("Command_Delay");
		isInstance = getConfig().getBoolean("Is_Instance");
		instanceName = getConfig().getString("Instance_Name");
		mainSpawn = parseLocation(getConfig().getString("Main_Spawn"));
		instanceSpawn = parseLocation(getConfig().getString("Instance_Spawn"));

		ConfigurationSection bosses = getConfig().getConfigurationSection("Bosses");
		instanceNames = (ArrayList<String>) getConfig().getStringList("Instances");

		// If not an instance, set up player cooldowns
		if (!isInstance) {
			try {
				Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				Statement stmt = con.createStatement();
				ResultSet rs;
				
				for (String boss : bosses.getKeys(false)) {
					ConcurrentHashMap<String, Long> cds = new ConcurrentHashMap<String, Long>();
					cooldowns.put(boss, cds);
					bossNames.add(boss);
					rs = stmt.executeQuery("SELECT * FROM neobossinstances_cds WHERE boss = '" + boss + "';");
					while (rs.next()) {
						cds.put(rs.getString(1), rs.getLong(3));
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Populate boss and raid  information
		for (String boss : bosses.getKeys(false)) {
			ConfigurationSection bossSection = bosses.getConfigurationSection(boss);
			int cooldown = bossSection.getInt("Cooldown");
			String cmd = bossSection.getString("Command");
			String displayName = bossSection.getString("Display-Name");
			boolean isRaid = bossSection.getBoolean("Is-Raid");
			int timeLimit = bossSection.getInt("Time-Limit");
			
			Location loc = parseLocation(bossSection.getString("Coordinates"));
			if (isRaid) {
				bossInfo.put(boss, new Boss(loc, cmd, cooldown, displayName, isRaid, timeLimit));
			}
			else {
				bossInfo.put(boss, new Boss(loc, cmd, cooldown, displayName));
			}
		}

		Bukkit.getServer().getLogger().info("NeoBossInstances Enabled");
	}

	public void onDisable() {
		// If not instance, save cooldowns from ConcurrentHashMap to SQL
		// Only save cooldowns that still matter (still on cooldown)
		if (!isInstance) {
			try {
				Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				Statement stmt = con.createStatement();
				
				// First clear all the cooldowns on the SQL currently
				stmt.executeUpdate("delete from neobossinstances_cds;");
				
				// Then add the cooldowns from the ConcurrentHashMap into SQL
				for (String boss : cooldowns.keySet()) {
					int cooldown = bossInfo.get(boss).getCooldown();
					ConcurrentHashMap<String, Long> lastFought = cooldowns.get(boss);
					for (String uuid : lastFought.keySet()) {
						// Only add to the cooldown list if it's still relevant
						if ((System.currentTimeMillis() - lastFought.get(uuid)) < (cooldown * 1000)) {
							stmt.executeUpdate("INSERT INTO neobossinstances_cds VALUES ('" + uuid + "','" + boss + "'," + lastFought.get(uuid) + ")");
						}
					}
				}
				
				int deleted = stmt.executeUpdate("delete from neobossinstances_fights;");
				Bukkit.getServer().getLogger().info("Cleared " + deleted + " fights from NeoBossInstances");
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		Bukkit.getServer().getLogger().info("NeoBossInstances Disabled");
	}
	
	public Location parseLocation(String toParse) {
		String[] sloc = toParse.split(" ");
		double x = Double.parseDouble(sloc[0]);
		double y = Double.parseDouble(sloc[1]);
		double z = Double.parseDouble(sloc[2]);
		float pitch = Float.parseFloat(sloc[3]);
		float yaw = Float.parseFloat(sloc[4]);
		return new Location(getServer().getWorld("Argyll"), x, y, z, yaw, pitch);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		// If instance, check where to send a player
		if (isInstance) {
			Player p = e.getPlayer();
			p.teleport(instanceSpawn);
			String uuid = p.getUniqueId().toString();
	    	// Remove all scoreboard tags
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), clearScoreboardCmd.replaceAll("%player%", p.getName()));
    		BukkitRunnable sendPlayer = new BukkitRunnable() {
    			int count = 0;
    			
    			@SuppressWarnings("deprecation")
				public void run() {
    				try {
    					// Connect
    					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
    					Statement stmt = con.createStatement();
    					ResultSet rs;

        				if (count > 3) {
        					this.cancel();
        				}
        				else {
	    					// Check where the player should be
	    					String boss;
	    					rs = stmt.executeQuery("SELECT *, COUNT(*) FROM neobossinstances_fights WHERE uuid = '" + uuid + "';");
	    					rs.next();
							boss = rs.getString(2);
	    					if (boss != null) {
	    						p.teleport(bossInfo.get(boss).getCoords());
	    						if (!activeFights.containsKey(boss)) {
	    							ArrayList<Player> players = new ArrayList<Player>();
	    							players.add(p);
	    							activeFights.put(boss, players);
	    						}
	    						else {
	    							activeFights.get(boss).add(p);
	    						}
	    						
	    						// Handle raid starts
	    						if (bossInfo.get(boss).isRaid()) {
	    				    		BukkitRunnable startRaid = new BukkitRunnable() {
	    				    			public void run() {
	    		    						p.setHealth(p.getMaxHealth());

	    		    						// Only start timer if it hasn't already been started
	    		    						if (!activeBosses.contains(boss)) {
	    		    							scheduleTimer(bossInfo.get(boss).getTimeLimit(), boss);
	    		    							activeBosses.add(boss);
	        	    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossInfo.get(boss).getCmd());
	    		    						}
	    				    			}
	    				    		};
	    				    		startRaid.runTaskLater(main, 60);
	    				    		this.cancel();
	    						}
	    						// Handle regular boss
	    						else {
	    				    		BukkitRunnable summonBoss = new BukkitRunnable() {
	    				    			public void run() {
	    		    						p.setHealth(p.getMaxHealth());
	    		    						
	    		    						// Only spawn boss if the fight is not currently active
	    		    						if (!activeBosses.contains(boss)) {
	    		    							activeBosses.add(boss);
	        	    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossInfo.get(boss).getCmd());
	    		    						}
	    				    			}
	    				    		};
	    				    		summonBoss.runTaskLater(main, cmdDelay * 20);
	    				    		this.cancel();
	    						}
	    					}
	    					// Retried 3 times, time to teleport them out
	    					else if (count >= 3 && !p.hasPermission("bossinstances.exemptjoin")) {
	    		    			p.sendMessage("§4[§c§lBosses§4] §7Something went wrong! Could not teleport you to boss.");
					    		BukkitRunnable returnPlayer = new BukkitRunnable() {
					    			public void run() {
		    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), returnCommand.replaceAll("%player%", p.getName()));
					    			}
					    		};
					    		returnPlayer.runTaskLater(main, 100L);
					    		count++;
	    					}
	    					// Task failed, retry
	    					else {
	    						count++;
	    					}
	    					
	    					con.close();
        				}
    				}
    				catch (Exception e) {
    					e.printStackTrace();
    				}
				}
    		};
    		sendPlayer.runTaskTimer(this, 60L, 60L);
		}
	}
	
	public void scheduleTimer(int time, String boss) {
		int ticks = time * 20;
		// 30 minute warning
		if (time > 1800) {
			scheduleWarning(ticks, 36000, "§e30 §cminutes", boss);
		}
		// 15 minute warning
		if (time > 900) {
			scheduleWarning(ticks, 18000, "§e15 §cminutes", boss);
		}
		scheduleWarning(ticks, 6000, "§e5 §cminutes", boss);
		scheduleWarning(ticks, 3600, "§e3 §cminutes", boss);
		scheduleWarning(ticks, 2400, "§e2 §cminutes", boss);
		scheduleWarning(ticks, 1200, "§e1 §cminute", boss);

		BukkitRunnable kickPlayer = new BukkitRunnable() {
			public void run() {
				if (activeFights.containsKey(boss)) {
    				for (Player p : activeFights.get(boss)) {
    					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), returnCommand.replaceAll("%player%", p.getName()));
    				}
				}
			}
		};
		kickPlayer.runTaskLater(main, ticks);
	}
	
	public void scheduleWarning(int ticks, int timeToWarn, String time, String boss) {
		BukkitRunnable warnPlayer = new BukkitRunnable() {
			public void run() {
				if (activeFights.containsKey(boss)) {
    				for (Player p : activeFights.get(boss)) {
    					p.sendMessage("§4[§c§lMLMC§4] " + time + " remaining!");
    				}
				}
			}
		};
		warnPlayer.runTaskLater(main, ticks - timeToWarn);
	}
	
	public String findInstance(String boss) {
		try {
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			ResultSet rs;
			ArrayList<String> instanceNamesCopy = new ArrayList<String>(instanceNames);
			Collections.shuffle(instanceNamesCopy);
			for (String instance : instanceNamesCopy) {
				rs = stmt.executeQuery("SELECT * FROM neobossinstances_fights WHERE boss = '" + boss + "' AND instance = '" + instance + "';");
				if (!rs.next()) {
					return instance;
				}
			}
			return "Not found";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "Failed to connect";
	}
	
	public void handleLeave(Player p) {
		// Remove player from all fights locally
		for (String boss : activeFights.keySet()) {
			activeFights.get(boss).remove(p);
			if (activeFights.get(boss).size() == 0) {
				activeFights.remove(boss);
				activeBosses.remove(boss);
			}
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), clearScoreboardCmd.replaceAll("%player%", p.getName()));
    	// Delete player from all fights in sql
		String uuid = p.getUniqueId().toString();
		try {
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			stmt.executeUpdate("delete from neobossinstances_fights WHERE uuid = '" + uuid + "';");
			
			con.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		BukkitRunnable handle = new BukkitRunnable() {
			public void run() {
				// Check if player is still here
				if (Bukkit.getPlayer(p.getName()) != null) {
					if (p.isDead()) {
						p.spigot().respawn();
					}
					if (p.hasPermission("bossinstances.exemptleave")) {
						p.teleport(instanceSpawn);
					}
			    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), returnCommand.replaceAll("%player%", p.getName()));
				}
			}
		};
		handle.runTaskLater(main, 20L);
	}
	
	public boolean getCooldown(String name, Player p) {
		if (cooldowns.containsKey(name)) {
			int cooldown = bossInfo.get(name).getCooldown() * 1000;
			String displayName = bossInfo.get(name).getDisplayName();
			if (cooldowns.get(name).containsKey(p.getUniqueId().toString())) {
				long lastUse = cooldowns.get(name).get(p.getUniqueId().toString());
				long currTime = System.currentTimeMillis();
				if (currTime > lastUse + cooldown) {
	    			p.sendMessage("§4[§c§lBosses§4] §l" + displayName + " §7is off cooldown!");
				}
				else {
					double temp = (lastUse + cooldown - currTime) / 6000;
					temp /= 10;
	    			p.sendMessage("§4[§c§lBosses§4] §l" + displayName + " §7has §c" + temp + " §7minutes remaining!");
				}
			}
			else {
    			p.sendMessage("§4[§c§lBosses§4] §l" + displayName + " §7is off cooldown!");
			}
			return true;
		}
		else {
			p.sendMessage("§4[§c§lBosses§4] §7Invalid boss name!");
			return true;
		}
	}
	
	public ConcurrentHashMap<String, ArrayList<Player>> getActiveFights() {
		return activeFights;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (isInstance) {
			Player p = e.getEntity();
			BukkitRunnable save = new BukkitRunnable() {
				public void run() {
					SkillAPI.saveSingle(p);
				}
			};
			save.runTaskLater(main, 10L);
			handleLeave(e.getEntity());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (isInstance) {
			Player p = e.getPlayer();
			SkillAPI.saveSingle(p);
			handleLeave(p);
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (isInstance) {
			Player p = e.getPlayer();
			SkillAPI.saveSingle(p);
			handleLeave(p);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (isInstance) {
			String p = e.getPlayer().getName();
			if (!dropCooldown.containsKey(p) || dropCooldown.get(p) + 2000 < System.currentTimeMillis()) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cYou tried to drop something! Drop it again within 2 seconds to confirm!");
			}
			dropCooldown.put(p, System.currentTimeMillis());
		}
	}
}
