package me.neoblade298.neopvp.wars;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.palmergames.bukkit.towny.object.Nation;

import me.neoblade298.neocore.util.Util;

public class War {
	private String key, display;
	private int maxPlayers;
	private Date date;
	private WarTeam[] teams = new WarTeam[2];
	
	public War(String key) {
		this.key = key;
	}
	
	public void setDate(String date) {
		try {
			this.date = new SimpleDateFormat("MM/dd/yyyy").parse(date);
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
	
	public void display(CommandSender s) {
		Util.msg(s, "§6§l>§8§m--------§c§l» " + this.name + " «§8§m--------§6§l<");
		Util.msg(s, "&6Date&7: " + date);
		Util.msg(s, "&6Max Players per Team&7: &e" + maxPlayers);
		
		displayTeam(1, s);
		displayTeam(2, s);
	}
	
	private void displayTeam(int num, CommandSender s) {
		if (teams[num + 1] != null) {
			WarTeam team = teams[num + 1];
			String msg = "&6Team " + num + " &7(&c" + teams[0].getName() + "&7) - ";
			Iterator<Nation> iter = team.getNations().iterator();
			while (iter.hasNext()) {
				msg += "&e" + iter.next().getName();
				if (iter.hasNext()) {
					msg += "&7, ";
				}
			}
			Util.msg(s, msg);
			
			Util.msg(s, "&7- &6Spawn&7: &e" + locationToString(team.getSpawn()));
			Util.msg(s, "&7- &6Mascot Spawn&7: &e" + locationToString(team.getMascotSpawn()));
		}
		else {
			Util.msg(s, "&6Team " + num + "&7: null");
		}
	}
	
	private String locationToString(Location loc) {
		return loc.getX() + " " + loc.getY() + " " +loc.getZ();
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

	public Date getDate() {
		return date;
	}

	public WarTeam[] getTeams() {
		return teams;
	}
}
