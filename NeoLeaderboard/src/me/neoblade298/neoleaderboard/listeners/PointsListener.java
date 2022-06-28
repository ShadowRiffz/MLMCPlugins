package me.neoblade298.neoleaderboard.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;

import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointsManager;

public class PointsListener implements Listener, PluginMessageListener {
	private static final double BLOCK_EDIT_POINTS = 0.01;
	private static final double KILL_PLAYER_POINTS = 0.01;
	private static final HashMap<UUID, Long> deathCooldowns = new HashMap<UUID, Long>();
	private static final long DEATH_COOLDOWN = 600000L;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		PointsManager.addPlayerPoints(e.getPlayer().getUniqueId(), BLOCK_EDIT_POINTS, PlayerPointType.EDIT_BLOCK);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		PointsManager.addPlayerPoints(e.getPlayer().getUniqueId(), BLOCK_EDIT_POINTS, PlayerPointType.EDIT_BLOCK);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKill(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Player killer = victim.getKiller();
		
		if (killer == null) return;
		TownyAPI api = TownyAPI.getInstance();
		Resident vr = api.getResident(victim);
		Resident kr = api.getResident(killer);
		Nation vn = vr.getNationOrNull();
		Nation kn = kr.getNationOrNull();
		
		if (vn == null || kn == null) return;
		if (deathCooldownActive(victim.getUniqueId())) return;
		deathCooldowns.put(victim.getUniqueId(), System.currentTimeMillis() + DEATH_COOLDOWN);
		PointsManager.addPlayerPoints(killer.getUniqueId(), KILL_PLAYER_POINTS, PlayerPointType.KILL_PLAYER);
	}
	
	private boolean deathCooldownActive(UUID uuid) {
		if (!deathCooldowns.containsKey(uuid)) return false;
		return System.currentTimeMillis() > deathCooldowns.get(uuid);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
	    if (!channel.equals("BungeeCord")) {
	      return;
	    }
	    
	    ByteArrayDataInput in = ByteStreams.newDataInput(msg);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("SomeSubChannel")) {
	      // Use the code sample in the 'Response' sections below to read
	      // the data.
	    }
	}
}
