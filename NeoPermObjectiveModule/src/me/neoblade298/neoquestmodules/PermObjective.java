package me.neoblade298.neoquestmodules;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import net.citizensnpcs.api.CitizensAPI;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PermObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public PermObjective() {
        this.setName("Perm Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Permission", "Enter the name of the perm that the player must have", "drop.angvoth");
        this.addStringPrompt("Id", "Enter the id of the NPC to talk to to complete the objective", "26");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onPlayerClickNPC(PlayerInteractEntityEvent e){
    	Player p = e.getPlayer();
    	Entity entity = e.getRightClicked();
    	
    	// Check if the entity is an npc
    	if (entity.hasMetadata("NPC")) {
    		int id = CitizensAPI.getNPCRegistry().getNPC(entity).getId();
	    	// Make sure to evaluate for all of the player's current quests
        	for (Quest quest : qp.getQuester(e.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
        	    Map<String, Object> map = getDataForPlayer(p, this, quest);
        	    if (map != null) {
		    		String perm = (String) map.get("Permission");
		    		int objId = Integer.parseInt((String) map.get("Id"));
		    		
		    		if (id == objId && p.hasPermission(perm)) {
		    			incrementObjective(e.getPlayer(), this, 1, quest);
		    		}
        	    }
	    	}
    	}
    }
}