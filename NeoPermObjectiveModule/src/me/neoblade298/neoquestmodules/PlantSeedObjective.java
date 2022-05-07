package me.neoblade298.neoquestmodules;

import me.Neoblade298.NeoProfessions.Events.ProfessionPlantSeedEvent;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlantSeedObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public PlantSeedObjective() {
        this.setName("Plant Seed Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Seed ID", "Enter the seed id or -1", "-1");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onPlantSeed (ProfessionPlantSeedEvent e){
    	Player p = e.getPlayer();

    	for (Quest quest : qp.getQuester(p.getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
    	    	int id = Integer.parseInt((String) map.get("Seed ID"));
    	    	if (id == e.getSeed().getId() || id == -1) {
    				incrementObjective(p, this, 1, quest);
    	    	}
    	    }
    	}
    }
}