package me.neoblade298.neocore.info;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.bukkit.MythicBukkit;

public class BossInfo {
	private String key, display, displayWithLvl;
	private int level;
	private List<String> healthComponents;
	private double health = 0;
	
	public BossInfo(ConfigurationSection cfg) {
		this.key = cfg.getName();
		this.display = cfg.getString("display", "DEFAULT");
		this.level = cfg.getInt("level");
		this.healthComponents = cfg.getStringList("health-components");
		this.displayWithLvl = "§6[Lv " + level + "] " + display;
	}

	public String getKey() {
		return key;
	}

	public String getDisplay(boolean includeLevel) {
		return includeLevel ? displayWithLvl : display;
	}

	public int getLevel() {
		return level;
	}

	public double getTotalHealth() {
		if (health == 0) {
			MobManager mm = MythicBukkit.inst().getMobManager();
			for (String mob : healthComponents) {
				health += mm.getMythicMob(mob).get().getHealth().get();
			}
		}
		return health;
	}
}
