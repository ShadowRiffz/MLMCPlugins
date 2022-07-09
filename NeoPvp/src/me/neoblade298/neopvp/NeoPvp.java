package me.neoblade298.neopvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neopvp.generators.GeneratorManager;

public class NeoPvp extends JavaPlugin {
	private static NeoPvp inst;
	private static ArrayList<Manager> mngrs = new ArrayList<Manager>();
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPvp Enabled");
		mngrs.add(new GeneratorManager());
		
		NeoCore.registerIOComponent(this, new PvpManager());
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPvp Disabled");
	    super.onDisable();
	}
	
	public static NeoPvp inst() {
		return inst;
	}
}
