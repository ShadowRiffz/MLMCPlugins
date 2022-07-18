package me.neoblade298.neopvp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.util.SchedulerAPI;
import me.neoblade298.neocore.util.SchedulerAPI.CoreRunnable;
import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PvpAccount {
	private UUID uuid;
	private Player p;
	private HashSet<UUID> uniqueKills = new HashSet<UUID>();
	private double pvpBalance;
	private int elo, killstreak, wins, losses;
	private long protectionExpires = -1;
	
	private CoreRunnable cr;
	
	public PvpAccount(UUID uuid) {
		this.uuid = uuid;
		protectionExpires = System.currentTimeMillis() + (1000 * 60 * 60 * 24); // 24 hours
		
		cr = SchedulerAPI.schedule("neopvp-protectionexpires-" + p.getUniqueId(), protectionExpires, new Runnable() {
			public void run() {
				removeProtection();
			}
		});
	}
	
	public void loadPlayer() {
		this.p = Bukkit.getPlayer(this.uuid);
	}
	
	public void removeProtection() {
		// If statement for in case a scheduler removes protection when it's already gone
		if (this.protectionExpires != -1) {
			this.protectionExpires = -1;
			Util.msg(p, "Your protection was removed.");
		}
	}
	
	public void addProtection(long timeInMillis) {
		if (protectionExpires < System.currentTimeMillis()) {
			protectionExpires = System.currentTimeMillis();
		}
		protectionExpires += timeInMillis;
		
		if (cr != null) {
			cr.setCancelled(true);
		}
		
		cr = SchedulerAPI.schedule("neopvp-protectionexpires-" + p.getUniqueId(), protectionExpires, new Runnable() {
			public void run() {
				removeProtection();
			}
		});
	}
	
	public PvpAccount(UUID uuid, ResultSet rs) throws SQLException {
		this.uuid = uuid;
		killstreak = rs.getInt(2);
		wins = rs.getInt(3);
		losses = rs.getInt(4);
		elo = rs.getInt(5);
		pvpBalance = rs.getDouble(6);
		protectionExpires = rs.getLong(7);
		
		if (protectionExpires > System.currentTimeMillis()) {
			System.out.println(protectionExpires + " " + System.currentTimeMillis());
			cr = SchedulerAPI.schedule("neopvp-protectionexpires-" + uuid, protectionExpires, new Runnable() {
				public void run() {
					System.out.println("TEST");
					removeProtection();
				}
			});
		}
	}
	
	public void addElo(int amount) {
		elo += amount;
	}
	
	public void takeElo(int amount) {
		addElo(-amount);
	}
	
	public int getElo() {
		return elo;
	}
	
	public double getBalance() {
		return pvpBalance;
	}
	
	public void setBalance(double balance) {
		pvpBalance = balance;
	}
	
	public void addBalance(double balance) {
		pvpBalance += balance;
	}
	
	public int getNumUniqueKills() {
		return uniqueKills.size();
	}
	
	public void addUniqueKill(Player p) {
		uniqueKills.add(p.getUniqueId());
	}
	
	public void incrementKillstreak() {
		killstreak++;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public void clearKillstreak() {
		killstreak = 0;
	}
	
	public void incrementWins() {
		wins++;
	}
	
	public void incrementLosses() {
		losses++;
	}
	
	public void setUniqueKills(HashSet<UUID> uniqueKills) {
		this.uniqueKills = uniqueKills;
	}
	
	public int getWins() {
		return wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public boolean isProtected() {
		return protectionExpires > System.currentTimeMillis();
	}
	
	public void displayAccount(CommandSender s) {
		String prot = "&6Protection: ";
		if (protectionExpires < System.currentTimeMillis()) {
			prot += "&4N/A";
		}
		else {
			ComponentBuilder b = new ComponentBuilder(prot + "&eExpires in " + (protectionExpires - System.currentTimeMillis()) + "s ");
			if (s instanceof Player && (Player) s == this.p) {
				b.append("&7&o[Click to remove]", FormatRetention.NONE)
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp disableprotection"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/pvp disableprotection")));
			}
			s.spigot().sendMessage(b.create());
		}
		Util.msg(s, "&c===[&6" + p.getName() + "&6]===");
		Util.msg(s, prot);
		Util.msg(s, " &6Rating: &e" + elo);
		Util.msg(s, " &6Pvp Balance: &e" + pvpBalance);
		Util.msg(s, " &6Killstreak: &e" + killstreak);
		Util.msg(s, " &6Wins: &e" + wins);
		Util.msg(s, " &6Losses: &e" + losses);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Player getPlayer() {
		return this.p;
	}
	
	public long getProtectionExpiration() {
		return protectionExpires;
	}
}
