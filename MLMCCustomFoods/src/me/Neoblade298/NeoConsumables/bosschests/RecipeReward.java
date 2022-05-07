package me.Neoblade298.NeoConsumables.bosschests;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoConsumables.Util;

public class RecipeReward extends ChestReward {
	private static Random gen = new Random();
	private static HashMap<String, Integer> maxes = new HashMap<String, Integer>();
	private String tier;
	
	static {
		maxes.put("ingr", 26);
		maxes.put("t1", 20);
		maxes.put("t2", 22);
		maxes.put("t3", 20);
		maxes.put("tlegend", 10);
	}
	
	public RecipeReward(String tier) {
		this.tier = tier;
	}

	@Override
	public void giveReward(Player p) {
		int num = gen.nextInt(maxes.get(tier));
		String recipe = "reci_" + tier;
		if (!tier.equalsIgnoreCase("Ingr") && !tier.equalsIgnoreCase("drink")) {
			recipe += "r";
		}
		recipe += num;
		Util.serverCommand("mythicmobs items give -s " + p.getName() + " " + recipe);
	}

	@Override
	public void sendMessage(Player p) {
		Util.sendMessage(p, "&7- a &9Recipe&7!");
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}
	
	public static RecipeReward parse(String args[]) {
		String tier = "ingr";
		int weight = 1;
		for (String arg : args) {
			if (arg.startsWith("tier")) {
				tier = arg.substring(arg.indexOf(":") + 1);
			}
			else if (arg.startsWith("weight")) {
				weight = Integer.parseInt(arg.substring(arg.indexOf(":") + 1));
			}
		}
		RecipeReward r = new RecipeReward(tier);
		r.setWeight(weight);
		return r;
	}

	
	@Override
	public String toString() {
		return "Recipes";
	}
}
