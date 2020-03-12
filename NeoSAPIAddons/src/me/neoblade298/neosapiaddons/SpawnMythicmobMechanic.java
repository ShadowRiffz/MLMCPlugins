package me.neoblade298.neosapiaddons;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import com.google.common.collect.ImmutableList;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

import io.lumine.xikage.mythicmobs.MythicMobs;

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
                        ImmutableList.of("caster", "target"))
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets) {
		String mob = settings.getString("mob");
		if (settings.getString("target").equalsIgnoreCase("caster")) {
			MythicMobs.inst().getMobManager().spawnMob(mob, caster.getLocation());
		}
		else {
			for (LivingEntity ent : targets) {
				MythicMobs.inst().getMobManager().spawnMob(mob, ent.getLocation());
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
