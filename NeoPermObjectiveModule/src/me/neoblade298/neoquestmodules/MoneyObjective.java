package me.neoblade298.neoquestmodules;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import net.ess3.api.events.UserBalanceUpdateEvent;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MoneyObjective extends CustomObjective implements Listener {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
    // Construct the objective
    public MoneyObjective() {
        this.setName("Money Objective");
        this.setAuthor("Neoblade298");
        this.addStringPrompt("Amount", "Enter the amount of money to reach", "5000");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void onBalanceUpdate (UserBalanceUpdateEvent e){
    	Player p = e.getPlayer();
    	double bal = e.getNewBalance().doubleValue();

    	for (Quest quest : qp.getQuester(e.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
    	    Map<String, Object> map = getDataForPlayer(p, this, quest);
    	    if (map != null) {
    	    	if (bal >= Integer.parseInt((String) map.get("Amount"))) {
    				incrementObjective(e.getPlayer(), this, 1, quest);
    	    	}
    	    }
    	}
    }
}