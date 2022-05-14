package me.neoblade298.neoquests.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.io.IOComponent;

public class IOListeners implements Listener {
	static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	static ArrayList<IOComponent> components = new ArrayList<IOComponent>();
	
	public static void addComponent(IOComponent component) {
		components.add(component);
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
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
					Statement stmt = con.createStatement();

					// Save account
					for (IOComponent component : components) {
						try {
							component.savePlayer(p, stmt);
							stmt.executeBatch();
						}
						catch (Exception ex) {
							Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to handle save for component " + component.getComponentName());
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		save.runTaskAsynchronously(NeoQuests.inst());
	}
	
	private void handleLoad(OfflinePlayer p) {
		BukkitRunnable load = new BukkitRunnable() {
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
					Statement stmt = con.createStatement();

					// Save account
					for (IOComponent component : components) {
						try {
							component.loadPlayer(p, stmt);
						}
						catch (Exception ex) {
							Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to handle load for component " + component.getComponentName());
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		load.runTaskAsynchronously(NeoQuests.inst());
	}
	
	public void handleDisable() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
			Statement stmt = con.createStatement();

			// Save account
			for (IOComponent component : components) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					try {
						component.savePlayer(p, stmt);
						stmt.executeBatch();
					}
					catch (Exception ex) {
						Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to handle disable for component " + component.getComponentName());
						ex.printStackTrace();
					}
				}
			}
			
			// Any final cleanup
			for (IOComponent component : components) {
				try {
					
				}
				catch (Exception ex) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to handle cleanup for component " + component.getComponentName());
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
