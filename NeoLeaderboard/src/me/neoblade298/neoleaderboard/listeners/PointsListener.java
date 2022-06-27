package me.neoblade298.neoleaderboard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointsManager;

public class PointsListener implements Listener {
	private static final double BLOCK_EDIT = 0.01;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		PointsManager.addPlayerPoints(e.getPlayer().getUniqueId(), BLOCK_EDIT, PlayerPointType.EDIT_BLOCK);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		PointsManager.addPlayerPoints(e.getPlayer().getUniqueId(), BLOCK_EDIT, PlayerPointType.EDIT_BLOCK);
	}
}
