package me.neoblade298.neomythicextension.mechanics;

import java.util.ArrayList;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import me.neoblade298.neobossinstances.BossInstances;
import me.neoblade298.neomythicextension.MythicExt;

public class ScaleGoldMechanic implements INoTargetSkill {
	
	protected final int min;
	protected final int max;
	protected final String boss;
	protected final BossInstances nbi;

    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }

	public ScaleGoldMechanic(MythicLineConfig config, BossInstances nbi) {
        this.min = config.getInteger("min", 0);
        this.max = config.getInteger("max", 0);
        this.boss = config.getString("boss", "Ratface");
        this.nbi = nbi;
	}

	@Override
	public SkillResult cast(SkillMetadata data) {
		try {
			if (data.getCaster().getLevel() < 1) {
				return SkillResult.CONDITION_FAILED;
			}
			
			double scale = Math.min(2, 1 + (0.02 * (data.getCaster().getLevel() - 1)));
			double scaledMin = this.min * scale;
			double scaledMax = this.max * scale;
			ArrayList<Player> players = nbi.getActiveFights().get(this.boss);
	
			// Get gold min and max for party size, generate gold
			double gold = Math.random() * (scaledMax - scaledMin) + scaledMin;
			gold = Math.round(gold / 25.0D) * 25L;
			
			// Give gold to each player
			for (Player player : players) {
				MythicExt.econ.depositPlayer(player, gold);
				player.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + (int) gold + " §7gold!");
			}
			return SkillResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
	}
}
