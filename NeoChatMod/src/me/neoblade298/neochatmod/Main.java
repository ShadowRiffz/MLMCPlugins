package me.neoblade298.neochatmod;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	File file = null;
	FileConfiguration conf = null;
	ArrayList<String> bannedWords = null;
	
	boolean muteTutorial = false;
	String punishCmd = null; 

	int X_BOUND_1 = -1578;
	int X_BOUND_2 = -1168;
	int Z_BOUND_1 = 1243;
	int Z_BOUND_2 = 1805;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoChatMod Enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// Save config if doesn't exist
		file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		conf = YamlConfiguration.loadConfiguration(file);
		
		// Load settings
		muteTutorial = getConfig().getBoolean("mute-tutorial");
		punishCmd = getConfig().getString("punish-command");
		
		// Load banned words
		bannedWords = (ArrayList<String>) getConfig().getStringList("banned-words");
		for (String word : bannedWords) {
			word.toUpperCase();
		}
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoChatMod Disabled");
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e) {
		String msg = e.getMessage().toUpperCase();
		double x = e.getPlayer().getLocation().getX();
		double z = e.getPlayer().getLocation().getZ();
		World w = e.getPlayer().getWorld();
		
		// Check if they're in the tutorial world
		if (w.getName().equalsIgnoreCase("Argyll") &&
			(X_BOUND_1 <= x && x <= X_BOUND_2) &&
			(Z_BOUND_1 <= z && z <= Z_BOUND_2)) {
			e.setCancelled(true);
			return;
		}
		
		// Check if it contained a curse word
		for (String word : bannedWords) {
			if (msg.contains(word)) {
				e.setCancelled(true);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), punishCmd.replaceAll("%player%", e.getPlayer().getName()));
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPermission("mycommand.staff")) {
						p.sendMessage("§4[§c§lMLMC§4] §e" + e.getPlayer().getName() + " §cwas punished for saying: §7" + msg);
					}
				}
				return;
			}
		}
	}
}