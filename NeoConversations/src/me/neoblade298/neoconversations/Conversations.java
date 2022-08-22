package me.neoblade298.neoconversations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Conversations extends JavaPlugin implements org.bukkit.event.Listener {
	
	private YamlConfiguration cfg;
	public HashMap<String, ArrayList<String>> conversations;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoConversations Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("conv").setExecutor(new Commands(this));
	    
	    conversations = new HashMap<String, ArrayList<String>>();
	    loadConfig();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoConversations Disabled");
	    super.onDisable();
	}
	
	public void loadConfig() {
		File cfg = new File(getDataFolder(), "config.yml");
		
		// Save config if doesn't exist
		if (!cfg.exists()) {
			saveResource("config.yml", false);
		}
		this.cfg = YamlConfiguration.loadConfiguration(cfg);
		
		// Load config
		conversations.clear();
		ConfigurationSection convSec = this.cfg.getConfigurationSection("conversations");
		for (String convName : convSec.getKeys(false)) {
			ArrayList<String> conv = (ArrayList<String>) convSec.getStringList(convName);
			conversations.put(convName, conv);
		}
	}
	
	public void playConversation(ArrayList<String> conv, Player p) {
		long time = 0;
		for (int i = 0; i < conv.size(); i++) {
			String piece = conv.get(i);
			
			// Check for delay
			if (piece.startsWith("$")) {
				String delay = piece.substring(1, piece.indexOf(" ") - 1);
				time += Integer.parseInt(delay);
				piece = piece.substring(piece.indexOf(" ") + 1);
			}
			final String finalPiece = piece;
			
			BukkitRunnable convPiece = new BukkitRunnable() {
				public void run() {
					if (p != null) {
						if (finalPiece.startsWith("/")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalPiece.substring(1)
									.replaceAll("<player>", p.getName()));
						}
						else {
							p.sendMessage(finalPiece.replaceAll("<player>", p.getName()).replaceAll("&", "§"));
						}
					}
				}
			};
			convPiece.runTaskLater(this, time);
		}
	}
}
