package me.neoblade298.neosapiaddons.mechanics;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.ImmutableList;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.DynamicSkill;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class SpawnMythicmobMechanic extends CustomEffectComponent {

	@Override
	public String getDescription() {
		return "Spawns a mythicmob at all target locations";
	}

	@Override
	public List<EditorOption> getOptions() {
        return ImmutableList.of(
                EditorOption.text(
                        "mob",
                        "Mob",
                        "The internal name of the mythic mob",
                        "Ratface"),
                EditorOption.dropdown(
                        "target",
                        "Target",
                        "Who to target",
                        ImmutableList.of("caster", "target")),
                EditorOption.text(
                        "level",
                        "Level",
                        "The key to use for the level of the mob, or override with int",
                        "max_val")
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets, double critChance) {
		String mob = settings.getString("mob");
		String levelKey = settings.getString("level");
		HashMap<String, Object> data = DynamicSkill.getCastData(caster);
		int moblevel = 1;
		if (StringUtils.isNumeric(levelKey)) {
			moblevel = Integer.parseInt(levelKey);
		}
		else {
			moblevel = (int) ((double) data.get(levelKey));
		}
		MobManager mm = MythicMobs.inst().getMobManager();
		
		if (settings.getString("target").equalsIgnoreCase("caster")) {
			ActiveMob am = mm.spawnMob(mob, caster.getLocation(), moblevel);
			am.setOwner(caster.getUniqueId());
		}
		else {
			for (LivingEntity ent : targets) {
				ActiveMob am = mm.spawnMob(mob, ent.getLocation(), moblevel);
				am.setOwner(caster.getUniqueId());
			}
		}
		return true;
	}

	@Override
	public String getKey() {
		return "Spawn Mythicmob";
	}

	@Override
	public ComponentType getType() {
		return ComponentType.MECHANIC;
	}

}
