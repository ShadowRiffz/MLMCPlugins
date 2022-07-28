package me.neoblade298.neomythicextension.conditions;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import me.neoblade298.neocore.NeoCore;

public class HasAccountTagCondition implements IEntityCondition {
	protected String tag;
	protected boolean negate;
    
    public HasAccountTagCondition(MythicLineConfig mlc) {
    	tag = mlc.getString("tag");
    	negate = mlc.getBoolean("negate", false);
    }

    public boolean check(AbstractEntity t) {
    	if (!(t.getBukkitEntity() instanceof Player)) return false;
    	try {
			Player p = (Player) t.getBukkitEntity();
			int account = SkillAPI.getPlayerAccountData(p).getActiveId();
			return NeoCore.getPlayerTags("questaccount_" + account).exists(tag, p.getUniqueId()) ^ negate;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
