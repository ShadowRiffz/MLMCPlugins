package me.Neoblade298.NeoProfessions.Minigames;

import java.util.HashMap;
import java.util.Set;

import me.Neoblade298.NeoProfessions.Objects.Rarity;

public class MinigameParameters {
	int id;
	double amountMultiplier;
	HashMap<Rarity, Double> rarityWeightMultipliers;
	public MinigameParameters() {
		this.id = 0;
		this.amountMultiplier = 1;
		this.rarityWeightMultipliers = new HashMap<Rarity, Double>();
	}
	
	public MinigameParameters(int id, double amountMultiplier, HashMap<Rarity, Double> rarityWeightMultipliers) {
		this.id = id;
		this.amountMultiplier = amountMultiplier;
		this.rarityWeightMultipliers = rarityWeightMultipliers;
	}
	public int getId() {
		return id;
	}
	public double getAmountMultiplier() {
		return amountMultiplier;
	}
	public void setAmountMultiplier(double amountMultiplier) {
		this.amountMultiplier = amountMultiplier;
	}
	public void addRarityWeightMultiplier(Rarity rarity, double mult) {
		rarityWeightMultipliers.put(rarity, rarityWeightMultipliers.getOrDefault(rarity, 1D) * mult);
	}
	public double getRarityWeightMultiplier(Rarity rarity) {
		return rarityWeightMultipliers.getOrDefault(rarity, 1D);
	}
	public Set<Rarity> getModdedRarities() {
		return rarityWeightMultipliers.keySet();
	}
}
