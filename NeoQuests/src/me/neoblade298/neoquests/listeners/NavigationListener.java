package me.neoblade298.neoquests.listeners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.Pathway;
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
		
		e.setCancelled(true);
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			Location loc = e.getClickedBlock().getLocation().add(0.5, 0, 0.5);
			if (p.isSneaking()) {
				deletePoint(p, editor, loc);
			}
			else {
				createOrTogglePoint(p, editor, loc);
			}
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !p.isSneaking()) {
			selectPoint(p, editor, e.getClickedBlock().getLocation().add(0.5, 0, 0.5));
		}
		else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.isSneaking()) {
				undoConnection(p, editor);
			}
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (!e.getItemDrop().getItemStack().getType().equals(Material.STICK)) return;

		e.setCancelled(true);
		editEndpoint(p, editor, e.getPlayer().getTargetBlock(null, 5).getLocation().add(0.5, 0, 0.5), e);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (!editor.editingEndpoint()) return;
		
		PathwayPoint point = editor.getEditingEndpoint();
		e.setCancelled(true);
		if (point.getEndpointKey() == null) {
			point.setKey(e.getMessage().split(" ")[0]);
			Util.msg(p, "Type a display name for the endpoint!");
		}
		else if (point.getDisplay() == null) {
			point.setDisplay(Util.translateColors(e.getMessage()));
			point.setFile(editor.getEndpointFile());
			point.setIsEndpoint(true);
			Util.msg(p, "Endpoint successfully created!");
		}
	}
	
	private void createOrTogglePoint(Player p, PathwayEditor editor, Location loc) {
		PathwayPoint point = NavigationManager.getOrCreatePoint(loc);
		if (point != null) {
			PathwayPointType type = point.toggleType();
			Util.msg(p, "Successfully toggled point to be type &6" + type + "&7!");
		}
		else {
			Util.msg(p, "Successfully created point!");
		}
	}
	
	private void deletePoint(Player p, PathwayEditor editor, Location loc) {
		editor.deletePoint(loc);
	}
	
	private void selectPoint(Player p, PathwayEditor editor, Location loc) {
		PathwayPoint point = NavigationManager.getPoint(loc);
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
	
	private void editEndpoint(Player p, PathwayEditor editor, Location loc, PlayerDropItemEvent e) {
		PathwayPoint point = NavigationManager.getPoint(loc);
		if (point != null) {
			if (point.isEndpoint()) {
				// Only able to remove endpoints if existing path is only connector
				if (point.getFromEndpoints().size() == 0 && point.getToEndpoints().size() == 0) {
					Util.msg(p, "&cCannot remove this endpoint as it is used in paths:");
					for (Pathway pw : point.getFromEndpoints().values()) {
						Util.msg(p, "&7- &6" + pw.getKey());
					}
					for (Pathway pw : point.getToEndpoints().values()) {
						Util.msg(p, "&7- &6" + pw.getKey());
					}
					return;
				}
				point.setIsEndpoint(false);
				Util.msg(p, "Successfully removed endpoint!");
			}
			else {
				editor.editEndpoint(point);
				Util.msg(p, "Type a key for the endpoint (No spaces)!");
			}
		}
	}
}
