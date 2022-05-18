package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfigParser;

public interface Condition extends LineConfigParser<Condition> {
	public boolean passes(Player p);
	public String getExplanation(Player p);
	public ConditionResult getResult();
}
