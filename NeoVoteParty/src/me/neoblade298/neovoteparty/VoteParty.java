package me.neoblade298.neovoteparty;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VoteParty extends JavaPlugin implements org.bukkit.event.Listener {
	public int count, voteparty, countdown, interval;
	public ArrayList<String> commands;
	public HashMap<Integer, String> specificNotifications;
	public HashMap<Integer, String> countdownNotifications;
	public String intervalNotification;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoVoteParty Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("vp").setExecutor(new Commands(this));
	    loadConfig();
	}
	
	public void onDisable() {
		getConfig().set("count", count);
		saveConfig();
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoVoteParty Disabled");
	    super.onDisable();
	}
	

	public ConfigurationSection loadConfig() {
		File file = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		ConfigurationSection cfg = YamlConfiguration.loadConfiguration(file);

		// count
		count = cfg.getInt("count");
		
		// general
		ConfigurationSection genCfg = cfg.getConfigurationSection("general");
		voteparty = genCfg.getInt("voteparty");
		countdown = genCfg.getInt("countdown");
		commands = (ArrayList<String>) genCfg.getStringList("voteparty-command");
		
		// notifications
		ConfigurationSection notCfg = cfg.getConfigurationSection("notifications");
		
		ConfigurationSection intCfg = notCfg.getConfigurationSection("interval");
		if (intCfg != null) {
			interval = intCfg.getInt("num");
			intervalNotification = intCfg.getString("message");
		}
		else {
			interval = -1;
		}
		
		ConfigurationSection specCfg = notCfg.getConfigurationSection("specific");
		specificNotifications = new HashMap<Integer, String>();
		for (String sNum : specCfg.getKeys(false)) {
			int num = Integer.parseInt(sNum);
			specificNotifications.put(num, specCfg.getString(sNum));
		}

		// countdown
		ConfigurationSection cdCfg = cfg.getConfigurationSection("countdown");
		countdownNotifications = new HashMap<Integer, String>();
		for (String sNum : cdCfg.getKeys(false)) {
			int num = Integer.parseInt(sNum);
			countdownNotifications.put(num, cdCfg.getString(sNum));
		}
		
		return cfg;
	}
	
	public void incrementVote(Player p) {
		count++;
		
		// First check if vote party is happening
		if (count >= voteparty) {
			commenceVoteParty();
			count = 0;
			return;
		}
		
		// Check for specific notifications
		if (specificNotifications.containsKey(count)) {
			String bc = specificNotifications.get(count);
			bc = bc.replaceAll("%player%", p.getName()).replaceAll("%votes%", Integer.toString(count))
					.replaceAll("%votesremaining%", Integer.toString(voteparty - count));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bc);
			return;
		}
		
		if (count % interval == 0) {
			String bc = intervalNotification;
			bc = bc.replaceAll("%player%", p.getName()).replaceAll("%votes%", Integer.toString(count))
					.replaceAll("%votesremaining%", Integer.toString(voteparty - count));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bc);
			return;
		}
	}
	
	public void commenceVoteParty() {
		BukkitRunnable startParty = new BukkitRunnable() {
			public void run() {
				for (String cmd : commands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
			}
		};
		startParty.runTaskLater(this, 20 * countdown);
		
		for (int num : countdownNotifications.keySet()) {
			if (num < countdown) {
				BukkitRunnable notif = new BukkitRunnable() {
					public void run() {
						String bc = countdownNotifications.get(num);
						bc = bc.replaceAll("%timeremaining%", Integer.toString(countdown - num));
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bc);
					}
				};
				notif.runTaskLater(this, 20 * num);
			}
		}
	}
	
}
