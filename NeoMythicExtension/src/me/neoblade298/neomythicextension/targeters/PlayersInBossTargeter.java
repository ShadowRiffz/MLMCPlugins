package me.neoblade298.neomythicextension.targeters;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;
import me.neoblade298.neobossinstances.Main;

public class PlayersInBossTargeter extends IEntitySelector {

	protected final String boss;
	protected final me.neoblade298.neobossinstances.Main nbi;

	public PlayersInBossTargeter(MythicLineConfig config) {
		super(MythicBukkit.inst().getSkillManager(), config);
        this.boss = config.getString(new String[] {"boss", "b"}, "Ratface");
        this.nbi = (Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
		if (nbi.getActiveFights().containsKey(this.boss)) {
			ArrayList<Player> players = nbi.getActiveFights().get(this.boss);
			for (Player p : players) {
				AbstractPlayer ap = BukkitAdapter.adapt(p);
				targets.add(ap);
			}
		}
		return targets;
	}
}
