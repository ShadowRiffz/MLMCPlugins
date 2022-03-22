package me.Neoblade298.NeoProfessions.Objects;

import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface IOComponent {
	public void savePlayer(Player p, Connection con, Statement stmt, boolean savingMultiple);
	public void loadPlayer(OfflinePlayer p);
	public void saveAll();
}
