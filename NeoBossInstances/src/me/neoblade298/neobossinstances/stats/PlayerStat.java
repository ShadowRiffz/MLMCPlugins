package me.neoblade298.neobossinstances.stats;

public class PlayerStat {
	String boss;
	private double damageDealt;
	private double damageTaken;
	private double selfHealed;
	private double allyHealed;
	private long startTime;
	
	public PlayerStat(String boss) {
		this.boss = boss;
		damageDealt = 0;
		damageTaken = 0;
		selfHealed = 0;
		allyHealed = 0;
	}

	public String getBoss() {
		return boss;
	}

	public void setBoss(String boss) {
		this.boss = boss;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public void addDamageDealt(double amount) {
		damageDealt += amount;
	}
	
	public void addDamageTaken(double amount) {
		damageTaken += amount;
	}
	
	public void addSelfHeal(double amount) {
		selfHealed += amount;
	}
	
	public void addAllyHeal(double amount) {
		allyHealed += amount;
	}
	
	public double getDamageDealt() {
		return damageDealt;
	}
	
	public double getDamageTaken() {
		return damageTaken;
	}
	
	public double getSelfHealed() {
		return selfHealed;
	}
	
	public double getAllyHealed() {
		return allyHealed;
	}
	
}
