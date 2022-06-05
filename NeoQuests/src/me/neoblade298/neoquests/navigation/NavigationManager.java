package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.Reloadable;

public class NavigationManager implements Reloadable {
	private static HashMap<Player, PathwayInstance> activePathways = new HashMap<Player, PathwayInstance>();
	private static HashMap<String, Pathway> pathways = new HashMap<String, Pathway>();
	private static HashMap<Chunk, ArrayList<PathwayPoint>> pointMap = new HashMap<Chunk, ArrayList<PathwayPoint>>();
	private static LinkedList<PathwayPoint> points = new LinkedList<PathwayPoint>();
	private static HashMap<Player, PathwayEditor> pathwayEditors = new HashMap<Player, PathwayEditor>();
	private static FileLoader pathwaysLoader, pointLoader;
	private static File pointFile = new File(NeoQuests.inst().getDataFolder(), "points.yml");
	
	private static LineConfigManager<PathwayPoint> mngr = new LineConfigManager<PathwayPoint>(NeoQuests.inst(), "points");
	
	static {
		mngr.register(new PathwayPoint());
		
		pointLoader = (cfg, file) -> {
			for (String line : cfg.getStringList("points")) {
				try {
					addPoint(mngr.get(new LineConfig(line)));
				} catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load nav point " + line, e);
				}
			}
		};
		
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
		new BukkitRunnable() {
			public void run() {
				for (Player p : pathwayEditors.keySet()) {
					pathwayEditors.get(p).show();
				}
			}
		}.runTaskTimer(NeoQuests.inst(), 0L, 30L);
		reload();
	}
	
	@Override
	public void reload() throws NeoIOException {
		NeoCore.loadFiles(new File(NeoQuests.inst().getDataFolder(), "points.yml"), pointLoader);
		NeoCore.loadFiles(new File(NeoQuests.inst().getDataFolder(), "pathways"), pathwaysLoader);
		for (PathwayPoint point : points) {
			if (!point.isConnected()) {
				NeoQuests.showWarning("The following point has no connections: " + point.getLocation());
			}
		}
	}
	
	@Override
	public String getKey() {
		return "NavigationManager";
	}
	
	public static boolean startNavigation(Player p, String pathway) {
		if (!pathways.containsKey(pathway.toUpperCase())) {
			Bukkit.getLogger().warning("[NeoQuests] Could not start pathway " + pathway + " for player " + p.getName() + ", pathway doesn't exist");
			return false;
		}
		return NavigationManager.startNavigation(p, pathways.get(pathway.toUpperCase()));
	}
	
	public static boolean startNavigation(Player p, Pathway pathway) {
		if (activePathways.containsKey(p)) {
			activePathways.remove(p).cancel("started a new pathway.");
		}
		
		return pathway.start(p) != null;
	}
	
	public static void startPathwayEditor(Player p, String name) throws NeoIOException {
		if (pathwayEditors.containsKey(p)) {
			Util.msg(p, "§cYou are already editing pathway: §6" + pathwayEditors.get(p).getName());
			Util.msg(p, "§cType /nav exit to dispose of your edits if you want to start a new one!");
			return;
		}
		File file = new File(NeoQuests.inst().getDataFolder(), "pathways/" + name + ".yml");
		if (file.exists()) {
			Util.msg(p, "§cThe file name §6" + file.getName() + " already exists! Try another one! You can always rename it later.");
			return;
		}

		Util.msg(p, "Successfully started pathway editor for name: " + name);
		pathwayEditors.put(p, new PathwayEditor(p, name, file));
	}
	
	public static void addPathway(Pathway pw) {
		pathways.put(pw.getKey().toUpperCase(), pw);
	}
	
	public static void savePoints() throws IOException {
		LinkedList<String> serialized = new LinkedList<String>();
		for (PathwayPoint point : points) {
			serialized.add(point.serialize());
		}
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.set("points", serialized);
		cfg.save(pointFile);
	}
	
	public static void showNearbyPoints(Player p) {
		for (Chunk c : Chunk.getSurroundingChunks(p.getLocation())) {
			if (pointMap.containsKey(c)) {
				for (PathwayPoint point : pointMap.get(c)) {
					point.spawnParticle(p);
				}
			}
		}
	}
	
	public static PathwayEditor getEditor(Player p) {
		return pathwayEditors.get(p);
	}
	
	public static PathwayPoint getOrCreatePoint(Location loc) {
		PathwayPoint point = getPoint(loc);
		if (point == null) {
			point = new PathwayPoint(loc, PathwayPointType.POINT);
			addPoint(point);
			return null;
		}
		else {
			return point;
		}
	}
	
	public static PathwayPoint getPoint(Location loc) {
		if (Chunk.containsKey(loc)) {
			Chunk c = Chunk.getChunk(loc);
			if (pointMap.containsKey(c)) {
				for (PathwayPoint point : pointMap.get(c)) {
					if (point.getLocation().equals(loc)) {
						return point;
					}
				}
			}
		}
		return null;
	}
	
	public static void addPoint(PathwayPoint point) {
		Chunk c = Chunk.getOrCreateChunk(point.getLocation());
		ArrayList<PathwayPoint> list = pointMap.getOrDefault(c, new ArrayList<PathwayPoint>());
		list.add(point);
		pointMap.put(c, list);
		points.add(point);
	}
	
	public static boolean deletePoint(Location loc) {
		if (Chunk.containsKey(loc)) {
			Chunk c = Chunk.getChunk(loc);
			if (pointMap.containsKey(c)) {
				PathwayPoint toDelete = null;
				for (PathwayPoint point : pointMap.get(c)) {
					if (point.getLocation().equals(loc)) {
						toDelete = point;
						break;
					}
				}
				if (toDelete != null) {
					pointMap.get(c).remove(toDelete);
					return true;
				}
			}
		}
		return false;
	}
}
