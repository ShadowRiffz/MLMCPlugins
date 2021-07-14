package me.neoblade298.neoresearch;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class Research extends JavaPlugin implements org.bukkit.event.Listener {
	// SQL
	public String url, user, pass;
	public HashMap<String, ResearchItem> researchItems;
	public HashMap<String, ArrayList<ResearchItem>> mobMap;
	public HashMap<String, String> displayNameMap;
	public HashMap<UUID, PlayerStats> playerStats;
	public HashMap<Integer, Integer> toNextLvl;
	public HashMap<UUID, Attributes> playerAttrs;
	public ArrayList<String> attrs;
	public Random rand;
	public boolean isInstance;

	public String broadcast, permcmd, levelup;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoResearch Enabled");
		if (!isInstance) getServer().getPluginManager().registerEvents(this, this);
		attrs = new ArrayList<String>(Arrays.asList("str", "dex", "int", "spr", "prc", "vit", "end"));
		this.getCommand("nr").setExecutor(new Commands(this));

		loadConfig();
	}

	public void onDisable() {
		saveAll();
		org.bukkit.Bukkit.getServer().getLogger().info("NeoResearch Disabled");
		super.onDisable();
	}

	public void loadConfig() {
		File file = new File(getDataFolder(), "config.yml");
		playerStats = new HashMap<UUID, PlayerStats>();
		playerAttrs = new HashMap<UUID, Attributes>();
		rand = new Random();

		// Save config if doesn't exist
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		ConfigurationSection cfg = YamlConfiguration.loadConfiguration(file);

		// SQL
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		url = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		user = sql.getString("username");
		pass = sql.getString("password");

		// General
		ConfigurationSection general = cfg.getConfigurationSection("general");
		broadcast = general.getString("research_complete_command").replaceAll("&", "§");
		permcmd = general.getString("permission_command");
		levelup = general.getString("research_levelup").replaceAll("&", "§");
		isInstance = general.getBoolean("is_instance");

		// Exp
		toNextLvl = new HashMap<Integer, Integer>();
		ConfigurationSection expSec = cfg.getConfigurationSection("exp");
		for (String lvl : expSec.getKeys(false)) {
			toNextLvl.put(Integer.parseInt(lvl), expSec.getInt(lvl));
		}

		// Load research items into mobMap, researchItems,
		researchItems = new HashMap<String, ResearchItem>();
		mobMap = new HashMap<String, ArrayList<ResearchItem>>();
		displayNameMap = new HashMap<String, String>();
		
		MobManager mm = MythicMobs.inst().getMobManager();
		ConfigurationSection rItems = cfg.getConfigurationSection("research_items");

		for (String rItem : rItems.getKeys(false)) {
			try {
				ConfigurationSection rItemSec = rItems.getConfigurationSection(rItem);
				ResearchItem researchItem = new ResearchItem(rItem);
				
				// exp
				researchItem.setExp(rItemSec.getInt("exp"));
				
				// attributes
				Attributes attributes = new Attributes();
				ConfigurationSection attrSec = rItemSec.getConfigurationSection("attributes");
				if (attrSec != null) {
					for (String attr : attrs) {
						attributes.setAttribute(attr, attrSec.getInt(attr));
					}
				}
				researchItem.setAttrs(attributes);
				
				// kill goals
				ConfigurationSection goalsSec = rItemSec.getConfigurationSection("goals");
				HashMap<String, Integer> goals = new HashMap<String, Integer>();
				for (String mob : goalsSec.getKeys(false)) {
					goals.put(mob, goalsSec.getInt(mob));
	
					// Add to mob map, research book min, display name map
					if (mobMap.containsKey(mob)) {
						mobMap.get(mob).add(researchItem);
						String mobdisplay = mob;
						if (mm.getMythicMob(mob) != null) {
							 mm.getMythicMob(mob).getDisplayName().get();
						}
						displayNameMap.put(mob, mobdisplay);
					}
					else {
						ArrayList<ResearchItem> list = new ArrayList<ResearchItem>();
						list.add(researchItem);
						mobMap.put(mob, list);
					}
				}
				researchItem.setGoals(goals);
				researchItems.put(rItem, researchItem);
			} catch (Exception e) {
				System.out.println("Failed: " + rItem);
			}
		}
		
		// Finally, load in all online players
		for (Player p : Bukkit.getOnlinePlayers()) {
			loadPlayer(p);
		}
	}

	@EventHandler
	public void onMobKill(MythicMobDeathEvent e) {
		if (e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			String mob = e.getMobType().getInternalName();

			// First update the mob kill stat
			PlayerStats stats = playerStats.get(p.getUniqueId());
			HashMap<String, Integer> mobKills = stats.getMobKills();
			int kills = mobKills.containsKey(mob) ? mobKills.get(mob) + 1 : 1;
			mobKills.put(mob, kills);

			// Update research points
			HashMap<String, Integer> researchPoints = stats.getResearchPoints();
			int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + 1 : 1;
			researchPoints.put(mob, points);

			checkItemCompletion(mob, p, points);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		loadPlayer(e.getPlayer());
	}
	
	private void loadPlayer(Player p) {
		UUID uuid = p.getUniqueId();
		
		// Add them to playerattrs
		if (!playerAttrs.containsKey(uuid)) {
			playerAttrs.put(uuid, new Attributes());
		}
			
		// Asynchronously look up sql and load it in
		Research main = this;
		BukkitRunnable load = new BukkitRunnable() {
			public void run() {
				int level = 5, exp = 0;
				HashMap<String, Integer> researchPoints = new HashMap<String, Integer>();
				HashMap<String, Integer> mobKills = new HashMap<String, Integer>();
				TreeSet<String> completedResearchItems = new TreeSet<String>();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(url, user, pass);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM research_updates WHERE uuid = '" + uuid + "';");
					
					// 2 cases to update:
					// 1. playerStats doesn't contain uuid
					// 2. playerStats contains uuid but rs not empty
					if (playerStats.containsKey(uuid) && rs.next()) {
						con.close();
						return;
					}
					rs = stmt.executeQuery("SELECT * FROM research_accounts WHERE uuid = '" + uuid + "';");
					
					// Load in account info
					if (rs.next()) {
						level = rs.getInt(2);
						exp = rs.getInt(3);
	
						rs = stmt.executeQuery("SELECT * FROM research_kills WHERE uuid = '" + uuid + "';");
						while (rs.next()) {
							researchPoints.put(rs.getString(2), rs.getInt(3));
						}
	
						rs = stmt.executeQuery("SELECT * FROM research_points WHERE uuid = '" + uuid + "';");
						while (rs.next()) {
							mobKills.put(rs.getString(2), rs.getInt(3));
						}
						
						rs = stmt.executeQuery("SELECT * FROM research_completed WHERE uuid = '" + uuid + "';");
						while (rs.next()) {
							completedResearchItems.add(rs.getString(2));
						}
	
						playerStats.put(uuid, new PlayerStats(main, level, exp, completedResearchItems, researchPoints, mobKills));
					}
					con.close();
				} catch (Exception ex) {
					System.out.println(ex);
				} finally {
					playerStats.put(uuid, new PlayerStats(main, level, exp, completedResearchItems, researchPoints, mobKills));
				}
			}
		};
		load.runTaskAsynchronously(this);
	}

	private void handleLeave(UUID uuid) {
		PlayerStats stats = playerStats.get(uuid);
		playerAttrs.remove(uuid);

		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				if (playerStats.containsKey(uuid)) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(url, user, pass);
						Statement stmt = con.createStatement();

						// Save account
						stmt.addBatch("REPLACE INTO research_accounts VALUES ('" + uuid + "','" + stats.getLevel()
						+ "','" + stats.getExp() + "');");

						// Save research points

						HashMap<String, Integer> mobKills = stats.getMobKills();
						for (String mob : mobKills.keySet()) {
							stmt.addBatch("REPLACE INTO research_kills values('" + uuid + "','" + mob + "'," + mobKills.get(mob) + ");");
						}
						HashMap<String, Integer> researchPoints = stats.getResearchPoints();
						for (String mob : researchPoints.keySet()) {
							stmt.addBatch("REPLACE INTO research_points values('" + uuid + "','" + mob + "'," + researchPoints.get(mob) + ");");
						}

						for (String item : stats.getCompletedResearchItems()) {
							stmt.addBatch("REPLACE INTO research_completed values('" + uuid + "','" + item + "');");
						}
						
						stmt.executeBatch();
						con.close();
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
		};
		save.runTaskAsynchronously(this);
	}
	
	private void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pass);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				UUID uuid = p.getUniqueId();
				PlayerStats stats = playerStats.get(uuid);
				if (playerStats.containsKey(p.getUniqueId())) {

					// Save account
					stmt.addBatch("REPLACE INTO research_accounts VALUES ('" + uuid + "','" + stats.getLevel()
					+ "','" + stats.getExp() + "');");

					// Save research points

					HashMap<String, Integer> mobKills = stats.getMobKills();
					for (String mob : mobKills.keySet()) {
						stmt.addBatch("REPLACE INTO research_kills values('" + uuid + "','" + mob + "'," + mobKills.get(mob) + ");");
					}
					HashMap<String, Integer> researchPoints = stats.getResearchPoints();
					for (String mob : researchPoints.keySet()) {
						stmt.addBatch("REPLACE INTO research_points values('" + uuid + "','" + mob + "'," + researchPoints.get(mob) + ");");
					}

					for (String item : stats.getCompletedResearchItems()) {
						stmt.addBatch("REPLACE INTO research_completed values('" + uuid + "','" + item + "');");
					}

				}
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onLeave(PlayerKickEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack main = p.getInventory().getItemInMainHand().clone();
		main.setAmount(1);

		if (main.getType().equals(Material.BOOK) && main.hasItemMeta() && main.getItemMeta().getCustomModelData() == 100 && main.getItemMeta().hasLore()) {
			String[] args = main.getItemMeta().getLore().get(0).split(" ");

			// "Grants x research points for [mob display name]"
			int amount = Integer.parseInt(args[1]);
			String display = main.getItemMeta().getLore().get(1);

			if (playerStats.containsKey(p.getUniqueId())) {
				String mob = displayNameMap.get(display);
				HashMap<String, Integer> researchPoints = playerStats.get(p.getUniqueId()).getResearchPoints();
				int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + amount : amount;
				researchPoints.put(mob, points);
				p.getInventory().removeItem(main);
				p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1, 1);
				p.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + amount + " §7research points for " + display + "§7!");
				checkItemCompletion(mob, p, points);
			}
			else {
				p.sendMessage("§4[§c§lMLMC§4] §cError, player stats not found.");
			}
		}
	}

	public void checkItemCompletion(String mob, Player p, int totalPoints) {
		// Check for research goals that need it
		PlayerStats stats = playerStats.get(p.getUniqueId());
		TreeSet<String> completedItems = stats.getCompletedResearchItems();
		HashMap<String, Integer> researchPoints = stats.getResearchPoints();
		if (mobMap.containsKey(mob)) {
			for (ResearchItem researchItem : mobMap.get(mob)) { // For each relevant research item
				if (!completedItems.contains(researchItem.getName())) { // If the player hasn't completed it
					// Check if research goal is completed for specific mob
					HashMap<String, Integer> goals = researchItem.getGoals();
					if (goals.get(mob) <= totalPoints) {
						for (String rMob : goals.keySet()) { // Check every objective
							if (researchPoints.get(rMob) < goals.get(rMob)) {
								return; // Haven't completed every item
							}
						}
	
						// Completed a research item
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								broadcast.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem.getName()));
						completedItems.add(researchItem.getName());
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1, 1);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								permcmd.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem.getName().replaceAll(" ", "")
										.replaceAll(":", "").toLowerCase()));
						stats.addExp(p, researchItem.getExp());
					}
				}
			}
		}
	}
	
	public void giveResearchPoints(Player p, int amount, String mob) {
		UUID uuid = p.getUniqueId();
		if (!isInstance) {
			if (playerStats.containsKey(uuid)) {
				HashMap<String, Integer> researchPoints = playerStats.get(uuid).getResearchPoints();
				int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + amount : 1;
				researchPoints.put(mob, points);
				checkItemCompletion(mob, p, points);
			}
		} else {
			BukkitRunnable increment = new BukkitRunnable() {
				public void run() {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(url, user, pass);
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM research_points WHERE uuid = '" + uuid + "' AND mob = '" + mob + "';");
						
						if (rs.next()) {
							stmt.executeUpdate("UPDATE research_points SET points = points + " + amount + " WHERE mob = '" + mob + "';");
						}
						else {
							stmt.executeUpdate("INSERT INTO research_points VALUES ('" + uuid + "', '" + mob + "', " + amount + ");");
						}
						
						stmt.executeUpdate("INSERT INTO research_updates VALUES ('" + uuid + "');");
						con.close();
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			};
			increment.runTaskAsynchronously(this);
		}
	}

	public void giveResearchKills(Player p, int amount, String mob) {
		UUID uuid = p.getUniqueId();
		if (!isInstance) {
			if (playerStats.containsKey(uuid)) {
				HashMap<String, Integer> mobKills = playerStats.get(uuid).getMobKills();
				int kills = mobKills.containsKey(mob) ? mobKills.get(mob) + amount : 1;
				mobKills.put(mob, kills);
				checkItemCompletion(mob, p, kills);
			}
		} else {
			BukkitRunnable increment = new BukkitRunnable() {
				public void run() {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(url, user, pass);
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM research_kills WHERE uuid = '" + uuid + "' AND mob = '" + mob + "';");
						
						if (rs.next()) {
							stmt.executeUpdate("UPDATE research_kills SET kills = kills + " + amount + " WHERE mob = '" + mob + "';");
						}
						else {
							stmt.executeUpdate("INSERT INTO research_kills VALUES ('" + uuid + "', '" + mob + "', " + amount + ");");
						}
						
						stmt.executeUpdate("INSERT INTO research_updates VALUES ('" + uuid + "');");
						con.close();
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			};
			increment.runTaskAsynchronously(this);
		}
	}
	
	public PlayerStats getPlayerStats(Player p) {
		return playerStats.get(p.getUniqueId());
	}
}