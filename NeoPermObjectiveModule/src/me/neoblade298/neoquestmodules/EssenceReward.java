package me.neoblade298.neoquestmodules;

import java.util.Map;

import org.bukkit.entity.Player;
import com.sucy.skill.log.Logger;

import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.blackvein.quests.CustomReward;

public class EssenceReward extends CustomReward {
	// Construct the reward
	public EssenceReward() {
		this.setName("Class Exp Reward");
		this.setAuthor("Neoblade298");
		this.setDisplay("Essence");
		this.addStringPrompt("Amount", "How much essence to give", 5);
		this.addStringPrompt("Level", "The level of the essence", 5);
	}

	// Give loot reward to a player
	@Override
	public void giveReward(Player player, Map<String, Object> data) {
		try {
			int amount = Integer.parseInt((String) data.get("Amount"));
			int level = Integer.parseInt((String) data.get("Level"));
			CurrencyManager.add(player, level, amount);
		} catch (Exception ex) {
			Logger.bug("Something went wrong with Quest Essence Reward:");
			ex.printStackTrace();
		}
	}
}