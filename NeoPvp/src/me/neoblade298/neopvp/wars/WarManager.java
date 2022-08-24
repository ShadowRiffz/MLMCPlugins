package me.neoblade298.neopvp.wars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.Util;

public class WarManager {
	private static HashMap<String, War> wars = new HashMap<String, War>();
	private static HashMap<String, War> ongoingWars = new HashMap<String, War>();
	private static HashMap<CommandSender, War> creatingWar = new HashMap<CommandSender, War>();
	
	private static final int KILL_POINTS = 5;
	
	public static void initialize() {
		Statement stmt = NeoCore.getStatement();
		Statement delete = NeoCore.getStatement();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM neopvp_wars;");
			
			while(rs.next()) {
				String key = rs.getString(1);
				ResultSet teams = stmt.executeQuery("SELECT * FROM neopvp_warteams WHERE war = '" + key + "';");
				War war = new War(rs, teams);
				
				// War date was before now and never happened, delete it
				if (rs.getLong(4) < System.currentTimeMillis()) {
					delete.addBatch("DELETE FROM neopvp_wars WHERE war = '" + key + "';");
					delete.addBatch("DELETE FROM neopvp_warteams WHERE war = '" + key + "';");
					Bukkit.getLogger().warning("[NeoPvp] Deleted war " + key + " that was never started but is past due.");
					continue;
				}

				wars.put(rs.getString(1), war);
				war.schedule();
			}
			delete.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void newWar(CommandSender s, War war) {
		creatingWar.put(s, war);
	}
	
	public static void displayWarCreation(CommandSender s) {
		if (!creatingWar.containsKey(s)) {
			Util.msg(s, "&cYou aren't currently creating a war!");
			return;
		}
		creatingWar.get(s).displayCreator(s);
	}
	
	public static War getWarCreator(CommandSender s) {
		return creatingWar.get(s);
	}
	
	public static void completeWarCreation(CommandSender s) {
		War war = creatingWar.get(s);
		
		if (war.getDate() == null) {
			Util.msg(s, "&cYou must first set a date!");
			return;
		}
		
		if (war.getMaxPlayers() <= 1) {
			Util.msg(s, "&cYou must set a max # of players above 1!");
			return;
		}
		
		if (!validateTeam(s, war, 1) || !validateTeam(s, war, 2)) {
			return;
		}
		
		creatingWar.remove(s);
		wars.put(war.getKey(), war);
	}
	
	public static void startWar(String key) {
		War war = wars.remove(key);
		ongoingWars.put(key, war);
		war.start();
		
		Statement stmt = NeoCore.getStatement();
		try {
			stmt.addBatch("DELETE FROM neopvp_wars WHERE war = '" + key + "';");
			stmt.addBatch("DELETE FROM neopvp_warteams WHERE war = '" + key + "';");
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static boolean validateTeam(CommandSender s, War war, int num) {
		WarTeam team = war.getTeams()[num - 1];
		if (team == null) {
			Util.msg(s, "&cYou must first create team " + num + "!");
			return false;
		}
		
		if (team.getNations().size() == 0) {
			Util.msg(s, "&cYou must add at least 1 nation to team " + num + "!");
			return false;
		}
		
		if (team.getSpawn() == null) {
			Util.msg(s, "&cYou must add a spawn to team " + num + "!");
			return false;
		}
		
		if (team.getMascotSpawn() == null) {
			Util.msg(s, "&cYou must add a mascot spawn to team " + num + "!");
			return false;
		}
		
		return true;
	}
	
	public static HashMap<String, War> getWars() {
		return wars;
	}
	
	public static HashMap<String, War> getOngoingWars() {
		return ongoingWars;
	}
	
	public static void handleKill(Player killer, Player victim) {
		for (War war : ongoingWars.values()) {
			if (!war.getWorld().equals(killer.getWorld())) continue;
			
			WarTeam teams[] = war.getTeams();
			if (teams[0].isMember(killer) && teams[1].isMember(victim)) {
				teams[0].addPoints(KILL_POINTS);
				teams[1].addPoints(-KILL_POINTS);
			}
			else if (teams[1].isMember(killer) && teams[0].isMember(victim)) {
				teams[1].addPoints(KILL_POINTS);
				teams[0].addPoints(-KILL_POINTS);
			}
		}
	}
}
