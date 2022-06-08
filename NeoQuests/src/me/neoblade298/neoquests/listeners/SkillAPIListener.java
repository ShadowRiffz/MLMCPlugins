package me.neoblade298.neoquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.sucy.skill.api.event.PlayerAccountChangeEvent;

import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class SkillAPIListener implements Listener {
	public void onAccountChange(PlayerAccountChangeEvent e) {
		Player p = e.getAccountData().getPlayer();
		QuestsManager.getQuester(p, e.getPreviousId()).setLocation(p.getLocation());
		Quester quester = QuestsManager.initializeOrGetQuester(p, e.getNewID());
		if (quester.getLocation() != null) {
			p.teleport(quester.getLocation());
		}
	}
}
