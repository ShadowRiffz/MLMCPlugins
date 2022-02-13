package me.neoblade298.neoanalysis;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;


public class NeoAnalysis extends JavaPlugin implements org.bukkit.event.Listener {
	// SQL
	public String url, user, pass;
	public Quests quests;
	public ArrayList<String> bosses;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoAnalysis Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("analyze").setExecutor(new Commands(this));

		File file = new File(getDataFolder(), "config.yml");

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
		
		quests = (Quests) Bukkit.getPluginManager().getPlugin("Quests");
		
		// Bosses
		bosses = new ArrayList<String>();
		bosses.add("drop.ratface");
		bosses.add("drop.spiderqueen");
		bosses.add("drop.angvoth");
		bosses.add("drop.lucius");
		bosses.add("drop.hamvil");
		bosses.add("drop.eirik");
		bosses.add("drop.banditking");
		bosses.add("drop.aberration");
		bosses.add("drop.zachai");
		bosses.add("drop.hollister");
		bosses.add("drop.blight");
		bosses.add("drop.dhaga");
		bosses.add("drop.frostqueen");
		bosses.add("drop.rhain");
		bosses.add("drop.maleficent");
		bosses.add("drop.salonden");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoAnalysis Disabled");
	    super.onDisable();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		saveSql(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onKick(PlayerKickEvent e) {
		saveSql(e.getPlayer());
	}
	
	private void saveSql(Player p) {
		UUID uuid = p.getUniqueId();
		Quester quester = quests.getQuester(uuid);
		ConcurrentHashMap<Quest, Integer> map = quester.getCurrentQuests();
		// Find latest quest
		String q = "";
		for (Quest quest : map.keySet()) {
			q = quest.getName();
			break;
		}
		final String quest = q.replaceAll("'", "''");
		
		new BukkitRunnable() {
			public void run() {
				
				// Find latest boss
				String boss = "";
				for (String perm : bosses) {
					if (p.hasPermission(perm)) {
						boss = perm;
					}
					else {
						break;
					}
				}
				
				// Find class and level
				String pClass = "";
				int level = 0;
				PlayerData pd = SkillAPI.getPlayerData(p);
				if (pd != null) {
					PlayerClass pc = pd.getClass("class");
					if (pc != null) {
						pClass = pc.getData().getName();
						level = pc.getLevel();
					}
				}
				long joined = p.getFirstPlayed();
				long now = System.currentTimeMillis();
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(url, user, pass);
					Statement stmt = con.createStatement();
					stmt.executeUpdate("REPLACE INTO analysis_players values('" + uuid + "','" + p.getName() + "'," + joined + "," + now + ",'" + pClass + 
							"'," + level + ",'" + quest + "','" + boss + "');");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(this);
	}
	
}
