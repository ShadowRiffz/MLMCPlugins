package me.neoblade298.neosettings.objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.neoblade298.neosettings.NeoSettings;

public class Settings {
	private final String key;
	private HashMap<UUID, HashMap<String, Value>> values;
	private HashMap<UUID, ArrayList<String>> changedValues;
	private HashMap<String, Object> defaults;
	private final boolean hidden;
	
	public Settings (NeoSettings main, String key, boolean hidden) {
		this.key = key;
		this.values = new HashMap<UUID, HashMap<String, Value>>();
		this.defaults = new HashMap<String, Object>();
		this.changedValues = new HashMap<UUID, ArrayList<String>>();
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
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + this.getKey() + "." + key + " for " + uuid + ". Key doesn't exist.");
			return null;
		}
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized. Returning default.");
			return defaults.get(key);
		}
		HashMap<String, Value> pValues = values.get(uuid);
		if (pValues.containsKey(key)) {
			// If value has expired, remove it
			if (pValues.get(key).getExpiration() < System.currentTimeMillis()) {
				pValues.remove(key);
			}
			else {
				return pValues.get(key).getValue();
			}
		}
		return defaults.get(key);
	}
	
	// Only happens on logout. If this changes, make sure to keep the UUID initialized!
	public void save(Connection con, Statement stmt, UUID uuid) {
		if (changedValues.containsKey(uuid)) {
			HashMap<String, Value> pValues = values.get(uuid);
			
			// Only save changed values
			for (String key : changedValues.get(uuid)) {
				Object value = pValues.get(key).getValue();
				long expiration = pValues.get(key).getExpiration();
				
				// Skip expired values
				if (pValues.get(key).getExpiration() < System.currentTimeMillis()) {
					continue;
				}
				
				
				try {
					if (value instanceof String) {
						stmt.addBatch("REPLACE INTO neosettings_strings VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "'," + expiration + ");");
					}
					else if (value instanceof Boolean) {
						stmt.addBatch("REPLACE INTO neosettings_strings VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "'," + expiration + ");");
					}
					else if (value instanceof Integer) {
						stmt.addBatch("REPLACE INTO neosettings_integers VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "'," + expiration + ");");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			values.remove(uuid);
			changedValues.remove(uuid);
		}
	}
	
	public void load(Connection con, UUID uuid) {
		HashMap<String, Value> pSettings = new HashMap<String, Value>();
		this.values.put(uuid, pSettings);
		this.changedValues.put(uuid, new ArrayList<String>());
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM neosettings_strings WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String subsetting = rs.getString(3);
				long expiration = rs.getLong(5);
				Object o = defaults.get(subsetting);
				Object value = null;
				if (o == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to load setting of " + this.getKey() + "." + subsetting + " for " + uuid + ". Key doesn't exist.");
					return;
				}
				else if (o instanceof String) {
					value = rs.getString(4);
				}
				else if (o instanceof Boolean) {
					value = rs.getBoolean(4);
				}
				
				if (value == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to load setting of " + this.getKey() + "." + subsetting + " for " + uuid + ". Value is null.");
				}
				pSettings.put(subsetting, new Value(value, expiration));
			}
			
			rs = stmt.executeQuery("SELECT * FROM neosettings_integers WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String subsetting = rs.getString(3);
				long expiration = rs.getLong(5);
				Object o = defaults.get(subsetting);
				Object value = null;
				if (o == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to load setting of " + this.getKey() + "." + subsetting + " for " + uuid + ". Key doesn't exist.");
					return;
				}
				else if (o instanceof Integer) {
					value = rs.getInt(4);
				}
				
				if (value == null) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to load setting of " + this.getKey() + "." + subsetting + " for " + uuid + ". Value is null.");
				}
				pSettings.put(subsetting, new Value(value, expiration));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addSetting(String key, Object def) {
		this.defaults.put(key, def);
	}
	
	public boolean changeSetting(String key, String v, UUID uuid) {
		return changeSetting(key, v, uuid, -1);
	}
	
	// If expiration is 0, the original expiration is kept
	public boolean changeSetting(String key, String v, UUID uuid, long expiration) {
		Object value = null;
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Subsetting doesn't exist.");
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
				if ((Integer) value > 100000 || (Integer) value < 1) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Value was out of bounds.");
					return false;
				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Couldn't convert string to class.");
			e.printStackTrace();
			return false;
		}
		
		if (value.equals(defaults.get(key))) {
			return resetSetting(key, uuid);
		}
		
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}

		changedValues.get(uuid).add(key);
		Value curr = values.get(uuid).get(key);
		curr.setValue(value);
		if (expiration != 0) {
			curr.setExpiration(expiration);
		}
		return true;
	}
	
	public boolean addToSetting(String key, int v, UUID uuid) {
		// Default to no expiration change
		return addToSetting(key, v, uuid, 0);
	}
	
	// Returns true if successful
	public boolean addToSetting(String key, int v, UUID uuid, long expiration) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Subsetting doesn't exist.");
			return false;
		}
		
		// Make sure the changed setting is an integer
		if (defaults.get(key).getClass() != Integer.class) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Subsetting was not of type Integer.");
			return false;
		}
		
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		
		int original = (int) defaults.get(key);
		if (values.containsKey(uuid)) {
			original = (int) values.get(uuid).get(key).getValue();
		}
		int newValue = original + v;
		
		if (newValue > 100000 || newValue < 1) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Value was out of bounds.");
			return false;
		}
		
		if (newValue == (int) defaults.get(key)) {
			return resetSetting(key, uuid);
		}

		changedValues.get(uuid).add(key);
		Value curr = values.get(uuid).get(key);
		curr.setValue(newValue);
		if (expiration != 0) {
			curr.setExpiration(expiration);
		}
		return true;
	}
	
	public boolean resetSetting(String key, UUID uuid) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to reset setting of " + this.getKey() + "." + key + " for " + uuid + ". Subsetting doesn't exist.");
			return false;
		}
		if (!values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		changedValues.get(uuid).add(key);
		values.get(uuid).remove(key);
		return true;
	}
	
	public boolean isHidden() {
		return hidden;
	}
}
