package me.neoblade298.neocsgoopener;

import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.CratesPlus;

public class Main extends JavaPlugin {
	public void onEnable() {
		CratesPlus.getOpenHandler().registerOpener(new CSGOOpener(this, "CSGO"));
	}
}
