package me.neoblade298.neosapiaddons.conditions;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.collect.ImmutableList;
import com.sucy.skill.dynamic.ComponentType;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.custom.EditorOption;

public class InfinityCondition extends CustomEffectComponent {

	@Override
	public String getDescription() {
		return "Check absorption of the player.";
	}

	@Override
	public List<EditorOption> getOptions() {
        return ImmutableList.of(
                EditorOption.text(
                        "amount",
                        "Amount",
                        "Amount of arrows to take",
                        "1")
        );
	}

	@Override
	public boolean execute(LivingEntity caster, int lvl, List<LivingEntity> targets) {
		int amount = settings.getInt("amount");
		Player p = (Player) caster;
		
		PlayerInventory inv = p.getInventory();
		ItemStack[] held = { inv.getItemInMainHand(), inv.getItemInOffHand() };
		boolean hasArrows;
		for (ItemStack item : held) {
			if (item.containsEnchantment(Enchantment.ARROW_INFINITE)) {
				return true;
			}
		}
		
		ItemStack arrow = new ItemStack(Material.ARROW);
		if (inv.contains(arrow)) {
			inv.remove(arrow);
			return true;
		}
		return false;
	}

	@Override
	public String getKey() {
		return "Absorption";
	}

	@Override
	public ComponentType getType() {
		return ComponentType.CONDITION;
	}

}
