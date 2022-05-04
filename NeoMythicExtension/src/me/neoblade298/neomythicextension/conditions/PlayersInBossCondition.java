package me.neoblade298.neomythicextension.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import me.neoblade298.neobossinstances.BossInstances;

public class PlayersInBossCondition implements IEntityCondition {
	private int min;
	private int max;
	private String boss;
	protected final BossInstances nbi;
    
    public PlayersInBossCondition(MythicLineConfig mlc, BossInstances nbi) {
        this.boss = mlc.getString(new String[] {"boss", "b"}, "Ratface");
        this.min = mlc.getInteger("min", 0);
        this.max = mlc.getInteger("max", 0);
        this.nbi = nbi;
    }

    public boolean check(AbstractEntity t) {
    	try {
	    	int numPlayers = 0;
			if (nbi.getActiveFights().containsKey(this.boss)) numPlayers = nbi.getActiveFights().get(this.boss).size();
			return min <= numPlayers && numPlayers <= max;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
