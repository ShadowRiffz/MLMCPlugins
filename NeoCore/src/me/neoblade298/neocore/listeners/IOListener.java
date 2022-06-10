package me.neoblade298.neocore.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.api.event.PlayerLoadCompleteEvent;
import com.sucy.skill.api.event.PlayerSaveEvent;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.IOComponent;

public class IOListener implements Listener {
	private static String connection;
	private static Properties properties;
	private static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	private static HashMap<String, IOComponent> components = new HashMap<String, IOComponent>();
	
	public IOListener(String connection, Properties properties) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		IOListener.connection = connection;
		IOListener.properties = properties;
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
	public void onPrejoin(AsyncPlayerPreLoginEvent e) {
		handlePreload(Bukkit.getOfflinePlayer(e.getUniqueId()));
	}

	@EventHandler
	public void onSkillAPILoad(PlayerLoadCompleteEvent e) {
		handleLoad(e.getPlayer());
	}
	
	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(connection, properties);
					Statement insert = con.createStatement();
					Statement delete = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						try {
							entry.getValue().savePlayer(p, insert, delete);
							delete.executeBatch();
							insert.executeBatch();
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
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void handlePreload(OfflinePlayer p) {
		new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(connection, properties);
					Statement stmt = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						try {
							entry.getValue().preloadPlayer(p, stmt);
							stmt.executeBatch();
						}
						catch (Exception ex) {
							Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle preload for component " + entry.getKey());
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void handleLoad(Player p) {
		new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(connection, properties);
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
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	public static void handleDisable() {
		try {
			Connection con = DriverManager.getConnection(connection, properties);
			Statement insert = con.createStatement();
			Statement delete = con.createStatement();
			
			// Any final cleanup
			for (Entry<String, IOComponent> entry : components.entrySet()) {
				try {
					Bukkit.getLogger().info("[NeoCore] Cleaning up component " + entry.getKey());
					entry.getValue().cleanup(insert, delete);
					delete.executeBatch();
					insert.executeBatch();
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
	
	public static Statement getStatement() {
		try {
			Connection con = DriverManager.getConnection(connection, properties);
			return con.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
