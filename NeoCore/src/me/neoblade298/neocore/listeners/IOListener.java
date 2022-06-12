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
	private static boolean canSave = true, canLoad = true, canPreload = true, canCleanup = true;
	
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
		save(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		save(e.getPlayer());
	}
	
	@EventHandler
	public void onSAPISave(PlayerSaveEvent e) {
		save(e.getPlayer());
	}

	@EventHandler
	public void onPrejoin(AsyncPlayerPreLoginEvent e) {
		preload(Bukkit.getOfflinePlayer(e.getUniqueId()));
	}

	@EventHandler
	public void onSkillAPILoad(PlayerLoadCompleteEvent e) {
		load(e.getPlayer());
	}
	
	private void save(Player p) {
		if (!canSave) {
			return;
		}
		
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
						if (entry.getValue().canSave()) {
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
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void preload(OfflinePlayer p) {
		if (!canPreload) {
			return;
		}
		
		new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(connection, properties);
					Statement stmt = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						if (entry.getValue().canPreload()) {
							try {
								entry.getValue().preloadPlayer(p, stmt);
								stmt.executeBatch();
							}
							catch (Exception ex) {
								Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle preload for component " + entry.getKey());
								ex.printStackTrace();
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void load(Player p) {
		if (!canLoad) {
			return;
		}
		
		new BukkitRunnable() {
			public void run() {
				try {
					Connection con = DriverManager.getConnection(connection, properties);
					Statement stmt = con.createStatement();

					// Save account
					for (Entry<String, IOComponent> entry : components.entrySet()) {
						if (entry.getValue().canLoad()) {
							try {
								entry.getValue().loadPlayer(p, stmt);
								stmt.executeBatch();
							}
							catch (Exception ex) {
								Bukkit.getLogger().log(Level.WARNING, "[NeoCore] Failed to handle load for component " + entry.getKey());
								ex.printStackTrace();
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	public static void handleDisable() {
		if (!canCleanup) {
			return;
		}
		
		try {
			Connection con = DriverManager.getConnection(connection, properties);
			Statement insert = con.createStatement();
			Statement delete = con.createStatement();
			
			// Any final cleanup
			for (Entry<String, IOComponent> entry : components.entrySet()) {
				if (entry.getValue().canCleanup()) {
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
				else {
					Bukkit.getLogger().info("[NeoCore] Skipping cleanup for component " + entry.getKey());
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
	
	public static void setCanSave(boolean canSave) {
		IOListener.canSave = canSave;
	}
	
	public static void setCanLoad(boolean canLoad) {
		IOListener.canLoad = canLoad;
	}
	
	public static void setCanPreload(boolean canPreload) {
		IOListener.canPreload = canPreload;
	}
	
	public static void setCanCleanup(boolean canCleanup) {
		IOListener.canCleanup = canCleanup;
	}
}
