package me.neoblade298.neomythicextension.mechanics;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import me.neoblade298.neobossinstances.Main;

public class ScaleGoldMechanic implements INoTargetSkill {
	
	protected final int min;
	protected final int max;
	protected final String boss;
	protected final Main nbi;

	public ScaleGoldMechanic(MythicLineConfig config) {
        this.min = config.getInteger("min", 0);
        this.max = config.getInteger("max", 0);
        this.boss = config.getString("boss", "Ratface");
        this.nbi = (Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");
	}

	@Override
	public SkillResult cast(SkillMetadata data) {
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
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		for (Player player : players) {
			String command = "eco give " + player.getName() + " " + (int) gold;
			Bukkit.dispatchCommand(console, command);
			player.sendMessage("§4[§c§lMLMC§4] §7You gained §e" + (int) gold + " §7gold!");
		}
		return SkillResult.SUCCESS;
	}
}
