package me.neoblade298.neoquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerAccountChangeEvent;

import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuesterListener implements Listener {
	@EventHandler
	public void onAccountChange(PlayerAccountChangeEvent e) {
		Player p = e.getAccountData().getPlayer();
		QuestsManager.getQuester(p, e.getPreviousId()).setLocation(p.getLocation());
		Quester quester = QuestsManager.initializeOrGetQuester(p, e.getNewID());
		if (quester.getLocation() != null) {
			p.teleport(quester.getLocation());
		}
		
		QuestsManager.getQuester(p, e.getPreviousId()).stopListening();
		quester.startListening();
	}
	
	@EventHandler
	public void onWorldChange(PlayerTeleportEvent e) {
		if (SkillAPI.getSettings().isWorldEnabled(e.getFrom().getWorld()) && !SkillAPI.getSettings().isWorldEnabled(e.getTo().getWorld())) {
			Player p = e.getPlayer();
			QuestsManager.initializeOrGetQuester(p, SkillAPI.getPlayerAccountData(p).getActiveId()).setLocation(e.getFrom());
		}
	}
}
