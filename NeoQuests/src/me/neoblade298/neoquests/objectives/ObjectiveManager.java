package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.objectives.builtin.FakeObjective;
import me.neoblade298.neoquests.objectives.builtin.GetStoredItemObjective;
import me.neoblade298.neoquests.objectives.builtin.InteractNpcObjective;
import me.neoblade298.neoquests.objectives.builtin.KillMythicmobObjective;
import me.neoblade298.neoquests.objectives.builtin.ReachLocationObjective;

public class ObjectiveManager {
	private static LineConfigManager<Objective> mngr;
	
	public ObjectiveManager() {
		mngr = new LineConfigManager<Objective>(NeoQuests.inst(), "objectives");
		
		register(new InteractNpcObjective());
		register(new GetStoredItemObjective());
		register(new KillMythicmobObjective());
		register(new ReachLocationObjective());
		register(new FakeObjective());
	}
	
	public static ArrayList<ObjectiveSet> parseObjectiveSets(ConfigurationSection cfg) throws NeoIOException {
		ArrayList<ObjectiveSet> objs = new ArrayList<ObjectiveSet>();
		for (String key : cfg.getKeys(false)) {
			ObjectiveSet set = new ObjectiveSet(cfg.getConfigurationSection(key));
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
