package me.neoblade298.neopvp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.PaginatedList;
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
	private PaginatedList<UUID> uniqueKills;
	private double pvpBalance;
	private int elo, killstreak, wins, losses;
	private long protectionExpires = -1;
	private static final DecimalFormat df = new DecimalFormat("#.##");
	
	private CoreRunnable cr;
	
	public PvpAccount(UUID uuid) {
		this.uuid = uuid;
		this.protectionExpires = System.currentTimeMillis() + (1000 * 60 * 60 * 24); // 24 hours
		this.elo = 1800;
		this.uniqueKills = new PaginatedList<UUID>();
		
		cr = SchedulerAPI.schedule("neopvp-protectionexpires-" + uuid, protectionExpires, new Runnable() {
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
	
	public PaginatedList<UUID> getUniqueKills() {
		return uniqueKills;
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
	
	public void setUniqueKills(PaginatedList<UUID> uniqueKills) {
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
		String prot = "§6Protection: ";
		Util.msg(s, "&6===[&e" + p.getName() + "&6]===", false);
		if (protectionExpires < System.currentTimeMillis()) {
			prot += "&4N/A";
			Util.msg(s, prot, false);
		}
		else {
			double millisToExpiration = protectionExpires - System.currentTimeMillis();
			double hoursLeft = millisToExpiration / 1000 / 60 / 60;
			ComponentBuilder b = new ComponentBuilder(prot + "§eExpires in " + df.format(hoursLeft) + "h ");
			if (s instanceof Player && (Player) s == this.p) {
				b.append("§7§o[Click to remove]", FormatRetention.NONE)
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp removeprotection"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/pvp removeprotection")));
			}
			s.spigot().sendMessage(b.create());
		}
		Util.msg(s, "&6Rating: &e" + elo, false);
		Util.msg(s, "&6Pvp Balance: &e" + pvpBalance, false);
		Util.msg(s, "&6Killstreak: &e" + killstreak, false);
		Util.msg(s, "&6Wins: &e" + wins, false);
		Util.msg(s, "&6Losses: &e" + losses, false);
		ComponentBuilder b = new ComponentBuilder(prot + "§6# of Unique Kills: §e" + uniqueKills.size())
			.append(" §7§o[Click to view]", FormatRetention.NONE)
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp uniquekills " + p.getName()))
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/pvp uniquekills " + p.getName())));
		s.spigot().sendMessage(b.create());
	}
	
	public void redeemBounty(CommandSender s) {
		NeoCore.getEconomy().depositPlayer(p, pvpBalance);
		Util.msg(s, "&7Successfully redeemed &e" + pvpBalance + "g&7.");
		pvpBalance = 0;
		uniqueKills.clear();
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
