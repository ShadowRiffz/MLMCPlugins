package me.neoblade298.neocore.io;

import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface IOComponent {
	public void savePlayer(Player p, Statement stmt);
	public void preloadPlayer(OfflinePlayer p, Statement stmt);
	public void loadPlayer(Player p, Statement stmt);
	public void cleanup(Statement stmt);
	public String getKey();
}
