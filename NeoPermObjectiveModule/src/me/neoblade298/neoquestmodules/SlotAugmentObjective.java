package me.neoblade298.neoquestmodules;

import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SlotAugmentObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public SlotAugmentObjective() {
        this.setName("Slot Augment Objective");
        this.setAuthor("Neoblade298");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onSlotAugment (ProfessionSlotSuccessEvent e){
    	Player p = e.getPlayer();

    	for (Quest quest : qp.getQuester(p.getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
				incrementObjective(p, this, 1, quest);
    	    }
    	}
    }
}