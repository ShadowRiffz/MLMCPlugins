package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import me.neoblade298.neocore.bungee.BungeeAPI;

public class PluginMessageMechanic implements ITargetedEntitySkill {

	protected final String channel;
	protected final String msg;

	@Override
	public ThreadSafetyLevel getThreadSafetyLevel() {
		return ThreadSafetyLevel.SYNC_ONLY;
	}

	public PluginMessageMechanic(MythicLineConfig config) {
		this.channel = config.getString(new String[] { "channel", "c" }, "neocore_bosskills");
		this.msg = config.getString(new String[] { "message", "msg", "m" }, "none");
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				Player p = (Player) target.getBukkitEntity();
				BungeeAPI.sendPluginMessage(p, this.channel, p.getUniqueId().toString(), this.msg);
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
	}
}
