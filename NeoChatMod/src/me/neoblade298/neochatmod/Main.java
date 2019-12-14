package me.neoblade298.neochatmod;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.TownyChat.events.AsyncChatHookEvent;

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
		
		for (int i = 0; i < bannedWords.size(); i++) {
			bannedWords.set(i, bannedWords.get(i).toUpperCase());
		}
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoChatMod Disabled");
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onChat(AsyncChatHookEvent e) {
		String msg = e.getMessage();
		double x = e.getPlayer().getLocation().getX();
		double z = e.getPlayer().getLocation().getZ();
		World w = e.getPlayer().getWorld();
		
		// Check if they're in the tutorial world
		if (w.getName().equalsIgnoreCase("Argyll") &&
			(X_BOUND_1 <= x && x <= X_BOUND_2) &&
			(Z_BOUND_1 <= z && z <= Z_BOUND_2)) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (p.hasPermission("mycommand.staff")) {
					p.sendMessage("§4[§c§lMLMC§4] §c" + e.getPlayer().getName() + " §7spoke in tutorial: §c" + msg);
				}
			}
			e.setCancelled(true);
		}
		
		// Check if it contained a curse word
		for (String word : bannedWords) {
			if (msg.toUpperCase().contains(word)) {
				e.setCancelled(true);
				try {
					Bukkit.getScheduler().callSyncMethod(this, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), punishCmd.replaceAll("%player%", e.getPlayer().getName()))).get();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPermission("mycommand.staff")) {
						p.sendMessage("§4[§c§lMLMC§4] §c" + e.getPlayer().getName() + " §7was punished for saying: §c" + msg);
					}
				}
				return;
			}
		}
	}
}