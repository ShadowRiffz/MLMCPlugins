package me.neoblade298.neomythicextension.mechanics;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

public class PluginMessageMechanic implements ITargetedEntitySkill {

	protected final String channel;
	protected final String msg;

	@Override
	public ThreadSafetyLevel getThreadSafetyLevel() {
		return ThreadSafetyLevel.SYNC_ONLY;
	}

	public PluginMessageMechanic(MythicLineConfig config) {
		this.channel = config.getString(new String[] { "channel", "c" }, "instance_bosskills");
		this.msg = config.getString(new String[] { "message", "msg", "m" }, "none");
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				Player p = (Player) target.getBukkitEntity();

				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Forward");
				out.writeUTF("ALL");
				out.writeUTF(this.channel);

				ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
				DataOutputStream msgout = new DataOutputStream(msgbytes);
				try {
					msgout.writeUTF(p.getUniqueId().toString()); // You can do anything you want with msgout
					msgout.writeUTF(this.msg);
				} catch (IOException exception) {
					exception.printStackTrace();
				}

				out.writeShort(msgbytes.toByteArray().length);
				out.write(msgbytes.toByteArray());
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
	}
}
