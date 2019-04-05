package me.neoblade298.neopprs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PPR {
	private int id;
	private String author, user, uuid, date, offense, action, description;
	private static DateFormat dateformat = new SimpleDateFormat("dd-MM-yy");
	
	public PPR(int id, String author) {
		this.id = id;
		this.author = author;
		this.date = dateformat.format(new Date());
	}
	
	public PPR(int id, String author, String user, String uuid, String date, String offense, String action, String description) {
		this.id = id;
		this.author = author;
		this.user = user;
		this.uuid = uuid;
		this.date = date;
		this.offense = offense;
		this.action = action;
		this.description = description;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@SuppressWarnings("deprecation")
	public void setUser(String user) {
		this.user = user;
		this.uuid = Bukkit.getServer().getOfflinePlayer(user).getUniqueId().toString();
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setOffense(String offense) {
		this.offense = offense;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getOffense() {
		return this.offense;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void preview(Player p) {
		p.sendMessage("§7-- §c: " + user + " §7--");
		p.sendMessage("§cPPR ID§7: " + id);
		p.sendMessage("§cDate§7: " + date);
		p.sendMessage("§cOffense§7: " + offense);
		p.sendMessage("§cAction§7: " + action);
		p.sendMessage("§cDescription§7: " + description);
		p.sendMessage("§cPosted by§7: " + author);
	}
	
	public void show(Player p) {
		p.sendMessage("§cPPR ID§7: " + id);
		p.sendMessage("§cOffense§7: " + offense);
		p.sendMessage("§cAction§7: " + action);
		p.sendMessage("§cDescription§7: " + description);
	}
	
	public void post(Player p) {
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO neopprs_pprs VALUES (" + id + "," + user + "," + uuid + "," + date + "," + offense + "," + action + "," + description +")");
			ResultSet rs = stmt.executeQuery("SELECT * FROM neopprs_pprs WHERE uuid = '" + uuid + "';");
			p.sendMessage("§7-- §c: " + user + " §7--");
			while (rs.next()) {
				PPR temp = new PPR(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				temp.show(p);
			}
			con.close();
			p.sendMessage("§4[§c§lMLMC§4] §7Successfully posted PPR!");
		}
		catch(Exception e) {
			System.out.println(e);
			p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
		}  
	}
}
