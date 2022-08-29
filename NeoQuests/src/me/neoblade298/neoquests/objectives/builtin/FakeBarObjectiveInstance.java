package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.bar.BarAPI;
import me.neoblade298.neocore.bar.CoreBar;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;

public class FakeBarObjectiveInstance extends ObjectiveInstance {
	private String barPrefix;

	public FakeBarObjectiveInstance(Player p, Objective obj, ObjectiveSetInstance set, int connection) {
		super(p, obj, set);
		this.barPrefix = "Q-" + set.getKey() + "-";
		
		CoreBar cb = BarAPI.getBar(p);
		Objective connected = set.getSet().getObjectives().get(connection);
		cb.setTitle(connected.getDisplay());
		cb.setTopic(barPrefix + connected.hashCode());
	}
}
