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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import com.sucy.skill.api.event.PlayerAttributeLoadEvent;
import com.sucy.skill.api.event.PlayerAttributeUnloadEvent;
import com.sucy.skill.api.event.PlayerLoadCompleteEvent;
import com.sucy.skill.api.event.PlayerSaveEvent;

import me.Neoblade298.NeoConsumables.Consumables;

public class ConsumableManager implements Listener {
	public static HashMap<UUID, PlayerCooldowns> cds = new HashMap<UUID, PlayerCooldowns>();
	public static HashMap<UUID, DurationEffects> effects = new HashMap<UUID, DurationEffects>();
	private static Consumables main;
	
	public ConsumableManager(Consumables main) {
		ConsumableManager.main = main;
	}

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
				
				// Set to true if you're saving several accounts at once
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

	public static void loadPlayer(UUID uuid) {
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
						// Don't start the effects until the player is actually loaded
						ConsumableManager.effects.put(uuid, effs);
					}
				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Consumables failed to load effects for " + uuid);
			e.printStackTrace();
		}
	}

	
	private void handleLeave(UUID uuid) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Consumables.connection, Consumables.properties);
			Statement stmt = con.createStatement();
			ConsumableManager.save(uuid, con, stmt, false);
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Consumables failed to handle leave for " + uuid);
			e.printStackTrace();
		}
	}
	
	private void endEffects(UUID uuid) {
		DurationEffects effs = ConsumableManager.effects.get(uuid);
		if (effs != null) {
			effs.endEffects(false);
		}
	}
	
	public void startEffects(UUID uuid) {
		DurationEffects effs = ConsumableManager.effects.get(uuid);
		if (effs != null) {
			effs.startEffects();
		}
	}
	
	@EventHandler
	public void onLoadSQL(PlayerLoadCompleteEvent e) {
		startEffects(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onSaveSQL(PlayerSaveEvent e) {
		handleLeave(e.getUUID());
	}
	
	@EventHandler
	public void onAttributeLoad(PlayerAttributeLoadEvent e) {
		startEffects(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onAttributeUnload(PlayerAttributeUnloadEvent e) {
		endEffects(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		loadPlayer(e.getUniqueId());
	}
}
