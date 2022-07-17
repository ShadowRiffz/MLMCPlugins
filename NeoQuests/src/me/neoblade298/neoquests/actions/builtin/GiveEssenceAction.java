package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class GiveEssenceAction extends RewardAction {
	private static String key = "give-essence";
	private int amount, level;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveEssenceAction());
	}
	
	public GiveEssenceAction() {}
	
	public GiveEssenceAction(LineConfig cfg) {
		super(cfg);
		this.amount = cfg.getInt("amount", 1);
		this.level = cfg.getInt("level", 5);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveEssenceAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return "§f" + amount + " §6Lv " + level + " Essence";
	}

	@Override
	public void run(Player p) {
		CurrencyManager.add(p, level, amount);
	}

}
