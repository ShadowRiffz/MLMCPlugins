package me.neoblade298.neobossinstances;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neobossinstances.Objects.Boss;

public class Main extends JavaPlugin implements Listener {

	// Config items
	File file = null;
	FileConfiguration conf = null;
	String returnCommand = null;
	int cmdDelay = 0;
	boolean isInstance = false;
	String instanceName = null;
	
	// SQL
	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";

	// Databases
	// Cooldowns: Key is boss name, payload is hashmap where key is playername and
	// payload is last fought
	HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<String, HashMap<String, Long>>();
	HashMap<String, Boss> bossInfo = new HashMap<String, Boss>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		// Save config if doesn't exist
		file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		conf = YamlConfiguration.loadConfiguration(file);

		// Load values from config
		returnCommand = getConfig().getString("Return_Command");
		cmdDelay = getConfig().getInt("Command_Delay");
		isInstance = getConfig().getBoolean("Is_Instance");
		instanceName = getConfig().getString("Instance_Name");

		ConfigurationSection bosses = getConfig().getConfigurationSection("Bosses");

		// If not an instance, set up player cooldowns
		if (!isInstance) {
			for (String boss : bosses.getKeys(false)) {
				cooldowns.put(boss, new HashMap<String, Long>());
				// TODO: Clean all cooldowns longer than the boss cooldown from SQL
				// TODO: Place remaining cooldowns in cooldowns hashmap
			}
		}

		// Populate boss information
		for (String boss : bosses.getKeys(false)) {
			int cooldown = bosses.getInt("Cooldown");
			String cmd = bosses.getString("Command");
			String[] sloc = bosses.getString("Coordinates").split(" ");
			double x = Double.parseDouble(sloc[0]);
			double y = Double.parseDouble(sloc[1]);
			double z = Double.parseDouble(sloc[2]);
			float pitch = Float.parseFloat(sloc[3]);
			float yaw = Float.parseFloat(sloc[4]);
			Location loc = new Location(getServer().getWorld("Argyll"), x, y, z, yaw, pitch);
			bossInfo.put(boss, new Boss(loc, cmd, cooldown));
		}

		Bukkit.getServer().getLogger().info("NeoBossInstances Enabled");
	}

	public void onDisable() {
		// If not instance, save cooldowns from hashmap to SQL
		// Only save cooldowns that still matter (still on cooldown)
		if (!isInstance) {
			try {
				// Connect
				Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
				Statement stmt = con.createStatement();
				
				
				for (String boss : cooldowns.keySet()) {
					int cooldown = bossInfo.get(boss).getCooldown();
					HashMap<String, Long> lastFought = cooldowns.get(boss);
					for (String uuid : lastFought.keySet()) {
						if ((System.currentTimeMillis() - lastFought.get(uuid)) < cooldown) {
							stmt.executeUpdate("INSERT INTO neobossinstances_cds VALUES ('" + uuid + "','" + boss + "'," + lastFought.get(uuid) + ")");
						}
					}
				}
				
				// TODO: Clear all queues, fights, and instances in SQL
				int deleted = stmt.executeUpdate("delete from neobossinstances_fights;");
				Bukkit.getServer().getLogger().info("Cleared " + deleted + "fights from NeoBossInstances");
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		Bukkit.getServer().getLogger().info("NeoBossInstances Disabled");
	}
}
