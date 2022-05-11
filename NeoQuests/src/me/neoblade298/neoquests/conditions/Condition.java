package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

public interface Condition {
	public boolean passes(Player p);
	public default ConditionResult getResult() {
		return ConditionResult.INVISIBLE;
	}
}
