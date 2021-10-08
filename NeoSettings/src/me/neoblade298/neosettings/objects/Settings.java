package me.neoblade298.neosettings.objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Settings {
	private final String key;
	private HashMap<UUID, HashMap<String, Object>> values;
	private HashMap<String, Object> defaults;
	
	public Settings (String key) {
		this.key = key;
		this.values = new HashMap<UUID, HashMap<String, Object>>();
		this.defaults = new HashMap<String, Object>();
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue(UUID uuid, String key) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to get setting of " + this.getKey() + "." + key + " for " + uuid + ". Key doesn't exist.");
			return null;
		}
		if (values.containsKey(uuid)) {
			HashMap<String, Object> pValues = values.get(uuid);
			if (pValues.containsKey(key)) {
				return pValues.get(key);
			}
		}
		return defaults.get(key);
	}
	
	public void save(Connection con, Statement stmt, UUID uuid) {
		if (values.containsKey(uuid)) {
			HashMap<String, Object> pValues = values.get(uuid);
			for (String key : pValues.keySet()) {
				Object value = pValues.get(key);
				try {
					if (value instanceof String) {
						stmt.addBatch("REPLACE INTO neosettings_strings VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "');");
					}
					else if (value instanceof Boolean) {
						stmt.addBatch("REPLACE INTO neosettings_strings VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "');");
					}
					else if (value instanceof Double) {
						stmt.addBatch("REPLACE INTO neosettings_integer VALUES ('" + uuid + "','" + this.getKey()
						+ "','" + key + "','" + value + "');");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void load(Connection con, UUID uuid) {
		HashMap<String, Object> pSettings = new HashMap<String, Object>();
		this.values.put(uuid, pSettings);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM neosettings_strings WHERE uuid = '" + uuid + "' AND setting = '" + this.getKey() + "';");
			while (rs.next()) {
				String subsetting = rs.getString(3);
				Object o = defaults.get(subsetting);
				if (o == null) {
					Bukkit.getLogger().log(Level.WARNING, "Failed to load setting of " + this.getKey() + "." + subsetting + " for " + uuid + ". Key doesn't exist.");
					return;
				}
				else if (o instanceof String) {
					rs.getString(4);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addSetting(String key, Object def) {
		this.defaults.put(key, def);
	}
	
	// Returns true if successful
	public boolean changeSetting(String key, Object o, UUID uuid) {
		if (!defaults.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Key doesn't exist.");
			return false;
		}
		if (defaults.get(key).getClass() != o.getClass()) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". Class doesn't match.");
			return false;
		}
		if (values.containsKey(uuid)) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to change setting of " + this.getKey() + "." + key + " for " + uuid + ". UUID not initialized.");
			return false;
		}
		
		values.get(uuid).put(key, o);
		return true;
	}
}
