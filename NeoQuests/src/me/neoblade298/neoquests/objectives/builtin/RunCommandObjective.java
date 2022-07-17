package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class RunCommandObjective extends Objective {
	private String command;

	public RunCommandObjective() {
		super();
	}

	public RunCommandObjective(LineConfig cfg) {
		super(ObjectiveEvent.COMMAND, cfg, true);
		command = cfg.getString("command", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new RunCommandObjective(cfg);
	}

	@Override
	public String getKey() {
		return "run-command";
	}

	public boolean checkEvent(PlayerCommandPreprocessEvent e, ObjectiveInstance o) {
		String msg = e.getMessage().substring(1); // Remove / from start of command
		if (command.startsWith("*") && command.endsWith("*")) {
			if (msg.contains(command)) {
				o.incrementCount();
				return true;
			}
		}
		else if (command.startsWith("*")) {
			if (msg.endsWith(command)) {
				o.incrementCount();
				return true;
			}
		}
		else if (command.endsWith("*")) {
			if (msg.startsWith(command)) {
				o.incrementCount();
				return true;
			}
		}
		else {
			if (msg.equalsIgnoreCase(command)) {
				o.incrementCount();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "Hide this objective!";
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
