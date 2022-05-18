package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neoquests.NeoQuests;

public class ObjectiveManager {
	private static LineConfigManager<Objective> mngr;
	
	public ObjectiveManager() {
		mngr = new LineConfigManager<Objective>(NeoQuests.inst(), "objectives");
		
		register(new StoredItemObjective());
	}
	
	public static ArrayList<ObjectiveSet> parseObjectives(ConfigurationSection cfg) {
		ArrayList<ObjectiveSet> objs = new ArrayList<ObjectiveSet>();
		for (String key : cfg.getKeys(false)) {
			ObjectiveSet set = new ObjectiveSet(cfg.getStringList(key));
			if (set != null) {
				objs.add(set);
			}
		}
		return objs;
	}
	
	public static Objective get(LineConfig cfg) throws NeoIOException {
		return mngr.get(cfg);
	}
	
	private void register(Objective obj) {
		mngr.register(obj);
	}
}
