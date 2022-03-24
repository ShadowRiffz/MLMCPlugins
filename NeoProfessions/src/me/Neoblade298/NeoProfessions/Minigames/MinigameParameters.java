package me.Neoblade298.NeoProfessions.Minigames;

import me.Neoblade298.NeoProfessions.Objects.Rarity;

public class MinigameParameters {
	int id;
	double amountMultiplier;
	double rarityWeightMultiplier;
	Rarity minRarity;
	public MinigameParameters() {
		this.id = 0;
		this.amountMultiplier = 1;
		this.rarityWeightMultiplier = 1;
		this.minRarity = Rarity.COMMON;
	}
	
	public MinigameParameters(int id, double amountMultiplier, double rarityWeightMultiplier, Rarity minRarity) {
		this.id = id;
		this.amountMultiplier = amountMultiplier;
		this.rarityWeightMultiplier = rarityWeightMultiplier;
		this.minRarity = minRarity;
	}
	public int getId() {
		return id;
	}
	public double getAmountMultiplier() {
		return amountMultiplier;
	}
	public double getRarityWeightMultiplier() {
		return rarityWeightMultiplier;
	}
	public Rarity getMinRarity() {
		return minRarity;
	}
}
