package me.neoblade298.neoplayerdata.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.neoblade298.neoplayerdata.NeoPlayerData;

public class PlayerTags {
	private final String key;
	private HashMap<UUID, HashMap<String, Value>> values;
	private HashMap<UUID, HashSet<String>> changedValues;
	private final boolean hidden;
	
	public PlayerTags(String key, boolean hidden) {
		this.key = key;
		this.values = new HashMap<UUID, HashMap<String, Value>>();
		this.changedValues = new HashMap<UUID, HashSet<String>>();
		this.hidden = hidden;
	}
	
	public String getKey() {
		return key;
	}
	
	public Set<String> getAllKeys(UUID uuid) {
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get tag of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized. Returning default.");
			return null;
		}
		return this.values.get(uuid).keySet();
	}
	
	public boolean exists(String subkey, UUID uuid) {
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get tag of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		HashMap<String, Value> pValues = values.get(uuid);
		if (pValues.containsKey(subkey)) {
			// If value has expired, remove it
			if (pValues.get(subkey).isExpired()) {
				pValues.remove(subkey);
				changedValues.get(uuid).add(key);
				return false;
			}
			return true;
		}
		return false;
	}
	
	// Only happens on logout. If this changes, make sure to keep the UUID initialized!
	public void save(Statement insert, Statement delete, UUID uuid) {
		if (changedValues.containsKey(uuid) && !changedValues.get(uuid).isEmpty()) {
			HashMap<String, Value> pValues = values.get(uuid);
			if (NeoPlayerData.inst().debug) {
				Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Debug: Changed values: " + this.getKey() + " " + changedValues.get(uuid) + " for " + uuid + ".");
			}
			
			// Only save changed values
			for (String key : changedValues.get(uuid)) {
				
				// If value was set
				if (pValues.containsKey(key)) {
					Object value = pValues.get(key).getValue();
					
					// Skip and remove expired values
					if (pValues.get(key).isExpired()) {
						pValues.remove(key);
						continue;
					}
					long expiration = pValues.get(key).getExpiration();
					
					
					try {
						Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Saving tag " + this.getKey() + "." + key + " to " + value + " for " + uuid + ".");
						insert.addBatch("REPLACE INTO neocore_strings VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "'," + expiration + ");");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// If value was unset
				else {
					Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Removing tag " + this.getKey() + "." + key + " for " + uuid + ".");
					try {
						delete.addBatch("DELETE FROM neocore_strings WHERE setting = '" + this.getKey() + "' AND Subkey = '" + key +
						"';");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			values.remove(uuid);
			changedValues.remove(uuid);
		}
	}
	
	public void load(Statement stmt, UUID uuid) {
		HashMap<String, Value> pSettings = new HashMap<String, Value>();
		this.values.put(uuid, pSettings);
		this.changedValues.put(uuid, new HashSet<String>());
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM NeoPlayerData_strings WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String Subkey = rs.getString(3);
				long expiration = rs.getLong(5);
				String value = rs.getString(4);
				
				if (value == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to load tag of " + this.getKey() + "." + Subkey + " for " + uuid + ". Value is null.");
				}
				if (NeoPlayerData.inst().debug) {
					Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Debug: Loading tag: " + this.getKey() + "." + Subkey + " for " + uuid + ".");
				}
				
				if (expiration == -1 || expiration < System.currentTimeMillis()) {
					pSettings.put(Subkey, new Value(value, expiration));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean change(String key, String v, UUID uuid) {
		return change(key, v, uuid, -1);
	}
	
	// If expiration is 0, the original expiration is kept
	public boolean change(String key, String v, UUID uuid, long expiration) {
		String value = null;
		
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change tag of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}

		changedValues.get(uuid).add(key);
		
		Value curr = null;
		if (values.get(uuid).containsKey(key)) {
			curr = values.get(uuid).get(key);
			curr.setValue(value);
			if (expiration != 0) {
				curr.setExpiration(expiration);
			}
		}
		else {
			curr = new Value(value, expiration);
			values.get(uuid).put(key, curr);
		}
		
		if (NeoPlayerData.inst().debug) {
			Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Changed tag of " + this.getKey() + "." + key + " for " + uuid + " to " +
					curr.getValue() + ".");
		}
		return true;
	}
	
	public boolean reset(String key, UUID uuid) {
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change tag of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		changedValues.get(uuid).add(key);
		values.get(uuid).remove(key);
		Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Reset tag of " + this.getKey() + "." + key + " for " + uuid + ".");
		return true;
	}
	
	public boolean isHidden() {
		return hidden;
	}
}
