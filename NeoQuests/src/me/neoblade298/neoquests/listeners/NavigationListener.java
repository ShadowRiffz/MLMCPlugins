package me.neoblade298.neoquests.listeners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.PathwayEditor;
import me.neoblade298.neoquests.navigation.PathwayPoint;
import me.neoblade298.neoquests.navigation.PathwayPointType;

public class NavigationListener implements Listener {
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
		if (e.getItem() == null || !e.getItem().getType().equals(Material.STICK)) return;
		
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			Location loc = e.getClickedBlock().getLocation();
			if (p.isSneaking()) {
				deletePoint(p, editor, loc);
			}
			else {
				createOrTogglePoint(p, editor, loc);
			}
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !p.isSneaking()) {
			selectPoint(p, editor, e.getClickedBlock().getLocation());
		}
		else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.isSneaking()) {
				undoConnection(p, editor);
			}
		}
	}
	
	private void createOrTogglePoint(Player p, PathwayEditor editor, Location loc) {
		PathwayPoint point = editor.getOrCreatePoint(loc);
		if (point != null) {
			PathwayPointType type = point.toggleType();
			Util.msg(p, "Successfully toggled point to be type " + type + "!");
		}
	}
	
	private void deletePoint(Player p, PathwayEditor editor, Location loc) {
		if (NavigationManager.deletePoint(loc)) {
			Util.msg(p, "Successfully deleted point!");
		}
	}
	
	private void selectPoint(Player p, PathwayEditor editor, Location loc) {
		PathwayPoint point = editor.getPoint(loc);
		if (point != null) {
			if (editor.isSelected(point)) {
				editor.deselect();
				return;
			}
			
			editor.selectOrConnectPoints(point);
		}
	}
	
	private void undoConnection(Player p, PathwayEditor editor) {
		editor.undoConnection();
	}
}
