package me.neoblade298.neobossinstances;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerAccounts;

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
	boolean disableFights = false;
	
	// SQL
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";

	// Databases
	// Cooldowns: Key is boss name, payload is ConcurrentHashMap where key is playername and
	// payload is last fought
	public ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> cooldowns = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>();
	public ConcurrentHashMap<String, Boss> bossInfo = new ConcurrentHashMap<String, Boss>();
	public ConcurrentHashMap<String, Long> dropCooldown = new ConcurrentHashMap<String, Long>();
	public ArrayList<String> bossNames = new ArrayList<String>();
	public ArrayList<String> raidBossesFought = new ArrayList<String>();
	ArrayList<String> instanceNames = null;
	ArrayList<String> activeBosses = new ArrayList<String>();
	public ConcurrentHashMap<String, ArrayList<Player>> activeFights = new ConcurrentHashMap<String, ArrayList<Player>>();
	public ConcurrentHashMap<String, ArrayList<BukkitRunnable>> activeWarnings = new ConcurrentHashMap<String, ArrayList<BukkitRunnable>>();
	public ConcurrentHashMap<UUID, Integer> spectatorAcc = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentHashMap<String, ArrayList<String>> healthbars = new ConcurrentHashMap<String, ArrayList<String>>();
	public ConcurrentHashMap<UUID, Boss> spectatorBoss = new ConcurrentHashMap<UUID, Boss>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("boss").setExecutor(new Commands(this));

	    loadConfig();

		Bukkit.getServer().getLogger().info("[NeoBossInstances] NeoBossInstances Enabled");
	}
	
	public void loadConfig() {
		// Clear existing databases
		cooldowns.clear();
		bossInfo.clear();
		activeBosses.clear();
		bossNames.clear();
		activeFights.clear();
		
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

		// Populate boss and raid  information
		for (String boss : bosses.getKeys(false)) {
			ConfigurationSection bossSection = bosses.getConfigurationSection(boss);
			int cooldown = bossSection.getInt("Cooldown");
			String cmd = bossSection.getString("Command");
			String displayName = bossSection.getString("Display-Name");
			boolean isRaid = bossSection.getBoolean("Is-Raid");
			int timeLimit = bossSection.getInt("Time-Limit");
			String permission = bossSection.getString("Permission");
			Location loc = parseLocation(bossSection.getString("Coordinates"));
			String placeholder = bossSection.getString("Placeholder");
			
			if (isRaid) {
				Boss info = new Boss(loc, cmd, cooldown, displayName, isRaid, timeLimit, permission, placeholder);
				
				// If the raid has extra bosses within it, add them to the boss info
				if (bossSection.contains("Bosses")) {
					ConfigurationSection raidBosses = bossSection.getConfigurationSection("Bosses");
					ArrayList<RaidBoss> raidBossList = new ArrayList<RaidBoss>();
					for (String raidBoss : raidBosses.getKeys(false)) {
						ConfigurationSection raidBossSection = raidBosses.getConfigurationSection(raidBoss);
						String rcmd = raidBossSection.getString("Command");
						Location rloc = parseLocation(raidBossSection.getString("Coordinates"));
						raidBossList.add(new RaidBoss(rloc, rcmd, raidBoss));
					}
					info.setRaidBosses(raidBossList);
				}
				bossInfo.put(boss, info);
			}
			else {
				bossInfo.put(boss, new Boss(loc, cmd, cooldown, displayName, permission, placeholder));
			}
		}
		
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
				Bukkit.getServer().getLogger().info("[NeoBossInstances] Cleared " + deleted + " fights from NeoBossInstances");
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		Bukkit.getServer().getLogger().info("[NeoBossInstances] NeoBossInstances Disabled");
	}
	
	public Location parseLocation(String toParse) {
		String[] sloc = toParse.split(" ");
		double x = Double.parseDouble(sloc[0]);
		double y = Double.parseDouble(sloc[1]);
		double z = Double.parseDouble(sloc[2]);
		float pitch = Float.parseFloat(sloc[3]);
		float yaw = Float.parseFloat(sloc[4]);
		World world = Bukkit.getServer().getWorld("Argyll");
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		// If instance, check where to send a player
		if (isInstance) {
			Player p = e.getPlayer();
			p.teleport(instanceSpawn);
			String uuid = p.getUniqueId().toString();
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
    							Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " sent to boss " + boss + ".");
	    						if (!activeFights.containsKey(boss)) {
	    							ArrayList<Player> players = new ArrayList<Player>();
	    							players.add(p);
	    							activeFights.put(boss, players);
	    						}
	    						else {
	    							activeFights.get(boss).add(p);
	    						}
	    						
	    						// Recalculate everyone's health bars every time someone joins
	    						for (Player partyMember : activeFights.get(boss)) {
	    							ArrayList<String> healthList = new ArrayList<String>();
		    						healthbars.put(partyMember.getName(), healthList);
		    						for (Player bossFighter : activeFights.get(boss)) {
		    							if (!bossFighter.equals(partyMember)) {
		    								healthList.add(bossFighter.getName());
		    							}
		    						}
		    						Collections.sort(healthList);
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
	        	    							for (RaidBoss raidBoss : bossInfo.get(boss).getRaidBosses()) {
	        	    								raidBossesFought.remove(raidBoss.getName());
	        	    							}
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
	    		    						if (!activeBosses.contains(boss) && p.isOnline()) {
	    		    							Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " spawned boss " + boss + ".");
	    		    							activeBosses.add(boss);
	        	    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossInfo.get(boss).getCmd());
	    		    						}
	    				    			}
	    				    		};
	    				    		summonBoss.runTaskLater(main, cmdDelay * 20);
	    				    		this.cancel();
	    						}
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
    		sendPlayer.runTaskTimer(this, 60L, 100L);
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
		if (activeWarnings.containsKey(boss)) {
			activeWarnings.get(boss).add(warnPlayer);
		}
		else {
			ArrayList<BukkitRunnable> list = new ArrayList<BukkitRunnable>();
			list.add(warnPlayer);
			activeWarnings.put(boss, list);
		}
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void handleLeave(Player p) {
		// Remove spectator mode
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vanish " + p.getName() + " off");
		p.setInvulnerable(false);
		p.setGameMode(GameMode.SURVIVAL);
		spectatorAcc.remove(p.getUniqueId());
		spectatorBoss.remove(p.getUniqueId());
		
		// Remove player from all fights locally
		healthbars.remove(p.getName());
		for (String boss : activeFights.keySet()) {
			if (activeFights.get(boss).contains(p)) {
				activeFights.get(boss).remove(p);
				// Remove health bar from everyone else in the boss fight
				for (Player player : activeFights.get(boss)) {
					healthbars.get(player.getName()).remove(p.getName());
				}
				Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " removed from boss " + boss + ".");
			}
			if (activeFights.get(boss).size() == 0) {
				Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " removed from boss " + boss + ", removed from list.");
				activeFights.remove(boss);
				activeBosses.remove(boss);
				if (activeWarnings.containsKey(boss)) {
					for (BukkitRunnable runnable : activeWarnings.get(boss)) {
						if (!runnable.isCancelled()) {
							runnable.cancel();
						}
					}
					activeWarnings.remove(boss);
				}
			}
		}
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
	
	public String getCooldownPlaceholder(String name, Player p) {
		if (cooldowns.containsKey(name)) {
			if (!p.hasPermission(bossInfo.get(name).getPermission())) {
				return "§c???";
			}
			String msg = bossInfo.get(name).getPlaceholder() + "§7: ";
			int cooldown = bossInfo.get(name).getCooldown() * 1000;
			if (cooldowns.get(name).containsKey(p.getUniqueId().toString())) {
				long lastUse = cooldowns.get(name).get(p.getUniqueId().toString());
				long currTime = System.currentTimeMillis();
				if (currTime > lastUse + cooldown) {
	    			return msg + "§aReady!";
				}
				else {
					double temp = (lastUse + cooldown - currTime) / 1000;
					return msg + "§c" + temp + "s";
				}
			}
			else {
    			return msg + "§aReady!";
			}
		}
		return "Loading...";
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
					if (p != null) {
						healthbars.remove(p.getName());
						for (String boss : activeFights.keySet()) {
							if (activeFights.get(boss).contains(p)) {
								spectatorBoss.put(p.getUniqueId(), bossInfo.get(boss));
								activeFights.get(boss).remove(p);
								Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " removed from boss " + boss + ".");
								for (Player player : activeFights.get(boss)) {
									healthbars.get(player.getName()).remove(p.getName());
								}
							}
							if (activeFights.get(boss).size() == 0) {
								Bukkit.getServer().getLogger().info("[NeoBossInstances] " + p.getName() + " removed from boss " + boss + ", removed from list.");
								activeFights.remove(boss);
								activeBosses.remove(boss);
								if (activeWarnings.containsKey(boss)) {
									for (BukkitRunnable runnable : activeWarnings.get(boss)) {
										if (!runnable.isCancelled()) {
											runnable.cancel();
										}
									}
									activeWarnings.remove(boss);
								}
							}
						}
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
					}
				}
			};
			save.runTaskAsynchronously(this);
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.teleport(spectatorBoss.get(p.getUniqueId()).getCoords()); // Tp after death to boss
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vanish " + p.getName() + " on");
		p.setGameMode(GameMode.ADVENTURE);
		p.setInvulnerable(true);
		PlayerAccounts accs = SkillAPI.getPlayerAccountData(p);
		spectatorAcc.put(p.getUniqueId(), accs.getActiveId());
		SkillAPI.getPlayerAccountData(p).setAccount(13);
		p.sendMessage("§4[§c§lMLMC§4] §7You died! You can now spectate, or leave with §c/boss return§7.");

	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (isInstance) {
			Player p = e.getPlayer();
			if (spectatorAcc.containsKey(p.getUniqueId())) {
				SkillAPI.getPlayerAccountData(p).setAccount(spectatorAcc.get(p.getUniqueId()));
			}
			if (SkillAPI.getPlayerData(p).getSkillBar().isEnabled()) {
				SkillAPI.getPlayerData(p).getSkillBar().toggleEnabled();
			}
			
			SkillAPI.saveSingle(p);
			handleLeave(p);
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (isInstance) {
			Player p = e.getPlayer();
			if (spectatorAcc.containsKey(p.getUniqueId())) {
				SkillAPI.getPlayerAccountData(p).setAccount(spectatorAcc.get(p.getUniqueId()));
			}
			if (SkillAPI.getPlayerData(p).getSkillBar().isEnabled()) {
				SkillAPI.getPlayerData(p).getSkillBar().toggleEnabled();
			}
			
			SkillAPI.saveSingle(p);
			handleLeave(p);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (isInstance) {
			String p = e.getPlayer().getName();
			if (spectatorAcc.containsKey(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cCan't drop items when you're dead!");
				return;
			}
			else if (!dropCooldown.containsKey(p) || dropCooldown.get(p) + 2000 < System.currentTimeMillis()) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cYou tried to drop something! Drop it again within 2 seconds to confirm!");
			}
			dropCooldown.put(p, System.currentTimeMillis());
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (spectatorAcc.containsKey(p.getUniqueId())) e.setCancelled(true);
		}
	}
	
	public ArrayList<String> getHealthBars(Player p) {
		return healthbars.get(p.getName());
	}
}
