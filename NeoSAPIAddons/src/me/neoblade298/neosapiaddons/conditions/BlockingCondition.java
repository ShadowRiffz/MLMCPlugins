package me.neoblade298.neosapiaddons.conditions;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

public class BlockingCondition extends CustomEffectComponent {

	@Override
	public String getDescription() {
		return "Check if player is blocking with shield";
	}

	@Override
	public List<EditorOption> getOptions() {
        return ImmutableList.of(
                EditorOption.dropdown("type", "Type", "Is blocking", ImmutableList.of("Blocking", "Not Blocking"))
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets) {
    	Player p = (Player) caster;
        final boolean blocking = p.isBlocking();
        final boolean wantBlocking = settings.getString("type", "blocking").equalsIgnoreCase("blocking");
        return blocking == wantBlocking && executeChildren(caster, lvl, targets);
	}

	@Override
	public String getKey() {
		return "Blocking";
	}

	@Override
	public ComponentType getType() {
		return ComponentType.CONDITION;
	}

}
