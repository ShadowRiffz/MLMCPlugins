package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Gardens.Fertilizer;
import me.Neoblade298.NeoProfessions.Gardens.Garden;
import me.Neoblade298.NeoProfessions.Gardens.GardenSlot;
import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class GardenManager implements IOComponent {
	public static Professions main;
	private static HashMap<UUID, HashMap<ProfessionType, Garden>> gardens = new HashMap<UUID, HashMap<ProfessionType, Garden>>();
	private static HashMap<Integer, Fertilizer> fertilizers = new HashMap<Integer, Fertilizer>();
	public GardenManager(Professions main) {
		GardenManager.main = main;

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
					double timeMult = cfg.getDouble("time-multiplier", 1);
					double amountMult = cfg.getDouble("amount-multiplier", 1);
					Rarity minRarity = Rarity.valueOf(cfg.getString("min-rarity", "COMMON").toUpperCase());
					double rarityWeightMult = cfg.getDouble("rarity-weight-multiplier", 1);
					MinigameParameters params = new MinigameParameters(id, amountMult, rarityWeightMult, minRarity);
					fertilizers.put(id, new Fertilizer(id, params, timeMult));
				}
			}
		}
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (gardens.containsKey(p.getUniqueId())) {
			return;
		}

		HashMap<ProfessionType, Garden> pgardens = new HashMap<ProfessionType, Garden>();
		gardens.put(p.getUniqueId(), pgardens);
		for (ProfessionType type : ProfessionType.values()) {
			if (type.equals(ProfessionType.CRAFTER)) {
				continue;
			}
			pgardens.put(type, new Garden());
		}
		
		try {
			// Set up gardens
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_gardens WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				pgardens.get(ProfessionType.valueOf(rs.getString(2))).setSize(rs.getInt(3));
			}

			// Set up garden slots
			rs = stmt.executeQuery("SELECT * FROM professions_gardenslots WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				if (rs.getInt(4) == 1) {
					continue;
				}
				GardenSlot gslot = new GardenSlot(rs.getInt(4), GardenManager.getFertilizer(rs.getInt(6)), rs.getLong(5));
				pgardens.get(ProfessionType.valueOf(rs.getString(2))).getSlots().put(rs.getInt(3), gslot);
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init gardens for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		HashMap<ProfessionType, Garden> pgardens = gardens.get(uuid);
		try {
			for (ProfessionType type : pgardens.keySet()) {
				Garden garden = pgardens.get(type);
				// Save garden size
				stmt.addBatch("REPLACE INTO professions_gardens "
						+ "VALUES ('" + uuid + "', '" + type + "', " + pgardens.get(type).getSize() + ");");
				
				// Save garden slots
				for (int slot = 0; slot < garden.getSize(); slot++) {
					GardenSlot gslot = garden.getSlots().get(slot);
					if (gslot != null) {
						int fid = gslot.getFertilizer() != null ? gslot.getFertilizer().getId() : -1;
						stmt.addBatch("REPLACE INTO professions_gardenslots "
								+ "VALUES ('" + uuid + "', '" + type + "'," + slot + "," + gslot.getId() + "," +
								gslot.getEndTime() + "," + fid +");");
					}
					else {
						stmt.addBatch("REPLACE INTO professions_gardenslots "
								+ "VALUES ('" + uuid + "', '" + type + "'," + slot + ",-1,-1,-1);");
					}
				}
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save gardens for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static HashMap<ProfessionType, Garden> getGardens(Player p) {
		return gardens.get(p.getUniqueId());
	}
	
	public static Fertilizer getFertilizer(int id) {
		return fertilizers.get(id);
	}
	
	@Override
	public String getComponentName() {
		return "GardenManager";
	}
}
