package me.neoblade298.neocore.io;

import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface IOComponent {
	public void savePlayer(Player p, Statement insert, Statement delete);
	public void preloadPlayer(OfflinePlayer p, Statement insert, Statement delete);
	public void loadPlayer(Player p, Statement insert, Statement delete);
	public void cleanup(Statement insert, Statement delete);
	public String getKey();
}
