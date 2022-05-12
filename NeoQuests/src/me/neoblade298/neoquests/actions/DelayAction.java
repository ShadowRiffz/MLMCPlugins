package me.neoblade298.neoquests.actions;

import me.neoblade298.neoquests.util.LineConfig;

public class DelayAction implements DelayableAction, EmptyAction {
	private static final String key;
	private int delay = 0;
	
	static { 
		key = "delay";
		Action.register(key, new DelayAction());
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
