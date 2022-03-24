package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Gardens.Fertilizer;
import me.Neoblade298.NeoProfessions.Gardens.Garden;
import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class GardenManager implements IOComponent {
	Professions main;
	private static HashMap<UUID, HashMap<ProfessionType, Garden>> gardens = new HashMap<UUID, HashMap<ProfessionType, Garden>>();
	private static HashMap<Integer, Fertilizer> fertilizers = new HashMap<Integer, Fertilizer>();
	public GardenManager(Professions main) {
		this.main = main;

		loadFertilizers(new File(main.getDataFolder(), "fertilizers"));
	}
	
	private void loadFertilizers(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadFertilizers(file);
			}
			else {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
				for (String key : yaml.getKeys(false)) {
					ConfigurationSection cfg = yaml.getConfigurationSection(key);
					int id = Integer.parseInt(key);
					double timeMult = cfg.getDouble("time-multiplier");
					double amountMult = cfg.getDouble("amount-multiplier");
					Rarity minRarity = Rarity.valueOf(cfg.getString("min-rarity").toUpperCase());
					double rarityWeightMult = cfg.getDouble("rarity-weight-multiplier");
					MinigameParameters params = new MinigameParameters(id, amountMult, rarityWeightMult, minRarity);
					fertilizers.put(id, new Fertilizer(id, params, timeMult));
				}
			}
		}
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		/*
		if (knowledge.containsKey(p.getUniqueId())) {
			return;
		}

		HashSet<String> keys = new HashSet<String>();
		knowledge.put(p.getUniqueId(), keys);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_knowledge WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				keys.add(rs.getString(2));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init storage for user " + p.getName());
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		/*
		UUID uuid = p.getUniqueId();
		try {
			for (String key : knowledge.get(uuid)) {
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + key + "');");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save storage for user " + p.getName());
			e.printStackTrace();
		}
		*/
	}
	
	public static HashMap<ProfessionType, Garden> getGardens(Player p) {
		return gardens.get(p.getUniqueId());
	}
	
	public static Fertilizer getFertilizer(int id) {
		return fertilizers.get(id);
	}
}
