package me.neoblade298.neouno;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neoblade298.neouno.Objects.Game;
import me.neoblade298.neouno.Objects.Lobby;
import me.neoblade298.neouno.Commands.Commands;

public class Uno extends JavaPlugin implements org.bukkit.event.Listener {
	// Player data structures
	public HashMap<UUID, Lobby> inlobby;
	public HashMap<UUID, Game> ingame;
	public HashMap<String, Lobby> lobbies;
	public HashMap<String, Game> games;
	public HashMap<String, ChatColor> stringToColor;
	public HashMap<ChatColor, String> colorToString;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoUno Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("uno").setExecutor(new Commands(this));
	    
	    // Initialize data structures
		inlobby = new HashMap<UUID, Lobby>();
		ingame = new HashMap<UUID, Game>();
		lobbies = new HashMap<String, Lobby>();
		games = new HashMap<String, Game>();
		stringToColor = new HashMap<String, ChatColor>();
		colorToString = new HashMap<ChatColor, String>();
		
		stringToColor.put("g", ChatColor.DARK_GREEN);
		stringToColor.put("b", ChatColor.BLUE);
		stringToColor.put("r", ChatColor.RED);
		stringToColor.put("y", ChatColor.YELLOW);
		
		colorToString.put(ChatColor.DARK_GREEN, "green");
		colorToString.put(ChatColor.BLUE, "blue");
		colorToString.put(ChatColor.RED, "red");
		colorToString.put(ChatColor.YELLOW, "yellow");
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoUno Disabled");
	    super.onDisable();
	}
	
}
