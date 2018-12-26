package me.Neoblade298.NeoProfessions.Items;


import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

public class StonecutterItems {
	
	public static ItemStack getStrengthGem(int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		Builder fe = FireworkEffect.builder();
		fe.withColor(Color.BLACK, Color.RED);
		meta.setEffect(fe.build());
		item.setItemMeta(meta);
		return item;
	}
}
