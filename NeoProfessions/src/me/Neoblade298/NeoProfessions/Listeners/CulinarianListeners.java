package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipeChecks;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;
import me.Neoblade298.NeoProfessions.Utilities.CulinarianUtils;

public class CulinarianListeners implements Listener {

	Professions main;
	CulinarianRecipes culinarianRecipes;
	CulinarianUtils culinarianUtils;
	CulinarianRecipeChecks check;
	public HashMap<Player, Integer> drunkness = new HashMap<Player, Integer>();
	HashMap<Player, Integer> numBonuses = new HashMap<Player, Integer>();
	Random gen;

	public CulinarianListeners(Professions main) {
		this.main = main;
		this.culinarianRecipes = main.culinarianRecipes;
		culinarianUtils = new CulinarianUtils();
		check = new CulinarianRecipeChecks(main);
		Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
			public void run() {
				for (Player p : drunkness.keySet()) {
					if (drunkness.get(p) > 0) {
						drunkness.put(p, drunkness.get(p) - 1);
						culinarianUtils.checkAlcoholDown(p, drunkness.get(p));
					}
					else if (drunkness.get(p) <= 0) {
						drunkness.remove(p);
					}
				}
			}
		}, 0, 100L);
		gen = new Random();
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (e.getPlayer() == null || !(e.getPlayer() instanceof Player) || e.getHand().equals(EquipmentSlot.OFF_HAND)) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand();

		boolean quickEat = false;
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			for (String line : item.getItemMeta().getLore()) {
				if (line.contains("Quick Eat") && !item.getType().equals(Material.PRISMARINE_CRYSTALS)) {
					quickEat = true;
					break;
				}
			}
		}
		if (quickEat) {
			ItemStack[] contents = p.getInventory().getContents();
			boolean canGetAttrs = p.getWorld().getName().equalsIgnoreCase("Argyll")
					|| p.getWorld().getName().equalsIgnoreCase("ClassPVP");
			for (int i = 9; i < 36; i++) {
				if (contents[i] != null && contents[i].hasItemMeta() && contents[i].getItemMeta().hasLore()) {
					String id = contents[i].getItemMeta().getLore().get(0);
					ItemStack drink = contents[i].clone();
					drink.setAmount(1);
					p.getInventory().removeItem(drink);

					if (id.contains("Drink 1")) {
						int drunk = 27 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Dexterity", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 2")) {
						int drunk = 22 + gen.nextInt(6);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Spirit", 100);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 3")) {
						int drunk = 17 + gen.nextInt(6);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.04, "Endurance", 120);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 4")) {
						int drunk = 17 + gen.nextInt(6);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.04, "Vitality", 100);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 5")) {
						int drunk = 27 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.06, "Spirit", 100);
							addStat(p, -0.04, "Strength", 100);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 6")) {
						int drunk = 17 + gen.nextInt(6);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.07, "Strength", 80);
							addStat(p, -0.04, "Endurance", 80);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 7")) {
						int drunk = 30 + gen.nextInt(31);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Intelligence", 100);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 8")) {
						int drunk = 12 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.04, "Perception", 100);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 9")) {
						int drunk = 35 + gen.nextInt(10);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.07, "Dexterity", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 10")) {
						int drunk = 35 + gen.nextInt(10);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Strength", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 11")) {
						int drunk = 27 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Intelligence", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 12")) {
						int drunk = 12 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.06, "Vitality", 90);
							addStat(p, -0.04, "Spirit", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 13")) {
						int drunk = 27 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.055, "Spirit", 90);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 14")) {
						int drunk = 40 + gen.nextInt(9);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.05, "Endurance", 60);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 15")) {
						int drunk = 35 + gen.nextInt(10);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.06, "Intelligence", 60);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
					else if (id.contains("Drink 16")) {
						int drunk = 27 + gen.nextInt(7);
						if (drunkness.containsKey(p)) {
							drunkness.put(p, drunkness.get(p) + drunk);
						}
						else {
							drunkness.put(p, drunk);
						}
						culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
						if (canGetAttrs) {
							addStat(p, 0.06, "Strength", 60);
						}
						p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onDrink(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return;
		}

		String id = ChatColor.stripColor(item.getItemMeta().getLore().get(0));
		Player p = e.getPlayer();
		boolean canGetAttrs = p.getWorld().getName().equalsIgnoreCase("Argyll")
				|| p.getWorld().getName().equalsIgnoreCase("ClassPVP");
		if (id.equals("Drink 1")) {
			int drunk = 27 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Dexterity", 90);
			}

		}
		else if (id.equals("Drink 2")) {
			int drunk = 22 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Spirit", 100);
			}

		}
		else if (id.equals("Drink 3")) {
			int drunk = 17 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.04, "Endurance", 120);
			}

		}
		else if (id.equals("Drink 4")) {
			int drunk = 17 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.04, "Vitality", 100);
			}
		}
		else if (id.equals("Drink 5")) {
			int drunk = 27 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.06, "Spirit", 100);
				addStat(p, -0.04, "Strength", 100);
			}
		}
		else if (id.equals("Drink 6")) {
			int drunk = 17 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.07, "Strength", 80);
				addStat(p, -0.04, "Endurance", 80);
			}
		}
		else if (id.equals("Drink 7")) {
			int drunk = 30 + gen.nextInt(31);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Intelligence", 100);
			}
		}
		else if (id.equals("Drink 8")) {
			int drunk = 12 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.04, "Perception", 100);
			}
		}
		else if (id.equals("Drink 9")) {
			int drunk = 35 + gen.nextInt(10);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.07, "Dexterity", 90);
			}
		}
		else if (id.equals("Drink 10")) {
			int drunk = 35 + gen.nextInt(10);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Strength", 90);
			}
		}
		else if (id.equals("Drink 11")) {
			int drunk = 27 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Intelligence", 90);
			}
		}
		else if (id.equals("Drink 12")) {
			int drunk = 12 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.06, "Vitality", 90);
				addStat(p, -0.04, "Spirit", 90);
			}
		}
		else if (id.equals("Drink 13")) {
			int drunk = 27 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.055, "Spirit", 90);
			}
		}
		else if (id.equals("Drink 14")) {
			int drunk = 40 + gen.nextInt(9);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.05, "Endurance", 60);
			}
		}
		else if (id.equals("Drink 15")) {
			int drunk = 35 + gen.nextInt(10);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.06, "Intelligence", 60);
			}
		}
		else if (id.equals("Drink 16")) {
			int drunk = 27 + gen.nextInt(7);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
			if (canGetAttrs) {
				addStat(p, 0.06, "Strength", 60);
			}
		}
		else if (id.equals("Ingredient 22")) {
			int drunk = 22 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
		}
		else if (id.equals("Ingredient 23")) {
			int drunk = 22 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
		}
		else if (id.equals("Ingredient 24")) {
			int drunk = 22 + gen.nextInt(6);
			if (drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
			}
			else {
				drunkness.put(p, drunk);
			}
			culinarianUtils.checkAlcoholUp(p, drunkness.get(p), drunkness);
		}
	}

	@EventHandler
	public void onPrepItemCraft(PrepareItemCraftEvent e) {
		if (e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			CraftingInventory inv = e.getInventory();
			if (inv.getResult() == null || !inv.getResult().hasItemMeta()
					|| !inv.getResult().getItemMeta().hasDisplayName()) {
				return;
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Vodka")) {
				check.checkVodka(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Rum")) {
				check.checkRum(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Tequila")) {
				check.checkTequila(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Black Widow")) {
				check.checkBlackWidow(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Pink Panther")) {
				check.checkPinkPanther(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Midnight Kiss")) {
				check.checkMidnightKiss(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Midnight Blue")) {
				check.checkMidnightBlue(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Good and Evil")) {
				check.checkGoodAndEvil(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Thor's Hammer")) {
				check.checkThorHammer(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Jack Frost")) {
				check.checkJackFrost(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("White Russian")) {
				check.checkWhiteRussian(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Swamp Water")) {
				check.checkSwampWater(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Blue Motorcycle")) {
				check.checkBlueMotorcycle(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Red Death")) {
				check.checkRedDeath(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Bombsicle")) {
				check.checkBombsicle(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Sweet Tart")) {
				check.checkSweetTart(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Pina Colada")) {
				check.checkPinaColada(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Margarita on the Rocks")) {
				check.checkMargaritaOnTheRocks(p, inv);
			}
			else if (inv.getResult().getItemMeta().getDisplayName().contains("Bloody Mary")) {
				check.checkBloodyMary(p, inv);
			}
		}
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if (!p.getWorld().getName().equalsIgnoreCase("Argyll")
				&& !p.getWorld().getName().equalsIgnoreCase("ClassPVP")) {
			if (numBonuses.containsKey(p)) {
				numBonuses.remove(p);
			}
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (numBonuses.containsKey(p)) {
			numBonuses.remove(p);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (numBonuses.containsKey(p)) {
			numBonuses.remove(p);
		}
	}

	public void addStat(Player p, double percent, String attr, int seconds) {
		PlayerData data = SkillAPI.getPlayerData(p);
		int oldAttr = data.getAttribute(attr);
		int newAttr = (int) (oldAttr * percent);
		data.addBonusAttributes(attr, newAttr);
		if (numBonuses.containsKey(p)) {
			numBonuses.put(p, numBonuses.get(p) + 1);
		}
		else {
			numBonuses.put(p, 1);
		}
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			public void run() {
				if (numBonuses.containsKey(p) && numBonuses.get(p) > 0) {
					data.addBonusAttributes(attr, -newAttr);
				}
			}
		}, seconds * 20);
	}

}
