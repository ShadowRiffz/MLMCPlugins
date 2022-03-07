package me.Neoblade298.NeoProfessions.PlayerProfessions;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ProfessionAccount {
	public static ArrayList<String> profNames;
	HashMap<String, Profession> professions;
	OfflinePlayer p;
	
	static {
		profNames.add("stonecutter");
		profNames.add("logger");
		profNames.add("harvester");
		profNames.add("crafter");
	}
	
	public ProfessionAccount(OfflinePlayer p) {
		this.p = p;
		professions = new HashMap<String, Profession>();
		for (String prof : profNames) {
			professions.put(prof, new Profession(p, prof));
		}
	}
	
	public int getLevel(String prof) {
		if (professions.containsKey(prof)) {
			return professions.get(prof).getLevel();
		}
		return -1;
	}
	
	public void addExp(String prof, int exp) {
		if (professions.containsKey(prof)) {
			professions.get(prof).addExp(p, exp);
		}
	}
	
	public HashMap<String, Profession> getProfessions() {
		return professions;
	}
}
