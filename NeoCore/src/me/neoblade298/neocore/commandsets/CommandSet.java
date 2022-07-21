package me.neoblade298.neocore.commandsets;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

public class CommandSet {
	private String key;
	private ArrayList<CommandSetEntry> entries = new ArrayList<CommandSetEntry>();
	private HashMap<String, CommandSetVariable> variables = new HashMap<String, CommandSetVariable>();
	private int totalWeight = 0;
	
	public CommandSet(String key, ConfigurationSection cfg) {
		this.key = key;
		
		if (cfg.contains("variables")) {
			ConfigurationSection sec = cfg.getConfigurationSection("variables")
			for (String var : sec.getKeys(false)) {
				variables.put(var, new CommandSetVariable(sec.getConfigurationSection(var)));
			}
		}
		
		ConfigurationSection sec = cfg.getConfigurationSection("entries");
		for (String entry : sec.getKeys(false)) {
			CommandSetEntry cse = new CommandSetEntry(sec.getConfigurationSection(entry));
			entries.add(cse);
			totalWeight += cse.getWeight();
		}
	}
}
