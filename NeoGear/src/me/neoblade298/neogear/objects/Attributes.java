package me.neoblade298.neogear.objects;

public class Attributes {
	public int strBase;
	public int strPerLvl;
	public int strRange;
	public int dexBase;
	public int dexPerLvl;
	public int dexRange;
	public int intBase;
	public int intPerLvl;
	public int intRange;
	public int sprBase;
	public int sprPerLvl;
	public int sprRange;
	public int prcBase;
	public int prcPerLvl;
	public int prcRange;
	public int endBase;
	public int endPerLvl;
	public int endRange;
	public int vitBase;
	public int vitPerLvl;
	public int vitRange;
	
	public Attributes (int strBase, int strPerLvl, int strRange, int dexBase, int dexPerLvl, int dexRange, int intBase, int intPerLvl, int intRange,
			int sprBase, int sprPerLvl, int sprRange, int prcBase, int prcPerLvl, int prcRange, int endBase, int endPerLvl, int endRange, int vitBase,
			int vitPerLvl, int vitRange) {
		this.strBase = strBase;
		this.strPerLvl = strPerLvl;
		this.dexBase = dexBase;
		this.dexPerLvl = dexPerLvl;
		this.intBase = intBase;
		this.intPerLvl = intPerLvl;
		this.sprBase = sprBase;
		this.sprPerLvl = sprPerLvl;
		this.prcBase = prcBase;
		this.prcPerLvl = prcPerLvl;
		this.endBase = endBase;
		this.endPerLvl = endPerLvl;
		this.vitBase = vitBase;
		this.vitPerLvl = vitPerLvl;
	}
}
