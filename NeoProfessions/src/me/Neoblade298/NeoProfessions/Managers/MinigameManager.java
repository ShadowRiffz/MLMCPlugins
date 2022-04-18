package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Minigames.Minigame;
import me.Neoblade298.NeoProfessions.Minigames.MinigameDrops;
import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class MinigameManager {
	public static Professions main;
	private static HashMap<Integer, Minigame> games = new HashMap<Integer, Minigame>();
	private static HashMap<UUID, HashMap<UUID, Long>> playerCooldowns = new HashMap<UUID, HashMap<UUID, Long>>();
	
	public MinigameManager(Professions main) {
		MinigameManager.main = main;
		
		loadMinigames(new File(main.getDataFolder(), "minigames"));
	}
	
	private void loadMinigames(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadMinigames(file);
			}
			else {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
				for (String key : yaml.getKeys(false)) {
					int id = Integer.parseInt(key);
					ConfigurationSection itemCfg = yaml.getConfigurationSection(key);
					String display = itemCfg.getString("display");
					ProfessionType type = ProfessionType.valueOf(itemCfg.getString("type").toUpperCase());
					int numDrops = itemCfg.getInt("num-drops");
					int difficulty = itemCfg.getInt("difficulty");
					int level = StorageManager.getItem(id).getLevel();
					
					// Parse drops
					ArrayList<String> drops = (ArrayList<String>) itemCfg.getStringList("drops");
					ArrayList<MinigameDrops> parsed = new ArrayList<MinigameDrops>();
					for (String line : drops) {
						String[] lineArgs = line.split(" ");
						// defaults
						int itemId = 0;
						int minAmt = 1;
						int maxAmt = 1;
						int weight = -1;
						int exp = -1;
						
						for (String lineArg : lineArgs) {
							String[] args = lineArg.split(":");
							switch (args[0]) {
							case "id":
								itemId = Integer.parseInt(args[1]);
								break;
							case "amount":
								String[] amts = args[1].split("-");
								minAmt = Integer.parseInt(amts[0]);
								maxAmt = amts.length == 2 ? Integer.parseInt(amts[1]) : minAmt;
								break;
							case "weight":
								weight = Integer.parseInt(args[1]);
								break;
							case "exp":
								exp = Integer.parseInt(args[1]);
								break;
							}
						}
						
						// Post-processing (auto-generate exp)
						StoredItem sitem = StorageManager.getItem(itemId);
						sitem.addSource(display, false);
						if (exp == -1) {
							exp = sitem.getDefaultExp();
						}
						if (weight == -1) {
							weight = Professions.getDefaultWeight(sitem.getRarity());
						}
						
						parsed.add(new MinigameDrops(sitem, minAmt, maxAmt, weight, exp));
					}
					games.put(id, new Minigame(display, type, parsed, numDrops, difficulty, level));
				}
			}
		}
	}
	
	public static void startMinigame(Player p, int key) {
		games.get(key).startMinigame(p, null);
	}
	
	public static void startMinigame(Player p, int id, MinigameParameters params) {
		games.get(id).startMinigame(p, params);
	}
	
	// Cooldown in seconds
	public static void startMinigame(Player p, int key, UUID mob, int cooldown) {
		if (!playerCooldowns.containsKey(p.getUniqueId())) {
			playerCooldowns.put(p.getUniqueId(), new HashMap<UUID, Long>());
		}
		
		HashMap<UUID, Long> cooldowns = playerCooldowns.get(p.getUniqueId());
		long currCd = cooldowns.getOrDefault(mob, 0L);
		if (currCd > System.currentTimeMillis()) {
			int time = (int) ((currCd - System.currentTimeMillis()) / 1000); // Remaining time in seconds
			int minutes = time / 60;
			int seconds = time % 60;
			p.sendMessage("§cYou cannot harvest this node for another " + String.format("§c%d:%02d", minutes, seconds));
			return;
		}
		
		if (games.get(key).startMinigame(p, null)) {
			cooldowns.put(mob, System.currentTimeMillis() + (cooldown * 1000));
		}
	}
}
