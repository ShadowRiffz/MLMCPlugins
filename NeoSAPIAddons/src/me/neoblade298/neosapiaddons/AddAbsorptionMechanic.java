package me.neoblade298.neosapiaddons;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import com.google.common.collect.ImmutableList;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

public class AddAbsorptionMechanic extends CustomEffectComponent {

	@Override
	public String getDescription() {
		return "Add absorption to the player.";
	}

	@Override
	public List<EditorOption> getOptions() {
        return ImmutableList.of(
                EditorOption.text(
                        "amount",
                        "Amount",
                        "How much absorption toadd",
                        "max_val")
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets) {
		int amt = settings.getInt("amount");
		caster.setAbsorptionAmount(caster.getAbsorptionAmount() + amt);
		return true;
	}

	@Override
	public String getKey() {
		return "Add Absorption";
	}

	@Override
	public ComponentType getType() {
		return ComponentType.MECHANIC;
	}

}
