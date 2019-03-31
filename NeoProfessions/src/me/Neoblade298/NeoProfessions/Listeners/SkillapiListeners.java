package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sucy.skill.api.event.PlayerSkillDowngradeEvent;

import me.Neoblade298.NeoProfessions.Main;

public class SkillapiListeners implements Listener {
	
	Main main;
	
	public SkillapiListeners(Main main) {
		this.main = main;
	}

	
	@EventHandler
	public void onLevelUp(PlayerSkillDowngradeEvent e) {
		String rpgClass = e.getDowngradedSkill().getPlayerClass().getData().getName();
		Player p = e.getPlayerData().getPlayer();
		if (rpgClass.equalsIgnoreCase("Mason") ||
			rpgClass.equalsIgnoreCase("Culinarian") ||
			rpgClass.equalsIgnoreCase("Blacksmith") ||
			rpgClass.equalsIgnoreCase("Stonecutter")) {
			p.sendMessage("§cYou cannot downgrade profession skills!");
			e.setCancelled(true);
		}
	}
}
