package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.FileReader;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestRecommendation;
import me.neoblade298.neoquests.quests.Questline;

public class NavigationManager implements Reloadable {
	private static HashMap<Player, Pathway> activePathways = new HashMap<Player, Pathway>();
	private static HashMap<String, Pathway> pathways = new HashMap<String, Pathway>();
	private static FileLoader pathwaysLoader;
	
	static {
		pathwaysLoader = (cfg) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					pathways.put(key.toUpperCase(), new Pathway(cfg.getConfigurationSection(key)));
				} catch (NeoIOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public NavigationManager() throws NeoIOException {
		reload();
	}
	
	@Override
	public void reload() throws NeoIOException {
		FileReader.loadRecursive(new File(NeoQuests.inst().getDataFolder(), "pathways"), pathwaysLoader);
	}
	
	@Override
	public String getKey() {
		return "QuestsManager";
	}
	
	public static boolean startNavigation(Player p, String pathway) {
		return true;
	}
	
	public static boolean startNavigation(Player p, Pathway pathway) {
		return true;
	}
}
