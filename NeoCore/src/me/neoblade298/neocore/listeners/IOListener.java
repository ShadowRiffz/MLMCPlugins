package me.neoblade298.neocore.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.api.event.PlayerSaveEvent;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.IOComponent;

public class IOListener implements Listener {
	private static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	private static HashMap<String, IOComponent> components = new HashMap<String, IOComponent>();
	
	public IOListener() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void register(JavaPlugin plugin, IOComponent component) {
		components.put(plugin.getName() + "-" + component.getKey(), component);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onSAPISave(PlayerSaveEvent e) {
		handleLeave(e.getPlayer());
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		handleLoad(Bukkit.getOfflinePlayer(e.getUniqueId()));
	}
	
	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(NeoCore.connection, NeoCore.properties);
					Statement stmt = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						try {
							entry.getValue().savePlayer(p, stmt);
							stmt.executeBatch();
						}
						catch (Exception ex) {
							Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle save for component " + entry.getKey());
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		save.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void handleLoad(OfflinePlayer p) {
		BukkitRunnable load = new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(NeoCore.connection, NeoCore.properties);
					Statement stmt = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						try {
							entry.getValue().loadPlayer(p, stmt);
							stmt.executeBatch();
						}
						catch (Exception ex) {
							Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle load for component " + entry.getKey());
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		load.runTaskAsynchronously(NeoCore.inst());
	}
	
	public static void handleDisable() {
		try {
			Connection con = DriverManager.getConnection(NeoCore.connection, NeoCore.properties);
			Statement stmt = con.createStatement();
			
			// Any final cleanup
			for (Entry<String, IOComponent> entry : components.entrySet()) {
				try {
					Bukkit.getLogger().info("[NeoCore] Cleaning up component " + entry.getKey());
					entry.getValue().cleanup(stmt);
					stmt.executeBatch();
				}
				catch (Exception ex) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle cleanup for component " + entry.getKey());
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
