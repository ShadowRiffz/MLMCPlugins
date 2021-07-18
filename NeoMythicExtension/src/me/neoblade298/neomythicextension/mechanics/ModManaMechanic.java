package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ModManaMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;

	public ModManaMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger(new String[] {"a", "amount"}, 5);
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			Player p = (Player) target.getBukkitEntity();
			SkillAPI.getPlayerData(p).giveMana(amount);
			System.out.println("Giving mana " + amount);
			return true;
		}
		return false;
    }
}
