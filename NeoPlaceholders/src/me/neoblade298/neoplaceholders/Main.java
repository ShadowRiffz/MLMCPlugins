package me.neoblade298.neoplaceholders;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.bukkit.MythicBukkit;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPlaceholders Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		// Load in mob health

		File file = new File(getDataFolder(), "mobhealth.yml");
		if (!file.exists()) {
			saveResource("mobhealth.yml", false);
		}
		ConfigurationSection healthSec = YamlConfiguration.loadConfiguration(file).getConfigurationSection("Bosses");
		HashMap<String, Integer> mobhealth = new HashMap<String, Integer>();
		MobManager mm = MythicBukkit.inst().getMobManager();
		for (String key : healthSec.getKeys(false)) {
			int health = 0;
			for (String mob : healthSec.getStringList(key)) {
				try {
					health += mm.getMythicMob(mob).get().getHealth().get();
				} catch (Exception e) {
					health = 0;
				}
			}
			mobhealth.put(key, health);
		}
		
		new MythicmobsPlaceholders(mobhealth).register();
		new SkillAPIPlaceholders().register();
		new BossMultipliersPlaceholders(mobhealth).register();
		new NeoSettingsPlaceholders().register();
		new VaultPlaceholders(this).register();
		new EtcPlaceholders(this).registerPlaceholders();
		new NeoProfessionsPlaceholders().register();
		new NeoBossInstancesPlaceholders().register();
		new ResearchPlaceholders().register();
		new ResearchNamePlaceholder().register();
		new ResearchNameAliasPlaceholder().register();
		new ResearchKillsPlaceholders().register();
		new ResearchPointsPlaceholders().register();
		new ResearchKillsAliasPlaceholders().register();
		new ResearchPointsAliasPlaceholders().register();
		new MinibossPlaceholders().register();
		new MinibossShortPlaceholders().register();
		new ChatColorPlaceholders().register();
		new SkillAPIPlaceholders().register();
		new LordboardPlaceholders().register();
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoPlaceholders Disabled");
		super.onDisable();
	}
}
