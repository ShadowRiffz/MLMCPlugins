package me.Neoblade298.NeoConsumables.objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.Neoblade298.NeoConsumables.Consumables;

public class ConsumableManager {
	public static HashMap<UUID, PlayerCooldowns> cds;
	public static HashMap<UUID, DurationEffects> effects;

	public static void save(UUID uuid, Connection con, Statement stmt, boolean savingMultiple) {
		DurationEffects eff = ConsumableManager.effects.get(uuid);
		if (eff != null) {
			if (!eff.isRelevant()) {
				ConsumableManager.effects.remove(uuid);
				return;
			}
			
			try {
				stmt.addBatch("REPLACE INTO consumables_effects VALUES ('" + uuid + "','" + eff.getCons().getKey()
				+ "'," + eff.getStartTime() + ");");
				
				// Set to truye if you're saving several accounts at once
				if (!savingMultiple) {
					stmt.executeBatch();
				}
			} catch (Exception e) {
				Bukkit.getLogger().log(Level.WARNING, "Consumables failed to save effects for " + uuid);
				e.printStackTrace();
			}
		}
	}
	
	public static void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Consumables.connection, Consumables.properties);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				save(p.getUniqueId(), con, stmt, true);
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			Bukkit.getLogger().log(Level.WARNING, "Consumables failed to save-all");
			ex.printStackTrace();
		}
	}

	public static void loadPlayer(Consumables main, UUID uuid) {
		ConsumableManager.effects.remove(uuid);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Consumables.connection, Consumables.properties);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM consumables_effects WHERE uuid = '" + uuid + "';");
			if (rs.next()) {
				FoodConsumable cons = (FoodConsumable) Consumables.consumables.get(rs.getString(2));
				if (cons.isDuration()) {
					DurationEffects effs = new DurationEffects(main, cons, rs.getLong(3), uuid, new ArrayList<BukkitTask>());
					if (effs.isRelevant()) {
						ConsumableManager.effects.put(uuid, effs);
						effs.startEffects();
					}
				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Consumables failed to load effects for " + uuid);
			e.printStackTrace();
		}
	}
}
