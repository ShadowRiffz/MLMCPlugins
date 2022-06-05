package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.HashMap;

import org.bukkit.entity.Player;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;

public class NavigationManager implements Reloadable {
	private static HashMap<Player, PathwayInstance> activePathways = new HashMap<Player, PathwayInstance>();
	private static HashMap<String, Pathway> pathways = new HashMap<String, Pathway>();
	private static FileLoader pathwaysLoader;
	
	static {
		pathwaysLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					if (pathways.containsKey(key)) {
						NeoQuests.showWarning("Duplicate pathway " + key + "in file " + file.getPath() + "/" + file.getName() + ", " +
								"the loaded pathway with this key is in " + pathways.get(key).getFileLocation());
						continue;
					}
					pathways.put(key.toUpperCase(), new Pathway(cfg.getConfigurationSection(key), file));
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
		NeoCore.loadFiles(new File(NeoQuests.inst().getDataFolder(), "pathways"), pathwaysLoader);
	}
	
	@Override
	public String getKey() {
		return "NavigationManager";
	}
	
	public static boolean startNavigation(Player p, String pathway) {
		if (!pathways.containsKey(pathway)) {
			return false;
		}
		return NavigationManager.startNavigation(p, pathways.get(pathway));
	}
	
	public static boolean startNavigation(Player p, Pathway pathway) {
		if (activePathways.containsKey(p)) {
			activePathways.remove(p).stop(false);
		}
		
		return pathway.start(p) != null;
	}
}
