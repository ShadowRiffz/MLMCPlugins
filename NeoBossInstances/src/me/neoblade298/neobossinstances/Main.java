package me.neoblade298.neobossinstances;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
	
	// SQL
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";

	// Databases
	// Cooldowns: Key is boss name, payload is hashmap where key is playername and
	// payload is last fought
	public HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<String, HashMap<String, Long>>();
	public HashMap<String, Boss> bossInfo = new HashMap<String, Boss>();
	ArrayList<String> instanceNames = null;
	ArrayList<String> activeBosses = new ArrayList<String>();

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
		Collections.reverse(instanceNames);

		// If not an instance, set up player cooldowns
		if (!isInstance) {
			try {
				Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				Statement stmt = con.createStatement();
				ResultSet rs;
				
				for (String boss : bosses.getKeys(false)) {
					HashMap<String, Long> cds = new HashMap<String, Long>();
					cooldowns.put(boss, cds);
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

		// Populate boss information
		for (String boss : bosses.getKeys(false)) {
			ConfigurationSection bossSection = bosses.getConfigurationSection(boss);
			int cooldown = bossSection.getInt("Cooldown");
			String cmd = bossSection.getString("Command");
			
			Location loc = parseLocation(bossSection.getString("Coordinates"));
			bossInfo.put(boss, new Boss(loc, cmd, cooldown));
		}

		Bukkit.getServer().getLogger().info("NeoBossInstances Enabled");
	}

	public void onDisable() {
		// If not instance, save cooldowns from hashmap to SQL
		// Only save cooldowns that still matter (still on cooldown)
		if (!isInstance) {
			try {
				Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				Statement stmt = con.createStatement();
				
				// First clear all the cooldowns on the SQL currently
				stmt.executeUpdate("delete from neobossinstances_cds;");
				
				// Then add the cooldowns from the hashmap into SQL
				for (String boss : cooldowns.keySet()) {
					int cooldown = bossInfo.get(boss).getCooldown();
					HashMap<String, Long> lastFought = cooldowns.get(boss);
					for (String uuid : lastFought.keySet()) {
						// Only add to the cooldown list if it's still relevant
						if ((System.currentTimeMillis() - lastFought.get(uuid)) < (cooldown * 1000)) {
							stmt.executeUpdate("INSERT INTO neobossinstances_cds VALUES ('" + uuid + "','" + boss + "'," + lastFought.get(uuid) + ")");
						}
					}
				}
				
				// TODO: Clear all queues, fights, and instances in SQL
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
			if (!p.hasPermission("bossinstances.admin")) {
	    		BukkitRunnable sendPlayer = new BukkitRunnable() {
	    			public void run() {
	    				try {
	    					// Connect
	    					Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
	    					Statement stmt = con.createStatement();
	    					ResultSet rs;
	
	    					// Check where the player should be
	    					String boss;
	    					rs = stmt.executeQuery("SELECT *, COUNT(*) FROM neobossinstances_fights WHERE uuid = '" + uuid + "';");
	    					rs.next();
							boss = rs.getString(2);
	    					if (boss != null) {
	    						p.teleport(bossInfo.get(boss).getCoords());
	    						// Execute the command if it was not already executed
	    						if (!activeBosses.contains(boss)) {
	    							activeBosses.add(boss);
	    				    		BukkitRunnable summonBoss = new BukkitRunnable() {
	    				    			public void run() {
	    	    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossInfo.get(boss).getCmd());
	    				    				activeBosses.remove(boss);
	    				    			}
	    				    		};
	    				    		summonBoss.runTaskLater(main, cmdDelay * 20);
	    						}
	    					}
	    					else {
	    		    			p.sendMessage("§4[§c§lBosses§4] §7Something went wrong! Could not teleport you to boss.");
					    		BukkitRunnable returnPlayer = new BukkitRunnable() {
					    			public void run() {
		    							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), returnCommand.replaceAll("%player%", p.getName()));
					    			}
					    		};
					    		returnPlayer.runTaskLater(main, 100L);
	    					}
	    					
	    					con.close();
	    				}
	    				catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		};
	    		sendPlayer.runTaskLater(this, 60L);
			}
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void handleLeave(Player p) {
		BukkitRunnable handle = new BukkitRunnable() {
			public void run() {
				if (p.isDead()) {
					p.spigot().respawn();
				}
				String uuid = p.getUniqueId().toString();
				p.teleport(instanceSpawn);
		    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), returnCommand.replaceAll("%player%", p.getName()));
		    	// Delete player from all fights
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
		};
		handle.runTaskLater(main, 20L);
	}
	
	public boolean getCooldown(String name, Player p) {
		if (cooldowns.containsKey(name)) {
			if (cooldowns.get(name).containsKey(p.getUniqueId().toString())) {
				long lastUse = cooldowns.get(name).get(p.getUniqueId().toString());
				long currTime = System.currentTimeMillis();
				int cooldown = bossInfo.get(name).getCooldown() * 1000;
				
				if (currTime > lastUse + cooldown) {
	    			p.sendMessage("§4[§c§lBosses§4] §l" + name + " §7is off cooldown!");
				}
				else {
					double temp = (lastUse + cooldown - currTime) / 6000;
					temp /= 10;
	    			p.sendMessage("§4[§c§lBosses§4] §l" + name + " §7has §c" + temp + " §7minutes remaining!");
				}
			}
			else {
    			p.sendMessage("§4[§c§lBosses§4] §l" + name + " §7is off cooldown!");
			}
			return true;
		}
		else {
			p.sendMessage("§4[§c§lBosses§4] §7Invalid boss name!");
			return true;
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (isInstance) {
			handleLeave(e.getEntity());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (isInstance) {
			handleLeave(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (isInstance) {
			handleLeave(e.getPlayer());
		}
	}
}
