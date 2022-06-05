package me.neoblade298.neoquests.listeners;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.PathwayEditor;
import me.neoblade298.neoquests.navigation.PathwayPoint;

public class NavigationListener implements Listener {
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
		if (e.getItem() == null || !e.getItem().getType().equals(Material.STICK)) return;
		
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			PathwayPoint point = editor.getOrCreatePoint(e.getClickedBlock().getLocation());
			if (point == null) {
				Util.sendMessage(p, "Successfully created new point!");
			}
			else {
				
			}
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
		}
	}
}
