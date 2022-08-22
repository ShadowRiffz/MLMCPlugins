package me.neoblade298.neoplayerdata;

import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.listeners.IOListener;
import me.neoblade298.neoplayerdata.objects.PlayerFields;

public class NeoPlayerData extends JavaPlugin implements Listener, IOComponent {
	private HashMap<String, PlayerFields> kpdata;
	private HashMap<String, PlayerFields> updata;
	// SQL
	public boolean debug;
	public HashMap<UUID, Long> lastSave;
	private static NeoPlayerData inst;
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoPlayerData Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("pd").setExecutor(new Commands(this));
	    this.debug = false;
	    
	    IOListener.register(this, this);
	    loadConfig();
	    loadBuiltinData();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPlayerData Disabled");
	    super.onDisable();
	}
	
	public void loadConfig() {
	    this.kpdata = new HashMap<String, PlayerFields>();
	    this.lastSave = new HashMap<UUID, Long>();
	}
	
	private void loadBuiltinData() {
		
	}
	
	public PlayerFields createKeyedPlayerData(String key, Plugin plugin, boolean hidden) {
		if (kpdata.containsKey(key)) {
			Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Keyed player data " + key + " for plugin " + plugin.getName() + " already exists. Returning existing keyed player data.");
			return kpdata.get(key);
		}
		Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Created keyed player data of " + key + " for plugin " + plugin.getName() + ", hidden: " + hidden + ".");
		PlayerFields newkp = new PlayerFields(key, hidden);
		kpdata.put(key, newkp);
		return newkp;
	}
	
	public boolean change(String key, String subkey, String value, UUID uuid, boolean canAccessHidden) {
		if (!this.kpdata.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change keyed player data of " + key + "." + subkey + " for " + uuid + ". Keyed player data doesn't exist.");
			return false;
		}
		if (kpdata.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data is hidden.");
			return false;
		}
		return this.kpdata.get(key).change(subkey, value, uuid);
	}
	
	public boolean add(String key, String subkey, int value, UUID uuid, boolean canAccessHidden) {
		if (!this.kpdata.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change keyed player data of " + key + "." + subkey + " for " + uuid + ". Keyed player data doesn't exist.");
			return false;
		}
		if (kpdata.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data is hidden.");
			return false;
		}
		return this.kpdata.get(key).add(subkey, value, uuid);
	}
	
	public boolean reset(String key, String subkey, UUID uuid, boolean canAccessHidden) {
		if (this.kpdata.containsKey(key)) {
			return this.kpdata.get(key).reset(subkey, uuid);
		}
		if (kpdata.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data is hidden.");
			return false;
		}
		Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to reset keyed player data of " + key + "." + subkey + " for " + uuid + ". Keyed player data doesn't exist.");
		return false;
	}
	
	public PlayerFields getKeyedPlayerData(String key, boolean canAccessHidden) {
		if (!kpdata.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data doesn't exist.");
			return null;
		}
		if (kpdata.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data is hidden.");
			return null;
		}
		return kpdata.get(key);
	}
	
	public boolean exists(String key, String subkey, UUID uuid, boolean canAccessHidden) {
		if (!kpdata.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data doesn't exist.");
			return false;
		}
		if (kpdata.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get keyed player data of " + key + ". Keyed player data is hidden.");
			return false;
		}
		return kpdata.get(key).exists(subkey, uuid);
	}
	
	public HashMap<String, PlayerFields> getAllKeyedPlayerData() {
		return kpdata;
	}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {	}

	@Override
	public void loadPlayer(Player p, Statement stmt) {
		for (String key : kpdata.keySet()) {
			kpdata.get(key).load(stmt, p.getUniqueId());
		}
	}
	
	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		for (String key : kpdata.keySet()) {
			kpdata.get(key).save(insert, delete, p.getUniqueId());
		}
	}

	@Override
	public void cleanup(Statement insert, Statement delete) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			savePlayer(p, insert, delete);
		}
	}

	@Override
	public String getKey() {
		return "PlayerDataManager";
	}
	
	public static NeoPlayerData inst() {
		return inst;
	}
}
