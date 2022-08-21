package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neoplaceholders.placeholders.*;

public class NeoPlaceholders extends JavaPlugin implements org.bukkit.event.Listener {

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPlaceholders Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		new MythicmobsPlaceholders().register();
		new SkillAPIPlaceholders().register();
		new BossMultipliersPlaceholders().register();
		new NeoCoreFieldPlaceholders().register();
		new NeoCoreTagPlaceholders().register();
		new VaultPlaceholders(this).register();
		new EtcPlaceholders(this).registerPlaceholders();
		new NeoProfessionsPlaceholders().register();
		new NeoBossInstancesPlaceholders().register();
		new ResearchPlaceholders().register();
		new ResearchNamePlaceholder().register();
		new ResearchNameBossPlaceholder().register();
		new ResearchKillsPlaceholders().register();
		new ResearchPointsPlaceholders().register();
		new ResearchKillsBossPlaceholders().register();
		new ResearchPointsBossPlaceholders().register();
		new MinibossPlaceholders().register();
		new MinibossShortPlaceholders().register();
		new ChatColorPlaceholders().register();
		new SkillAPIPlaceholders().register();
		new LordboardPlaceholders().register();
		new QuestTagPlaceholders().register();
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoPlaceholders Disabled");
		super.onDisable();
	}
}
