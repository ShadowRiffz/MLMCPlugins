package me.Neoblade298.NeoProfessions.Items;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import net.md_5.bungee.api.ChatColor;

public class StonecutterItems {
	
	public static ItemStack getOre(String type, int level) {
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		Builder fe = FireworkEffect.builder();
		String oreName = null;
		switch (type) {
			case "strength":
				oreName = "Ruby";
				fe.withColor(Color.BLACK, Color.RED);
				break;
			case "dexterity":
				oreName = "Amethyst";
				fe.withColor(Color.GRAY, Color.PURPLE);
				break;
			case "intelligence":	
				oreName = "Sapphire";
				fe.withColor(Color.BLUE);
				break;
			case "spirit":
				oreName = "Emerald";
				fe.withColor(Color.LIME, Color.LIME, Color.GREEN);
				break;
			case "perception":
				oreName = "Topaz";
				fe.withColor(Color.YELLOW, Color.ORANGE);
				break;
			case "vitality":
				oreName = "Garnet";
				fe.withColor(Color.GRAY, Color.BLACK);
				break;
			case "endurance":
				oreName = "Adamantium";
				fe.withColor(Color.BLACK, Color.RED);
				break;
		}
		ArrayList<String> lore = new ArrayList<String>();
		switch (level) {
			case 1:	meta.setDisplayName("§4[Lv " + level + "] §c " + oreName + " Fragment");
							lore.add("§7Level " + level + " " + oreName + " Ore");
							lore.add("§7Item used for profession crafting");
							break;
			case 2:	meta.setDisplayName("§4[Lv " + level + "] §c " + oreName + " Shard");
							lore.add("§7Level " + level + " " + oreName + " Ore");
							lore.add("§7Item used for profession crafting");
							break;
			case 3:	meta.setDisplayName("§4[Lv " + level + "] §c " + oreName + " Ore");
							lore.add("§7Level " + level + " " + oreName + " Ore");
							lore.add("§7Item used for profession crafting");
							break;
			case 4:	meta.setDisplayName("§4[Lv " + level + "] §c " + oreName + " Cluster");
							lore.add("§7Level " + level + " " + oreName + " Ore");
							lore.add("§7Item used for profession crafting");
							break;
			case 5:	meta.setDisplayName("§4[Lv " + level + "] §c " + oreName + " Gem");
							lore.add("§7Level " + level + " " + oreName + " Ore");
							lore.add("§7Item used for profession crafting");
							break;
		}
		meta.setEffect(fe.build());
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		return item;
	}
}
