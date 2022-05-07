package me.neoblade298.neoquestmodules;

import me.Neoblade298.NeoProfessions.Events.ProfessionCraftSuccessEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionPlantSeedEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CraftRecipeObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public CraftRecipeObjective() {
        this.setName("Craft Recipe Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Recipe", "Enter the recipe name or *", "*");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onCraftRecipe (ProfessionCraftSuccessEvent e){
    	Player p = e.getPlayer();

    	for (Quest quest : qp.getQuester(p.getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
    	    	String recipe = (String) map.get("Recipe");
    	    	if (recipe.equals(e.getRecipe().getKey()) || recipe.equals("*")) {
    				incrementObjective(p, this, 1, quest);
    	    	}
    	    }
    	}
    }
}