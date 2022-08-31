package me.neoblade298.neoleaderboard.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;

import me.neoblade298.neobossinstances.Boss;
import me.neoblade298.neobossinstances.BossInstances;
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointsManager;

public class PointsListener implements Listener, PluginMessageListener {
	public static final double BLOCK_EDIT_POINTS = 0.002;
	public static final double KILL_PLAYER_POINTS = 5;
	public static final double KILL_BOSS_BASE_POINTS = 0.05;
	public static final double MINUTES_ONLINE_POINTS = 0.1;
	private static final HashMap<UUID, Long> deathCooldowns = new HashMap<UUID, Long>();
	private static final long DEATH_COOLDOWN = 600000L;
	
	private HashMap<Player, Long> playtime = new HashMap<Player, Long>();

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
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		playtime.put(e.getPlayer(), System.currentTimeMillis());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}
	
	private void handleLeave(Player p) {
		if (playtime.containsKey(p)) {
			long millisPlayed = System.currentTimeMillis() - playtime.get(p);
			long minutes = millisPlayed / (1000 * 60);
			BungeeAPI.sendPluginMessage("leaderboard_playtime", new String[] {p.getUniqueId().toString(), "" + minutes});
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(msg);
		String subchannel = in.readUTF();
		
		if (!subchannel.startsWith("leaderboard") && !subchannel.equals("neocore_bosskills")) return;
		
		short len = in.readShort();
		byte[] msgbytes = new byte[len];
		in.readFully(msgbytes);
		DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
		
		if (subchannel.equals("neocore_bosskills")) handleBossKillPM(msgin);
		else if (subchannel.equals("leaderboard_playtime")) handlePlaytimePM(msgin);
		try {
			UUID uuid = UUID.fromString(msgin.readUTF());
			String boss = msgin.readUTF();
			Boss b = BossInstances.getBoss(boss);
			if (b != null) {
				String ph = b.getPlaceholder();
				double lv = Integer.parseInt(b.getPlaceholder().substring(ph.indexOf("Lv ") + 3, ph.indexOf(']'))); 
				PointsManager.addPlayerPoints(uuid, KILL_BOSS_BASE_POINTS * lv, PlayerPointType.KILL_BOSS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// UUID, boss
	private void handleBossKillPM(DataInputStream msgin) {
		try {
			UUID uuid = UUID.fromString(msgin.readUTF());
			String boss = msgin.readUTF();
			Boss b = BossInstances.getBoss(boss);
			if (b != null) {
				String ph = b.getPlaceholder();
				double lv = Integer.parseInt(b.getPlaceholder().substring(ph.indexOf("Lv ") + 3, ph.indexOf(']'))); 
				PointsManager.addPlayerPoints(uuid, KILL_BOSS_BASE_POINTS * lv, PlayerPointType.KILL_BOSS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// UUID, long timeOnline
	private void handlePlaytimePM(DataInputStream msgin) {
		try {
			UUID uuid = UUID.fromString(msgin.readUTF());
			int minutes = Integer.parseInt(msgin.readUTF());
			PointsManager.addPlayerPoints(uuid, MINUTES_ONLINE_POINTS * minutes, PlayerPointType.PLAYTIME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
