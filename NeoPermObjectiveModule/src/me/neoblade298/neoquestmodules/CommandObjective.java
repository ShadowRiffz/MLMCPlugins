package me.neoblade298.neoquestmodules;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public CommandObjective() {
        this.setName("Command Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Command", "Enter command", "/profs");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent e){
    	Player p = e.getPlayer();

    	for (Quest quest : qp.getQuester(p.getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
    	    	String compare = ((String) map.get("Command")).toLowerCase();
    	    	if (e.getMessage().toLowerCase().startsWith(compare)) {
    				incrementObjective(p, this, 1, quest);
    	    	}
    	    }
    	}
    }
}