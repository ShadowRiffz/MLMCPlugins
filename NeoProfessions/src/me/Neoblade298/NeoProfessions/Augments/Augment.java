package me.Neoblade298.NeoProfessions.Augments;

public abstract class Augment {
	protected static String name;
	protected int level;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Augment.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLine() {
		return "§7[" + name + " Lv " + level + "]";
	}
	
	public static void register() { 
		
	}
	
	public abstract Augment createNew(int level);
}
