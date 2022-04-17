package me.Neoblade298.NeoProfessions.Objects;

import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface IOComponent {
	public void savePlayer(Player p, Statement stmt);
	public void loadPlayer(OfflinePlayer p, Statement stmt);
	public void cleanup(Statement stmt);
	public String getComponentName();
}
