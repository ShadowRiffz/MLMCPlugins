package me.neoblade298.neoquestmodules;

import me.Neoblade298.NeoProfessions.Events.OpenProfessionInvEvent;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpenProfessionInvObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public OpenProfessionInvObjective() {
        this.setName("Open Profession Inv Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Type", "Enter the profession inv type", "StorageView");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onOpenProfessionInv (OpenProfessionInvEvent e){
    	Player p = e.getPlayer();

    	for (Quest quest : qp.getQuester(p.getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
    	    	if (((String) map.get("Type")).equalsIgnoreCase(e.getInventory().getClass().getSimpleName())) {
    				incrementObjective(p, this, 1, quest);
    	    	}
    	    }
    	}
    }
}