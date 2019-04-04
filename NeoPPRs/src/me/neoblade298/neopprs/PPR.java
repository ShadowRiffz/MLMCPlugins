package me.neoblade298.neopprs;

import org.bukkit.Bukkit;

public class PPR {
	private int id;
	private String author, user, uuid, date, offense, action, description;
	
	public PPR(int id) {
		this.id = id;
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
}
