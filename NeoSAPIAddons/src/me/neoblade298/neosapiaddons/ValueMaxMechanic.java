package me.neoblade298.neosapiaddons;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.DynamicSkill;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

public class ValueMaxMechanic extends CustomEffectComponent {

	@Override
	public String getDescription() {
		return "Get the maximum of all available attributes.";
	}

	@Override
	public List<EditorOption> getOptions() {
        return ImmutableList.of(
                EditorOption.text(
                        "key",
                        "Key",
                        "The key to place the max into",
                        "max_val"),
                EditorOption.text(
                        "attributes",
                        "Attributes",
                        "The attributes with which to max, separated by :",
                        "Strength:Dexterity:Intelligence:Spirit")
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets) {
		String[] attrs = settings.getString("attributes").split(" ");
		Player p = (Player) caster;
		PlayerData d = SkillAPI.getPlayerData(p);
		int max = 0;
		for (String attr : attrs) {
			int attrVal = d.getAttribute(attr);
			if (attrVal > max) {
				max = attrVal;
			}
		}

		String key = settings.getString("key");
		HashMap<String, Object> data = DynamicSkill.getCastData(caster);
		data.put(key, max);
		return true;
	}

	@Override
	public String getKey() {
		return "Value Max";
	}

	@Override
	public ComponentType getType() {
		return ComponentType.MECHANIC;
	}

}
