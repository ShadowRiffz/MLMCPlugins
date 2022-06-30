package me.neoblade298.neocore.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import me.neoblade298.neocore.io.IOType;
import me.neoblade298.neocore.io.PostIOTask;

public class IOListener implements Listener {
	private static String connection;
	private static Properties properties;
	private static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	private static HashMap<String, IOComponent> components = new HashMap<String, IOComponent>();
	private static HashSet<IOType> disabledIO = new HashSet<IOType>();
	private static HashMap<IOType, HashSet<UUID>> performingIO = new HashMap<IOType, HashSet<UUID>>();
	private static HashMap<IOType, HashMap<UUID, ArrayList<PostIOTask>>> postIORunnables = new HashMap<IOType, HashMap<UUID, ArrayList<PostIOTask>>>();
	
	static {
		for (IOType type : IOType.values()) {
			performingIO.put(type, new HashSet<UUID>());
			postIORunnables.put(type, new HashMap<UUID, ArrayList<PostIOTask>>());
		}
	}
	
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
		if (disabledIO.contains(IOType.SAVE)) {
			return;
		}
		
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		IOType type = IOType.SAVE;
		performingIO.get(type).add(uuid);
		
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
				finally {
					endIOTask(type, p.getUniqueId());
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void preload(OfflinePlayer p) {
		IOType type = IOType.PRELOAD;
		if (disabledIO.contains(type)) {
			return;
		}
		performingIO.get(type).add(p.getUniqueId());
		
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
				finally {
					endIOTask(type, p.getUniqueId());
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	private void load(Player p) {
		IOType type = IOType.LOAD;
		if (disabledIO.contains(type)) {
			return;
		}
		performingIO.get(type).add(p.getUniqueId());
		
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
				finally {
					endIOTask(type, p.getUniqueId());
				}
			}
		}.runTaskAsynchronously(NeoCore.inst());
	}
	
	public static void handleDisable() {
		if (disabledIO.contains(IOType.CLEANUP)) {
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
	
	public static void disableIO(IOType type) {
		disabledIO.add(type);
	}

	public static void enableIO(IOType type) {
		disabledIO.remove(type);
	}
	
	public static void addPostIORunnable(BukkitRunnable task, IOType type, UUID uuid, boolean async) {
		ArrayList<PostIOTask> tasks = postIORunnables.get(type).getOrDefault(uuid, new ArrayList<PostIOTask>());
		tasks.add(new PostIOTask(task, async));
		postIORunnables.get(type).putIfAbsent(uuid,	tasks);
	}
	
	public static boolean isPerformingIO(UUID uuid, IOType type) {
		return performingIO.get(type).contains(uuid);
	}
	
	private static void endIOTask(IOType type, UUID uuid) {
		performingIO.get(type).remove(uuid);
		if (postIORunnables.get(type).containsKey(uuid)) {
			for (PostIOTask task : postIORunnables.get(type).get(uuid)) {
				if (task.isAsync()) {
					task.getRunnable().runTaskAsynchronously(NeoCore.inst());
				}
				else {
					task.getRunnable().runTask(NeoCore.inst());
				}
			}
			postIORunnables.get(type).remove(uuid);
		}
	}
}
