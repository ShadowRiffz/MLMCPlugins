package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPlaceholders Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		new SkillAPIPlaceholders(this).registerPlaceholders();
		new EtcPlaceholders(this).registerPlaceholders();
		new NeoprofessionsPlaceholders().register();
		new NeoBossInstancesPlaceholders().register();
		new ResearchKillsPlaceholders().register();
		new ResearchPointsPlaceholders().register();
		new ResearchKillsAliasPlaceholders().register();
		new ResearchPointsAliasPlaceholders().register();
		new MinibossPlaceholders().register();
		new MinibossShortPlaceholders().register();
		new ChatColorPlaceholders().register();
		new OtherSkillAPIPlaceholders().register();
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoPlaceholders Disabled");
		super.onDisable();
	}
}
