package me.neoblade298.neogear.objects;

import java.util.ArrayList;

import me.neoblade298.neogear.Main;

public class ItemSet {
	ArrayList<String> orderedTypes;
	ArrayList<Integer> orderedWeights;
	Main main;
	int maxWeight = 0;
	public ItemSet (Main main, ArrayList<String> contents) {
		this.main = main;
		
		orderedTypes = new ArrayList<String>();
		orderedWeights = new ArrayList<Integer>();
		for (String item : contents) {
			String params[] = item.split(":");
			orderedTypes.add(params[0]);
			if (params.length > 1) {
				int weight = Integer.parseInt(params[1]);
				orderedWeights.add(weight);
				maxWeight += weight;
			}
			else {
				orderedWeights.add(1);
				maxWeight += 1;
			}
		}
	}
	
	public String pickItem() {
		int rand = main.gen.nextInt(maxWeight);
		for (int i = 0; i < orderedTypes.size(); i++) {
			rand -= orderedWeights.get(i);
			if (rand < 0) {
				return orderedTypes.get(i);
			}
		}
		return null;
	}
}
