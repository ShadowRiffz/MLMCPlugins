package me.neoblade298.neosapiaddons;

public class PointSet {
	private int basePoints;
	private int pointsPerLvl;
	private int baseLvl;
	
	public PointSet(int basePoints, int pointsPerLvl, int baseLvl) {
		this.basePoints = basePoints;
		this.pointsPerLvl = pointsPerLvl;
		this.baseLvl = baseLvl;
	}

	public int getBasePoints() {
		return basePoints;
	}

	public void setBasePoints(int basePoints) {
		this.basePoints = basePoints;
	}

	public int getPointsPerLvl() {
		return pointsPerLvl;
	}

	public void setPointsPerLvl(int pointsPerLvl) {
		this.pointsPerLvl = pointsPerLvl;
	}

	public int getBaseLvl() {
		return baseLvl;
	}

	public void setBaseLvl(int baseLvl) {
		this.baseLvl = baseLvl;
	}
	
}
