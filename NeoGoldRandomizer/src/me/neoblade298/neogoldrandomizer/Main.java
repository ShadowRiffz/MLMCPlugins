package me.neoblade298.neogoldrandomizer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	protected me.neoblade298.neobossinstances.Main nbi;

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoGoldRandomizer Enabled");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		nbi = (me.neoblade298.neobossinstances.Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");

		getCommand("neogoldrandomizer").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoGoldRandomizer Disabled");
		super.onDisable();
	}
}
