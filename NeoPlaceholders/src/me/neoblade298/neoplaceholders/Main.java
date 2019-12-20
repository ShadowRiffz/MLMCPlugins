package me.neoblade298.neoplaceholders;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPlaceholders Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		DecimalFormat df = new DecimalFormat("0");
		DecimalFormat df1k = new DecimalFormat("0.00k");
		DecimalFormat df10k = new DecimalFormat("00.0k");
		DecimalFormat pct = new DecimalFormat("00.00");

		// Current health placeholder
		PlaceholderAPI.registerPlaceholder(this, "CurrentHealth", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "Loading...";

				if (online && p != null) {
					double health = p.getHealth();
					if (health >= 10000) {
						placeholder = df10k.format(health / 10000);
					}
					else if (health >= 1000) {
						placeholder = df1k.format(health / 1000);
					}
					else {
						placeholder = df.format(health);
					}
					return placeholder;
				}
				return placeholder;
			}
		});

		// Max health placeholder
		PlaceholderAPI.registerPlaceholder(this, "MaxHealth", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "Loading...";

				if (online && p != null) {
					@SuppressWarnings("deprecation")
					double health = p.getMaxHealth();
					if (health >= 10000) {
						placeholder = df10k.format(health / 10000);
					}
					else if (health >= 1000) {
						placeholder = df1k.format(health / 1000);
					}
					else {
						placeholder = df.format(health);
					}
					return placeholder;
				}
				return placeholder;
			}
		});

		// Current mana placeholder
		PlaceholderAPI.registerPlaceholder(this, "CurrentMana", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "0";

				if (online && p != null) {
					PlayerData pData = SkillAPI.getPlayerData(p);
					if (pData != null) {
						placeholder = df.format(pData.getMana());
						return placeholder;
					}
				}
				return placeholder;
			}
		});

		// Max mana placeholder
		PlaceholderAPI.registerPlaceholder(this, "MaxMana", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "0";
				
				if (online && p != null) {
					PlayerData pData = SkillAPI.getPlayerData(p);
					if (pData != null) {
						placeholder = df.format(pData.getMaxMana());
						return placeholder;
					}
				}
				return placeholder;
			}
		});

		// Next level % placeholder
		PlaceholderAPI.registerPlaceholder(this, "NextLevel", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "N/A";

				if (online && p != null) {
					PlayerData pData = SkillAPI.getPlayerData(p);
					if (pData != null) {
						PlayerClass pClass = pData.getClass("class");
						if (pClass != null) {
							int max = pClass.getData().getMaxLevel();
							int lvl = pClass.getLevel();
							if (max != lvl) {
								double reqExp = pClass.getRequiredExp();
								double currExp = pClass.getExp();
								placeholder = pct.format((currExp / reqExp) * 100) + "%";
								return placeholder;
							}
						}
					}
				}
				return placeholder;
			}
		});
		
		// Current level placeholder
		PlaceholderAPI.registerPlaceholder(this, "CurrentLevel", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "N/A";

				if (online && p != null) {
					PlayerData pData = SkillAPI.getPlayerData(p);
					if (pData != null) {
						PlayerClass pClass = pData.getClass("class");
						if (pClass != null) {
							placeholder = pClass.getLevel() + "";
							return placeholder;
						}
					}
				}
				return placeholder;
			}
		});
		
		// Resource placeholder
		PlaceholderAPI.registerPlaceholder(this, "Resource", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				boolean online = e.isOnline();
				Player p = e.getPlayer();
				String placeholder = "MP";
				
				if (online && p != null) {
					PlayerData pData = SkillAPI.getPlayerData(p);
					if (pData != null) {
						PlayerClass pClass = pData.getClass("class");
						if (pClass != null) {
							placeholder = pClass.getData().getManaName();
							return placeholder;
						}
					}
				}
				return placeholder;
			}
		});
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoPlaceholders Disabled");
		super.onDisable();
	}

}
