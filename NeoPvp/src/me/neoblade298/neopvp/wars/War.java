package me.neoblade298.neopvp.wars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import me.neoblade298.neocore.scheduler.SchedulerAPI;
import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class War {
	private String key, display;
	private int maxPlayers;
	private Calendar date = Calendar.getInstance();
	private long endTime;
	private WarTeam[] teams = new WarTeam[2];
	private World w;
	private boolean isOngoing = false;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm a z");
	
	private static final int DEFAULT_WAR_HOUR = 14;
	
	public War(String key) {
		this.key = key;
	}
	
	public War(ResultSet war, ResultSet teams) {
		try {
			war.next();
			this.key = war.getString(1);
			this.display = war.getString(2);
			this.maxPlayers = war.getInt(3);
			this.date.setTimeInMillis(war.getLong(4));
			
			teams.next();
			this.teams[0] = new WarTeam(this.key, teams);
			teams.next();
			this.teams[1] = new WarTeam(this.key, teams);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void schedule() {
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				start();
			}
		};
		SchedulerAPI.schedule("War-" + this.key, this.date.getTimeInMillis(), runnable);
	}
	
	public void start() {
		isOngoing = true;
		endTime = System.currentTimeMillis() + (1000 * 60 * 60); // 1 hr
		for (WarTeam team : teams) {
			team.startWar();
		}
	}
	
	public boolean isOngoing() {
		return isOngoing;
	}
	
	public void setDate(String src) {
		setDate(src, DEFAULT_WAR_HOUR);
	}
	
	public void setDate(String src, int hour) {
		try {
			this.date.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(src));
			this.date.set(Calendar.HOUR_OF_DAY, hour);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public void setTeam(int num, String team) {
		teams[num - 1] = new WarTeam(team);
	}
	
	public void addTeamNation(int num, Nation n) {
		teams[num - 1].addNation(n);
	}
	
	public void removeTeamNation(int num, Nation n) {
		teams[num - 1].removeNation(n);
	}
	
	public void setTeamSpawn(int num, Location loc) {
		teams[num - 1].setSpawn(loc);
	}
	
	public void setMascotSpawn(int num, Location loc) {
		teams[num - 1].setMascotSpawn(loc);
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public void displayCreator(CommandSender s) {
		Util.msg(s, "§6§l>§8§m--------§c§l» " + this.display + " «§8§m--------§6§l<", false);
		Util.msg(s, "&6Date&7: " + sdf.format(date.getTime()), false);
		Util.msg(s, "&6World&7: " + w == null ? "Not set" : w.getName());
		Util.msg(s, "&6Max Players per Team&7: &e" + maxPlayers, false);
		
		displayTeamCreator(1, s);
		displayTeamCreator(2, s);
	}
	
	public void display(CommandSender s) {
		Util.msg(s, "§6§l>§8§m--------§c§l» " + this.display + " «§8§m--------§6§l<", false);
		displayTeam(1, s);
		displayTeam(2, s);
	}
	
	private void displayTeamCreator(int num, CommandSender s) {
		if (teams[num - 1] != null) {
			WarTeam team = teams[num - 1];
			String msg = "&6Team " + num + " &7(&c" + team.getDisplay() + "&7) - ";
			Iterator<Nation> iter = team.getNations().iterator();
			while (iter.hasNext()) {
				msg += "&e" + iter.next().getName();
				if (iter.hasNext()) {
					msg += "&7, ";
				}
			}
			Util.msg(s, msg);
			
			Util.msg(s, "&7- &6Spawn&7: &e" + (team.getSpawn() == null ? "Not set" : Util.locToString(team.getSpawn())));
			Util.msg(s, "&7- &6Mascot Spawn&7: &e" + (team.getMascotSpawn() == null ? "Not set" : Util.locToString(team.getMascotSpawn())));
		}
		else {
			Util.msg(s, "&6Team " + num + "&7: null");
		}
	}
	
	private void displayTeam(int num, CommandSender s) {
		WarTeam team = teams[num - 1];
		String msg = "&6Team " + num + " &7(&c" + team.getDisplay() + "&7) - &f" +
				team.getPoints();

		ComponentBuilder builder = new ComponentBuilder(Util.translateColors(msg))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(buildTeamHover(team))));
		s.spigot().sendMessage(builder.create());
	}
	
	private String buildTeamHover(WarTeam team) {
		String msg = "";
		
		// Nations
		Iterator<Nation> iter = team.getNations().iterator();
		while (iter.hasNext()) {
			msg += "&e" + iter.next().getName();
			if (iter.hasNext()) {
				msg += "&7, ";
			}
		}
		msg += "\n";
		
		// Towns
		Iterator<Town> itertown = team.getWhitelistedTowns().iterator();
		while (itertown.hasNext()) {
			msg += "&e" + itertown.next().getName();
			if (itertown.hasNext()) {
				msg += "&7, ";
			}
		}
		msg += "\n";
		
		// Players
		Iterator<String> iterplayer = team.getWhitelistedPlayers().iterator();
		while (iterplayer.hasNext()) {
			msg += "&e" + iterplayer.next();
			if (iterplayer.hasNext()) {
				msg += "&7, ";
			}
		}
		msg += "\n";
		
		msg += "&6Kills&7: &e" + team.getKills() + "\n&6Deaths&7: &e" + team.getDeaths() + "\n&6Mascot Health Lost&7: &e" + team.getMascotHealthLost();
		return msg;
	}
	
	public String getKey() {
		return key;
	}

	public String getDisplay() {
		return display;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public Calendar getDate() {
		return date;
	}

	public WarTeam[] getTeams() {
		return teams;
	}
	
	public World getWorld() {
		return w;
	}
	
	public void setWorld(World w) {
		this.w = w;
	}
	
	public long getTimeToEnd() {
		return this.endTime;
	}
}
