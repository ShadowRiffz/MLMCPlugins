package me.neoblade298.neoreports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Report {
	private int id;
	private String date, user, description, comment;
	private boolean is_resolved, seen, is_urgent;
	private static DateFormat dateformat = new SimpleDateFormat("MM-dd-yy");
	
	public Report(int id, String user, boolean is_urgent) {
		this.id = id;
		this.date = dateformat.format(new Date());
		this.user = user;
		this.comment = "none";
		this.is_resolved = false;
		this.seen = false;
		this.is_urgent = is_urgent;
	}
	
	public Report(int id, String date, String user, String description, String comment, boolean is_resolved, boolean seen, boolean is_urgent) {
		this.id = id;
		this.date = date
		this.user = user;
		this.description = description;
		this.comment = comment;
		this.is_resolved = is_resolved;
		this.seen = seen;
		this.is_urgent = is_urgent;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setResolved(boolean resolved) {
		this.is_resolved = resolved;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	public void setUrgency(boolean urgency) {
		this.is_urgent = urgency;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public boolean isResolved() {
		return this.is_resolved;
	}
	
	public boolean isSeen() {
		return this.seen;
	}
	
	public boolean isUrgent() {
		return this.is_urgent;
	}
	
	public void post(Player p) {
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			// Post the PPR to SQL
			stmt.executeUpdate("INSERT INTO neopprs_pprs VALUES (" + id + ",'" + author + "','" + user + "','" + uuid + "','" + date + "','" + offense + "','" + action + "','" + description +"')");
			
			// Get all alt accounts together
			ArrayList<String> accounts = new ArrayList<String>();
			accounts.add(uuid);
			ResultSet rs = stmt.executeQuery("SELECT * FROM neopprs_alts WHERE uuid = '" + uuid + "';");
			while (rs.next()) {
				accounts.add(rs.getString(6));
			}
			
			// Show all relevant PPRs
			p.sendMessage("§7§m----------");
			for (String account : accounts) {
				rs = stmt.executeQuery("SELECT * FROM neopprs_pprs WHERE uuid = '" + account + "';");
				while (rs.next()) {
					PPR temp = new PPR(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
					temp.show(p);
				}
			}
			p.sendMessage("§4[§c§lMLMC§4] §7Successfully posted PPR!");
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
			p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
		}
	}
	public void modify(Player p) {
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Main.connection, Main.sqlUser, Main.sqlPass);
			Statement stmt = con.createStatement();
			// Post the PPR to SQL
			stmt.executeUpdate("UPDATE neopprs_pprs SET username = '" + user + "', uuid = '" + uuid + "', offense = '" + offense + "', action = '" + action + "', description = '"
			+ description + "' WHERE id = " + id + ";");
			p.sendMessage("§4[§c§lMLMC§4] §7Successfully modified PPR!");
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
			p.sendMessage("§4[§c§lMLMC§4] §cSomething went wrong! Report to neo and don't use the plugin anymore!");
		}
	}
}
