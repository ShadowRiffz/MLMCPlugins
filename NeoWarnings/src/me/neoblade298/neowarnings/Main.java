package me.neoblade298.neowarnings;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.event.NewTownEvent;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	
	static HashMap<String, Boolean> understood;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoWarnings Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    // Initiate hashmap
	    understood = new HashMap<String, Boolean>();
	    
	    // Get command listener
	    this.getCommand("warning").setExecutor(new Commands());
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoWarnings Disabled");
	    super.onDisable();
	}
	
	@EventHandler
	public void onCreateTown(NewTownEvent e) {
		Player p = Bukkit.getPlayer(e.getTown().getMayor().getName());
		understood.put(p.getName(), false);
		
		BukkitRunnable warning = new BukkitRunnable() {
			public void run() {
				if (Bukkit.getPlayer(p.getName()) != null && understood.containsKey(p.getName())) {
					p.sendMessage("§4[§c§lMLMC§4] §c§lWARNING§c: Your town has no money in its bank and will fall to upkeep! You must deposit gold with " + 
							"§e/t deposit [amount]§c!");
					p.sendMessage("§4[§c§lMLMC§4] §cThe daily 24hr upkeep for a 1 person town is 50g. You can see daily upkeep with §e/t§c!");
					p.sendMessage("§4[§c§lMLMC§4] §cPlease type §e/warning understood §cto stop getting this message!");
				}
				else {
					if(understood.containsKey(p.getName())) {
						understood.remove(p.getName());
					}
					this.cancel();
				}
			}
		};
		warning.runTaskTimer(this, 200L, 200L);
		
	}
}
