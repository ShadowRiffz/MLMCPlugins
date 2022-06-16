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

public class PlayerFields {
	private final String key;
	private HashMap<UUID, HashMap<String, Value>> values;
	private HashMap<UUID, HashSet<String>> changedValues;
	private HashMap<String, Object> defaults;
	private final boolean hidden;
	
	public PlayerFields (String key, boolean hidden) {
		this.key = key;
		this.values = new HashMap<UUID, HashMap<String, Value>>();
		this.defaults = new HashMap<String, Object>();
		this.changedValues = new HashMap<UUID, HashSet<String>>();
		this.hidden = hidden;
	}
	
	public String getKey() {
		return key;
	}
	
	public Set<String> getAllKeys() {
		return this.defaults.keySet();
	}
	
	public Object getValue(UUID uuid, String key) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get field " + this.getKey() + "." + key + " for " + uuid + ". Subkey doesn't exist.");
			return null;
		}
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get field " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized. Returning default.");
			return defaults.get(key);
		}
		HashMap<String, Value> pValues = values.get(uuid);
		if (pValues.containsKey(key)) {
			// If value has expired, remove it
			if (pValues.get(key).isExpired()) {
				pValues.remove(key);
				changedValues.get(uuid).add(key);
			}
			else {
				return pValues.get(key).getValue();
			}
		}
		return defaults.get(key);
	}
	
	public Object getDefault(String key) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get default field " + this.getKey() + "." + key + ". Subkey doesn't exist.");
			return null;
		}
		return defaults.get(key);
	}
	
	public boolean exists(String subkey, UUID uuid) {
		if (!defaults.containsKey(subkey)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get field " + this.getKey() + "." + subkey + " for " + uuid + ". Subkey doesn't exist.");
			return false;
		}
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to get field " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
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
				
				// If value was changed to something else
				if (pValues.containsKey(key)) {
					Object value = pValues.get(key).getValue();
					
					// Skip and remove expired values
					if (pValues.get(key).isExpired()) {
						pValues.remove(key);
						continue;
					}
					long expiration = pValues.get(key).getExpiration();
					
					
					try {
						Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Saving " + this.getKey() + "." + key + " to " + value + " for " + uuid + ".");
						if (value instanceof String) {
							insert.addBatch("REPLACE INTO NeoPlayerData_strings VALUES ('" + uuid + "','" + this.getKey()
							+ "','" + key + "','" + value + "'," + expiration + ");");
						}
						else if (value instanceof Boolean) {
							insert.addBatch("REPLACE INTO NeoPlayerData_strings VALUES ('" + uuid + "','" + this.getKey()
							+ "','" + key + "','" + value + "'," + expiration + ");");
						}
						else if (value instanceof Integer) {
							insert.addBatch("REPLACE INTO NeoPlayerData_integers VALUES ('" + uuid + "','" + this.getKey()
							+ "','" + key + "','" + value + "'," + expiration + ");");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// If value was set back to default
				else {
					Object def = defaults.get(key);
					Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Defaulting " + this.getKey() + "." + key + " to " + def + " for " + uuid + ".");
					try {
						if (def instanceof String || def instanceof Boolean) {
							delete.addBatch("DELETE FROM NeoPlayerData_strings WHERE setting = '" + this.getKey() + "' AND Subkey = '" + key +
							"';");
						}
						else if (def instanceof Integer) {
							delete.addBatch("DELETE FROM NeoPlayerData_integers WHERE setting = '" + this.getKey() + "' AND Subkey = '" + key +
							"';");
						}
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
		HashMap<String, Value> pFields = new HashMap<String, Value>();
		this.values.put(uuid, pFields);
		this.changedValues.put(uuid, new HashSet<String>());
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM NeoPlayerData_strings WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String Subkey = rs.getString(3);
				long expiration = rs.getLong(5);
				Object o = defaults.get(Subkey);
				Object value = null;
				if (o == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to load field " + this.getKey() + "." + Subkey + " for " + uuid + ". Subkey doesn't exist.");
					return;
				}
				else if (o instanceof String) {
					value = rs.getString(4);
				}
				else if (o instanceof Boolean) {
					value = rs.getBoolean(4);
				}
				
				if (value == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to load field " + this.getKey() + "." + Subkey + " for " + uuid + ". Value is null.");
				}
				if (NeoPlayerData.inst().debug) {
					Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Debug: Loading field: " + this.getKey() + "." + Subkey + " for " + uuid + ".");
				}
				
				if (expiration == -1 || expiration < System.currentTimeMillis()) {
					pFields.put(Subkey, new Value(value, expiration));
				}
			}
			
			rs = stmt.executeQuery("SELECT * FROM NeoPlayerData_integers WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String Subkey = rs.getString(3);
				long expiration = rs.getLong(5);
				Object o = defaults.get(Subkey);
				Object value = null;
				if (o == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to load field " + this.getKey() + "." + Subkey + " for " + uuid + ". Subkey doesn't exist.");
					return;
				}
				else if (o instanceof Integer) {
					value = rs.getInt(4);
				}
				
				if (value == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to load field " + this.getKey() + "." + Subkey + " for " + uuid + ". Value is null.");
				}
				if (NeoPlayerData.inst().debug) {
					Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Debug: Loading field: " + this.getKey() + "." + Subkey + " for " + uuid + ".");
				}
				pFields.put(Subkey, new Value(value, expiration));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void initialize(String key, Object def) {
		this.defaults.put(key, def);
	}
	
	public boolean change(String key, String v, UUID uuid) {
		return change(key, v, uuid, -1);
	}
	
	// If expiration is 0, the original expiration is kept
	public boolean change(String key, String v, UUID uuid, long expiration) {
		Object value = null;
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Subkey doesn't exist.");
			return false;
		}
		
		// Try to change String o into the proper class
		try {
			if (defaults.get(key).getClass() == Boolean.class) {
				value = Boolean.parseBoolean(v);
			}
			else if (defaults.get(key).getClass() == String.class) {
				value = v;
			}
			else if (defaults.get(key).getClass() == Integer.class) {
				value = Integer.parseInt(v);
				if ((Integer) value > 100000 || (Integer) value < -99) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Value was out of bounds.");
					return false;
				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Couldn't convert string to class.");
			e.printStackTrace();
			return false;
		}
		
		if (value.equals(defaults.get(key))) {
			return reset(key, uuid);
		}
		
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
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
			Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Changed field " + this.getKey() + "." + key + " for " + uuid + " to " +
					curr.getValue() + ".");
		}
		return true;
	}
	
	public boolean add(String key, int v, UUID uuid) {
		// Default to no expiration change
		return add(key, v, uuid, 0);
	}
	
	// Returns true if successful
	public boolean add(String key, int v, UUID uuid, long expiration) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Subkey doesn't exist.");
			return false;
		}
		
		// Make sure the changed field is an integer
		if (defaults.get(key).getClass() != Integer.class) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Subkey was not of type Integer.");
			return false;
		}
		
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		
		int original = (int) defaults.get(key);
		if (values.containsKey(uuid)) {
			original = (int) values.get(uuid).get(key).getValue();
		}
		int newValue = original + v;
		
		if (newValue > 100000 || newValue < 1) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". Value was out of bounds.");
			return false;
		}
		
		if (newValue == (int) defaults.get(key)) {
			return reset(key, uuid);
		}

		changedValues.get(uuid).add(key);
		Value curr = values.get(uuid).get(key);
		curr.setValue(newValue);
		if (expiration != 0) {
			curr.setExpiration(expiration);
		}
		if (NeoPlayerData.inst().debug) {
			Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Added " + v + " to field " + this.getKey() + "." + key + " for " + uuid + ". Before: " +
					original + ", after: " + curr.getValue() + ".");
		}
		return true;
	}
	
	public boolean reset(String key, UUID uuid) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to reset field " + this.getKey() + "." + key + " for " + uuid + ". Subkey doesn't exist.");
			return false;
		}
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoPlayerData] Failed to change field " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		changedValues.get(uuid).add(key);
		values.get(uuid).remove(key);
		Bukkit.getLogger().log(Level.INFO, "[NeoPlayerData] Reset field " + this.getKey() + "." + key + " for " + uuid + ".");
		return true;
	}
	
	public boolean isHidden() {
		return hidden;
	}
}
