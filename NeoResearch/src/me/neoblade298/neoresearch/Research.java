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

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;
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

	public String broadcast, permcmd, levelup, discovery;

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
		discovery = general.getString("discovery");
		if (new File(getDataFolder(), "instance").exists()) {
			isInstance = true;
		}

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
				// perm
				researchItem.setPermission(rItemSec.getString("perm"));
				
				// attributes
				Attributes attributes = new Attributes();
				ConfigurationSection attrSec = rItemSec.getConfigurationSection("attrs");
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
				System.out.println("Failed to load research item: " + rItem);
			}
		}
		
		// Finally, load in all online players
		for (Player p : Bukkit.getOnlinePlayers()) {
			loadPlayer(p);
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
						
						// Use completed research items to apply attributes
						updateBonuses(p);
					}
					con.close();
				} catch (Exception ex) {
					System.out.println(ex);
				} finally {
					playerStats.put(uuid, new PlayerStats(main, level, exp, completedResearchItems, researchPoints, mobKills));
				}
			}
		};
		load.runTaskLaterAsynchronously(this, 100L);
	}

	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();
		playerAttrs.remove(uuid);
		PlayerStats stats = playerStats.get(uuid);

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
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack main = p.getInventory().getItemInMainHand().clone();
		main.setAmount(1);

		if (main.getType().equals(Material.BOOK) && main.hasItemMeta() && main.getItemMeta().hasCustomModelData() && 
				main.getItemMeta().getCustomModelData() == 100 && main.getItemMeta().hasLore()) {
			String[] args = main.getItemMeta().getLore().get(0).split(" ");

			// "Grants x research points for [mob display name]"
			int amount = Integer.parseInt(args[1]);
			String display = main.getItemMeta().getLore().get(1);

			if (playerStats.containsKey(p.getUniqueId())) {
				String mob = new NBTItem(main).getString("internalmob");
				HashMap<String, Integer> researchPoints = playerStats.get(p.getUniqueId()).getResearchPoints();
				HashMap<String, Integer> mobKills = playerStats.get(p.getUniqueId()).getMobKills();
				int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + amount : amount;
				
				// Reveal mob in kills if not there
				if (!mobKills.containsKey(mob)) {
					mobKills.put(mob, 0);
				}
				
				researchPoints.put(mob, points);
				p.getInventory().removeItem(main);
				p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1, 1);
				p.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + amount + " §7research points for " + display + "§7!");
				checkItemCompletion(mob, p, points, display);
			}
			else {
				p.sendMessage("§4[§c§lMLMC§4] §cError, player stats not found.");
			}
		}
	}

	public void checkItemCompletion(String mob, Player p, int totalPoints, String display) {
		// Check if new discovery
		PlayerStats stats = playerStats.get(p.getUniqueId());
		HashMap<String, Integer> researchPoints = stats.getResearchPoints();
		if (!researchPoints.containsKey(mob)) {
			p.sendMessage(discovery.replaceAll("%mob%", display).replaceAll("&", "§"));
		}
		
		// Check for research goals that need it
		TreeSet<String> completedItems = stats.getCompletedResearchItems();
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
								permcmd.replaceAll("%player%", p.getName()).replaceAll("%perm%", researchItem.getPermission()));
						stats.addExp(p, researchItem.getExp());
						updateBonuses(p);
					}
				}
			}
		}
	}

	// Strictly for taking away research points/kills
	public void checkItemDecompletion(String mob, Player p, int totalPoints) {
		// Check for research goals that need it
		PlayerStats stats = playerStats.get(p.getUniqueId());
		TreeSet<String> completedItems = stats.getCompletedResearchItems();
		if (mobMap.containsKey(mob)) {
			for (ResearchItem researchItem : mobMap.get(mob)) { // For each relevant research item
				if (completedItems.contains(researchItem.getName())) { // If the player has completed it
					// Check if research goal is completed for specific mob
					HashMap<String, Integer> goals = researchItem.getGoals();
					if (goals.get(mob) > totalPoints) {
						stats.takeExp(p, researchItem.getExp());
						completedItems.remove(researchItem.getName());
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								permcmd.replaceAll("%player%", p.getName()).replaceAll("%perm%", researchItem.getPermission()).replaceAll("set", "unset"));
					}
				}
			}
		}
		updateBonuses(p);
	}
	
	public void giveResearchPoints(Player p, int amount, String mob, boolean announce) {
		UUID uuid = p.getUniqueId();
		if (playerStats.containsKey(uuid)) {
			HashMap<String, Integer> researchPoints = playerStats.get(uuid).getResearchPoints();
			int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + amount : amount;
			researchPoints.put(mob, points);
			String display = MythicMobs.inst().getMobManager().getMythicMob(mob).getDisplayName().get();
			if (announce) {
				String msg = new String("&4[&c&lMLMC&4] &7You gained &e" + amount + " &7extra research points for " + display + "&7!");
				msg = msg.replaceAll("&", "§");
				p.sendMessage(msg);
			}
			checkItemCompletion(mob, p, points, display);
		}
	}
	
	public void giveResearchPointsAlias(Player p, int amount, String mob, String display, boolean announce) {
		UUID uuid = p.getUniqueId();
		if (playerStats.containsKey(uuid)) {
			HashMap<String, Integer> researchPoints = playerStats.get(uuid).getResearchPoints();
			int points = researchPoints.containsKey(mob) ? researchPoints.get(mob) + amount : amount;
			researchPoints.put(mob, points);
			if (announce) {
				String msg = new String("&4[&c&lMLMC&4] &7You gained &e" + amount + " &7extra research points for " + display + "&7!");
				msg = msg.replaceAll("&", "§");
				p.sendMessage(msg);
			}
			checkItemCompletion(mob, p, points, display);
		}
	}

	public void giveResearchKills(Player p, int amount, String mob) {
		UUID uuid = p.getUniqueId();
		if (playerStats.containsKey(uuid)) {
			HashMap<String, Integer> mobKills = playerStats.get(uuid).getMobKills();
			int kills = mobKills.containsKey(mob) ? mobKills.get(mob) + amount : amount;
			mobKills.put(mob, kills);
		}
	}
	
	public void setResearchPoints(Player p, int amount, String mob) {
		UUID uuid = p.getUniqueId();
		if (playerStats.containsKey(uuid)) {
			HashMap<String, Integer> researchPoints = playerStats.get(uuid).getResearchPoints();
			researchPoints.put(mob, amount);
			checkItemDecompletion(mob, p, amount);
		}
	}

	public void setResearchKills(Player p, int amount, String mob) {
		UUID uuid = p.getUniqueId();
		if (playerStats.containsKey(uuid)) {
			HashMap<String, Integer> mobKills = playerStats.get(uuid).getMobKills();
			mobKills.put(mob, amount);
		}
	}

	public void updateBonuses(Player p) {
		// Make sure the player has no bonuses already equipped
		removeBonuses(p);

		// Go through all completed collections and add attributes
		UUID uuid = p.getUniqueId();
		Attributes pAttrs = new Attributes();
		for (String rName : playerStats.get(uuid).getCompletedResearchItems()) {
			pAttrs.addAttribute(researchItems.get(rName).getAttrs());
		}
		
		playerAttrs.put(uuid, pAttrs);
		pAttrs.applyAttributes(p);
	}
	
	public void removeBonuses(Player p) {
		UUID uuid = p.getUniqueId();
		if (playerAttrs.containsKey(uuid)) {
			Attributes pAttrs = playerAttrs.get(uuid);
			pAttrs.removeAttributes(p);
			pAttrs.resetAttributes();
		}
	}
	
	public PlayerStats getPlayerStats(Player p) {
		return playerStats.get(p.getUniqueId());
	}
	
	public int getNumResearchItems() {
		return researchItems.size();
	}
	
	public HashMap<Integer, Integer> getNextLvl() {
		return toNextLvl;
	}
}