package me.neoblade298.neocore.io;

import java.util.HashMap;

import me.neoblade298.neocore.exceptions.NeoIOException;

public class LineConfigManager {
	private HashMap<String, LineConfigParser> parsers = new HashMap<String, LineConfigParser>();
	private String name;
	
	public LineConfigManager(String name) {
		this.name = name;
	}
	
	public void addParser(LineConfigParser parser) {
		parsers.put(parser.getKey(), parser);
	}
	
	public LineConfigParser get(LineConfig cfg) throws NeoIOException {
		if (parsers.containsKey(cfg.getKey())) {
			return parsers.get(cfg.getKey()).create(cfg);
		}
		else {
			throw new NeoIOException(name + " Config Manager Exception: Could not find key " + cfg.getKey());
		}
	}
}
