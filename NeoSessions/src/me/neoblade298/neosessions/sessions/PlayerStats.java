package me.neoblade298.neosessions.sessions;

public class PlayerStats {
	private String key;
	private double damageDealt, damageTaken, selfHealed, allyHealed;
	private long startTime;
	
	public PlayerStats(String key) {
		this.key = key;
		damageDealt = 0;
		damageTaken = 0;
		selfHealed = 0;
		allyHealed = 0;
	}

	public String getKey() {
		return key;
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
