package me.neoblade298.neoanalysis;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
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

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.quests.QuestInstance;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;


public class NeoAnalysis extends JavaPlugin implements org.bukkit.event.Listener {
	// SQL
	public String url, user, pass;
	public ArrayList<String> bosses;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoAnalysis Enabled");
		getServer().getPluginManager().registerEvents(this, this);

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
		
		// Bosses
		bosses = new ArrayList<String>();
		bosses.add("KilledRatface");
		bosses.add("KilledSpiderQueen");
		bosses.add("KilledAngvoth");
		bosses.add("KilledLucius");
		bosses.add("KilledHamvil");
		bosses.add("KilledEirik");
		bosses.add("KilledBanditKing");
		bosses.add("KilledAberration");
		bosses.add("KilledZachai");
		bosses.add("KilledHollister");
		bosses.add("KilledBlight");
		bosses.add("KilledDhaga");
		bosses.add("KilledFrostQueen");
		bosses.add("KilledRhain");
		bosses.add("KilledMaleficent");
		bosses.add("KilledSalondeon");
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
		Quester quester = QuestsManager.getQuester(p, 1);
		// Find latest quest
		String q = "";
		boolean iteratedAll = true;
		if (quester != null) {
			q += quester.getCompletedQuests().size() + "-";
			for (QuestInstance qi : quester.getActiveQuests()) {
				if (q.length() + qi.getQuest().getKey().length() < 77) {
					q += qi.getQuest().getKey() + ",";
				}
				else {
					iteratedAll = false;
					break;
				}
			}
			if (iteratedAll) q += "...";
		}
		final String quest = q;
		
		new BukkitRunnable() {
			public void run() {
				
				// Find latest boss
				String boss = "";
				for (String perm : bosses) {
					if (NeoQuests.getPlayerTags(1).exists(perm, uuid)) {
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
