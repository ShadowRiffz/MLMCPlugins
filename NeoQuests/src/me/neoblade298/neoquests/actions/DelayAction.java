package me.neoblade298.neoquests.actions;

import java.util.HashMap;

import me.neoblade298.neoquests.io.LineConfig;

public class DelayAction implements DelayableAction, EmptyAction {
	private static final String key = "delay";
	private int delay = 0;
	
	public static void register(HashMap<String, Action> actions) {
		actions.put(key, new DelayAction());
	}
	
	public DelayAction() {}
	
	public DelayAction(LineConfig cfg) {
		delay = Integer.parseInt(cfg.getFullLine().substring("delay ".length()));
	}

	@Override
	public Action newInstance(LineConfig cfg) {
		return new DelayAction(cfg);
	}

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public String getKey() {
		return key;
	}
}
