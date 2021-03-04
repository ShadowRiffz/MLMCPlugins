package me.neoblade298.neoresearch;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;

public class Research extends JavaPlugin implements org.bukkit.event.Listener {
	// SQL
	public String url, user, pass;
	public HashMap<String, HashMap<String, Integer>> researchItems;
	public HashMap<String, ArrayList<String>> mobMap;
	public HashMap<String, String> displayNameMap;
	public HashMap<String, Integer> researchBookMin;
	public HashMap<UUID, PlayerStats> playerStats;
	public HashMap<Integer, Integer> toNextLvl;

	private String broadcast;
	private String permcmd;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoResearch Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("nr").setExecutor(new Commands(this));

		load_config();
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoResearch Disabled");
		super.onDisable();
	}

	public void load_config() {
		File file = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		ConfigurationSection cfg = YamlConfiguration.loadConfiguration(file);

		// SQL
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		url = "jbdc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		user = sql.getString("username");
		pass = sql.getString("password");

		// General
		ConfigurationSection general = cfg.getConfigurationSection("general");
		broadcast = general.getString("research_complete_broadcast").replaceAll("&", "§");
		permcmd = general.getString("permission_command");

		// Exp
		toNextLvl = new HashMap<Integer, Integer>();
		ConfigurationSection exp = cfg.getConfigurationSection("exp");
		for (String lvl : exp.getKeys(false)) {
			toNextLvl.put(Integer.parseInt(lvl), exp.getInt(lvl));
		}

		// Load research items into mobMap, researchItems,
		researchItems = new HashMap<String, HashMap<String, Integer>>();
		mobMap = new HashMap<String, ArrayList<String>>();
		ConfigurationSection rItems = cfg.getConfigurationSection("research_items");

		for (String rItem : rItems.getKeys(false)) {
			HashMap<String, Integer> obj = new HashMap<String, Integer>();
			ConfigurationSection sec = rItems.getConfigurationSection(rItem);
			for (String mob : rItems.getKeys(false)) {
				obj.put(mob, sec.getInt(mob));

				// Add to mob map and research book min
				if (mobMap.containsKey(mob)) {
					mobMap.get(mob).add(rItem);
					if (researchBookMin.get(mob) < sec.getInt(mob))
						researchBookMin.put(mob, sec.getInt(mob));
				}
				else {
					ArrayList<String> list = new ArrayList<String>();
					list.add(rItem);
					mobMap.put(mob, list);
					researchBookMin.put(mob, sec.getInt(mob));
				}
			}
			researchItems.put(rItem, obj);
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
			mobKills.put(mob, mobKills.get(mob) + 1);

			// Update research points
			HashMap<String, Integer> researchPoints = stats.getResearchPoints();
			int points = researchPoints.get(mob) + 1;
			researchPoints.put(mob, points);

			checkItemCompletion(mob, p, points);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		// First check if the account was already loaded
		if (!playerStats.containsKey(uuid)) {
			
			// Asynchronously look up sql and load it in
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
						ResultSet rs = stmt.executeQuery("SELECT * FROM research_accounts WHERE uuid = '" + uuid + "';");
						
						// Load in account info
						if (rs.next()) {
							level = rs.getInt(2);
							exp = rs.getInt(3);

							rs = stmt.executeQuery("SELECT * FROM research_statistics WHERE uuid = '" + uuid + "';");
							while (rs.next()) {
								researchPoints.put(rs.getString(2), rs.getInt(3));
							}

							rs = stmt.executeQuery("SELECT * FROM research_points WHERE uuid = '" + uuid + "';");
							while (rs.next()) {
								mobKills.put(rs.getString(2), rs.getInt(3));
							}
							
							rs = stmt.executeQuery("SELECT * FROM research_items WHERE uuid = '" + uuid + "';");
							while (rs.next()) {
								completedResearchItems.add(rs.getString(2));
							}

							playerStats.put(uuid, new PlayerStats(level, exp, completedResearchItems, researchPoints, mobKills));
						}
						con.close();
					} catch (Exception ex) {
						System.out.println(ex);
					} finally {
						playerStats.put(uuid, new PlayerStats(level, exp, completedResearchItems, researchPoints, mobKills));
					}
				}
			};
			load.runTaskAsynchronously(this);
		}
	}

	private void handleLeave(UUID uuid) {
		PlayerStats stats = playerStats.get(uuid);

		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				if (playerStats.containsKey(uuid)) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(url, user, pass);
						Statement stmt = con.createStatement();

						// Save account
						stmt.executeUpdate("INSERT INTO research_accounts VALUES ('" + uuid + "','" + stats.getLevel()
						+ "','" + stats.getExp() + "');");

						// Save research points
						String exec = "";
						HashMap<String, Integer> researchPoints = stats.getResearchPoints();
						for (String mob : researchPoints.keySet()) {
							exec += "REPLACE INTO research_points values('" + uuid + "'," + mob + "," + researchPoints.get(mob) + ");";
						}

						HashMap<String, Integer> mobKills = stats.getMobKills();
						for (String mob : mobKills.keySet()) {
							exec += "REPLACE INTO research_statistics values('" + uuid + "'," + mob + "," + mobKills.get(mob) + ");";
						}

						for (String item : stats.getCompletedResearchItems()) {
							exec += "REPLACE INTO research_statistics values('" + uuid + "'," + item + ");";
						}
						stmt.executeUpdate(exec);
						con.close();
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
		};
		save.runTaskAsynchronously(this);
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
			String display = args[5];
			for (int i = 6; i < args.length; i++) {
				display += " " + args[i];
			}

			if (playerStats.containsKey(p.getUniqueId())) {
				String mob = displayNameMap.get(display);
				HashMap<String, Integer> researchPoints = playerStats.get(p.getUniqueId()).getResearchPoints();
				int points = researchPoints.get(mob) + amount;
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

	public void checkItemCompletion(String mob, Player p, int points) {
		// Check for research goals that need it
		PlayerStats stats = playerStats.get(p.getUniqueId());
		TreeSet<String> completedItems = stats.getCompletedResearchItems();
		HashMap<String, Integer> researchPoints = stats.getResearchPoints();
		for (String researchItem : mobMap.get(mob)) { // For each relevant research item
			if (!completedItems.contains(researchItem)) { // If the player hasn't completed it
				// Check if research goal is completed for specific mob
				if (researchItems.get(researchItem).get(mob) <= points) {
					for (String rMob : researchItems.get(researchItem).keySet()) { // Check every objective
						if (researchPoints.get(rMob) <= researchItems.get(researchItem).get(rMob)) {
							return; // Haven't completed every item
						}
					}

					// Completed a research item
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							broadcast.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem));
					completedItems.add(researchItem);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1, 1);
					p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							permcmd.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem));
				}
			}
		}
	}
}